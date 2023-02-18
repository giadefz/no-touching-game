package com.gamedesign.notouching.util;

import com.gamedesign.notouching.framework.Pool;
import com.gamedesign.notouching.level.Rope;

import java.util.List;

public class RopePool {

    public static Pool<Rope> ropePool = new Pool<>(Rope::new, 20);

    private RopePool() {
    }

    public static Rope newRope(){
        return ropePool.newObject();
    }

    public static void freeRope(Rope gameObject){
        ropePool.free(gameObject);
    }

    public static void freeRopes(List<Rope> ropes){
        ropes.forEach(RopePool::freeRope);
    }


}
