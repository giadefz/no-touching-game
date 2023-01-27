package com.gamedesign.notouching.component;

import com.gamedesign.notouching.parse.FieldSetter;
import com.gamedesign.notouching.parse.FieldSetters;
import com.gamedesign.notouching.parse.GameObjectsJSON;
import com.gamedesign.notouching.parse.ParseGameObjectJSONException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public abstract class Component {

    protected Entity owner;

    public static Map<Class<?>, FieldSetter> fieldSetterMap = new HashMap<>();


    static {
        fieldSetterMap.put(int.class, FieldSetters.INT.getSetter());
        fieldSetterMap.put(float.class, FieldSetters.FLOAT.getSetter());
        fieldSetterMap.put(boolean.class, FieldSetters.BOOLEAN.getSetter());
    }

    public static Class<?> classForComponentType(String componentName) throws ClassNotFoundException {
        String packageName = Component.class.getPackage().getName() + ".";
        return Class.forName(packageName + componentName);
    }

    public static Component fromComponentJSON(GameObjectsJSON.ComponentJSON componentJSON, GameObject owner) {
        try {
            Class<?> componentClass = classForComponentType(componentJSON.component);
            Constructor<?> constructor = componentClass.getConstructor();
            Component component = (Component) constructor.newInstance();
            component.owner = owner;
            component.fillFieldsFromArgs(componentJSON.args, componentClass);
            component.postConstructOperations();
            return component;
        } catch (ClassNotFoundException e) {
            throw new ParseGameObjectJSONException("No class found for component type: " + componentJSON.component);
        } catch (NoSuchMethodException e) {
            throw new ParseGameObjectJSONException("No empty constructor for component type: " + componentJSON.component);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new ParseGameObjectJSONException("Error instantiating for component type: " + componentJSON.component);
        }
    }

    public void fillFieldsFromArgs(List<GameObjectsJSON.Argument> args, Class<?> componentClass) {
        List<Field> fields = Arrays.asList(componentClass.getDeclaredFields());
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
