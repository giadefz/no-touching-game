package com.gamedesign.notouching.screen;

import android.graphics.Color;

import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.framework.TouchConsumer;
import com.gamedesign.notouching.touch.TutorialTouchConsumer;
import com.gamedesign.notouching.util.Assets;

import java.util.List;

public class TutorialScreen extends Screen implements BoundChecker{

    private static final int TUTORIAL_SCREENS = 4;
    public int chosenImage = 0;
    private final TouchConsumer touchConsumer = new TutorialTouchConsumer(this);
    private final MainMenuScreen mainMenuScreen;

    public TutorialScreen(Game game, MainMenuScreen mainMenuScreen) {
        super(game);
        this.mainMenuScreen = mainMenuScreen;
    }

    @Override
    public void update(float deltaTime) {
        game.getGraphics().clear(Color.argb(255, 0, 0, 0));
        game.getGraphics().drawPixmap(Assets.tutorialPixmaps[chosenImage], 0, 0);
        game.getGraphics().drawPixmap(Assets.nextLevelButton, 1500, 500);
        game.getGraphics().drawPixmap(Assets.retryButton, 300, 500);
        game.getGraphics().drawPixmap(Assets.quitButton, 1700, 100);
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        if (touchEvents.size() > 0) {
            for(Input.TouchEvent te: touchEvents) {
                touchConsumer.handleTouchEvent(te);
            }
        }
    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public void backToMainMenu(){
        game.setScreen(mainMenuScreen);
    }

}
