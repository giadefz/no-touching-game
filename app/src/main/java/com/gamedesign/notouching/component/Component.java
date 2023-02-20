package com.gamedesign.notouching.component;

import com.gamedesign.notouching.framework.Pool;
import com.gamedesign.notouching.parse.FieldSetter;
import com.gamedesign.notouching.parse.FieldSetters;
import com.gamedesign.notouching.parse.GameObjectsJSON;
import com.gamedesign.notouching.parse.ParseGameObjectJSONException;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public abstract class Component {

    public Entity owner;

    private static final Map<Class<?>, FieldSetter> fieldSetterMap = new HashMap<>();
    private static final Map<Class<? extends Component>, List<Field>> fieldsMap = new ConcurrentHashMap<>();

    static {
        fieldSetterMap.put(int.class, FieldSetters.INT.getSetter());
        fieldSetterMap.put(float.class, FieldSetters.FLOAT.getSetter());
        fieldSetterMap.put(boolean.class, FieldSetters.BOOLEAN.getSetter());
        fieldSetterMap.put(String.class, FieldSetters.STRING.getSetter());
    }

    public static Class<? extends Component> classForComponentType(String componentName) throws ClassNotFoundException {
        String packageName = Component.class.getPackage().getName() + ".";
        return (Class<? extends Component>) Class.forName(packageName + componentName);
    }

    public static Component fromComponentJSON(GameObjectsJSON.ComponentJSON componentJSON, GameObject owner) {
        try {
            Class<? extends Component> componentClass = classForComponentType(componentJSON.component);
            Component component = ComponentPools.getNewInstance(componentClass);
            component.owner = owner;
            component.fillFieldsFromArgs(componentJSON.args, componentClass);
            component.postConstructOperations();
            return component;
        } catch (ClassNotFoundException e) {
            throw new ParseGameObjectJSONException("No class found for component type: " + componentJSON.component);
        }
    }

    public void fillFieldsFromArgs(List<GameObjectsJSON.Argument> args, Class<? extends Component> componentClass) {
        List<Field> fields = fieldsMap.computeIfAbsent(componentClass, c -> Arrays.asList(c.getDeclaredFields()));
        args.forEach(arg -> {
            Field field = fields.stream()
                    .filter(f -> f.getName().equals(arg.name))
                    .findFirst()
                    .orElseThrow(() -> new ParseGameObjectJSONException("No field with name: " + arg.name + " for type: " + componentClass));

            FieldSetter fieldSetter = fieldSetterMap.get(field.getType());
            if(fieldSetter == null) throw new ParseGameObjectJSONException("No field setter for type: " + field.getType());
            fieldSetter.setField(field, arg.value, this);
        });
    }

    public abstract ComponentType type();

    public void postConstructOperations(){

    }

}
