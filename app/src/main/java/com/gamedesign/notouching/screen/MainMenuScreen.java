package com.gamedesign.notouching.screen;

import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.Settings;

import java.util.List;

public class MainMenuScreen extends Screen implements BoundChecker {

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {

        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                if(inBounds(event, 804, 630, 320, 160) ) {
                    game.setScreen(new GameScreen(game));
                    Assets.click.play(1);
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }else if(inBounds(event, 804, 920, 320, 160) ) {
                    game.setScreen(new TutorialScreen(game, this));
                    Assets.click.play(1);
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.mainMenuPlayButton, 800, 620);
        g.drawPixmap(Assets.mainMenuPlayButton, 800, 920);
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
}
