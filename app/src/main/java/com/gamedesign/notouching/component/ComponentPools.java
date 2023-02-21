package com.gamedesign.notouching.component;

import android.util.Log;

import com.gamedesign.notouching.framework.Pool;
import com.gamedesign.notouching.parse.ParseGameObjectJSONException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentPools {

    public static Map<Class<? extends Component>, Pool<Component>> componentPools = new HashMap<>();

    static {
        componentPools.put(PixmapDrawable.class, new Pool<>(PixmapDrawable::new, 50));
        componentPools.put(CirclePixmapDrawable.class, new Pool<>(CirclePixmapDrawable::new, 50));
        componentPools.put(Position.class, new Pool<>(Position::new, 50));
        componentPools.put(Exploding.class, new Pool<>(Exploding::new, 5));
        componentPools.put(ChassisEngine.class, new Pool<>(ChassisEngine::new, 50));
    }

    private ComponentPools(){
    }

    public static Component getNewInstance(Class<? extends Component> componentClass){
        Pool<?> pool = componentPools.get(componentClass);
        if(pool == null){
            Log.i("POOLS", "No pool present for component: " + componentClass);
            try {
                Constructor<? extends Component> constructor = componentClass.getConstructor();
                return constructor.newInstance();
            } catch (NoSuchMethodException e) {
                throw new ParseGameObjectJSONException("No empty constructor for Component: " +componentClass);
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                throw new ParseGameObjectJSONException(e.toString());
            }
        }
        return (Component) pool.newObject();
    }

    public static void freeComponent(Component component){
        Pool<Component> componentPool = componentPools.get(component.getClass());
        if(componentPool == null) throw new IllegalArgumentException("No component pool found for type: " + component.getClass());
        componentPool.free(component);
    }

    public static void freeComponents(List<Component> components){
        components.forEach(ComponentPools::freeComponent);
    }

}
