package com.gamedesign.notouching.touch;

import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.TouchConsumer;
import com.gamedesign.notouching.screen.BoundChecker;
import com.gamedesign.notouching.screen.TutorialScreen;

public class TutorialTouchConsumer extends TouchConsumer implements BoundChecker {

    private final TutorialScreen screen;

    public TutorialTouchConsumer(TutorialScreen screen) {
        this.screen = screen;
    }

    @Override
    protected void handleTouchDown(Input.TouchEvent event) {
        if(inBounds(event, 1500, 500, 125, 125)){
            if(screen.chosenImage < 3)
                screen.chosenImage++;
        }else if(inBounds(event, 300, 500, 125, 125)){
            if(screen.chosenImage > 0)
                screen.chosenImage--;
        }else if(inBounds(event, 1700, 100, 125, 125)){
            screen.backToMainMenu();
        }
    }

    @Override
    protected void handleTouchUp(Input.TouchEvent event) {

    }

    @Override
    protected void handleTouchDragged(Input.TouchEvent event) {

    }
}
