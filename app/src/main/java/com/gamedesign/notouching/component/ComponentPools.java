package com.gamedesign.notouching.component;

import com.gamedesign.notouching.framework.Pool;
import com.gamedesign.notouching.parse.ParseGameObjectJSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentPools {

    public static Map<Class<? extends Component>, Pool<Component>> componentPools = new HashMap<>();

    static {
        componentPools.put(PixmapDrawable.class, new Pool<>(PixmapDrawable::new, 30));
        componentPools.put(CirclePixmapDrawable.class, new Pool<>(CirclePixmapDrawable::new, 30));
        componentPools.put(Position.class, new Pool<>(Position::new, 30));
        componentPools.put(Exploding.class, new Pool<>(Exploding::new, 30));
    }

    private ComponentPools(){
    }

    public static Component getNewInstance(Class<? extends Component> componentClass){
        Pool<?> pool = componentPools.get(componentClass);
        if(pool == null){
            throw new ParseGameObjectJSONException("No pool present for class: " + componentClass);
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
