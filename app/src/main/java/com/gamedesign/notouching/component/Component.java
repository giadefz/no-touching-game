package com.gamedesign.notouching.component;

import com.gamedesign.notouching.util.GameObjectsJSON;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public abstract class Component {

    protected Entity owner;
    public abstract ComponentType type();

    public static Class<?> classForType(ComponentType type) throws ClassNotFoundException {
        String packageName = Component.class.getPackage().getName() + ".";
        return Class.forName(packageName + type.name());
    }

    public static Component fromComponentJSON(GameObjectsJSON.ComponentJSON componentJSON, GameObject owner){
        try {
            Class<?> componentClass = classForType(componentJSON.component);
            Constructor<?> constructor = componentClass.getConstructor();
            Component component = (Component) constructor.newInstance();
            component.owner = owner;
            component.fillFieldsFromArgs(componentJSON.args);
            return component;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("No class found for type: " + componentJSON.component);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No empty constructor for type: " + componentJSON.component);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException("Error instantiating for type: " + componentJSON.component);
        }
    }

    public abstract void fillFieldsFromArgs(List<String> args);

}
