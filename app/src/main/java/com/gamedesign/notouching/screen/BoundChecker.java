package com.gamedesign.notouching.screen;

import com.gamedesign.notouching.framework.Input;

public interface BoundChecker {

    default boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height){
        if(event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

}
