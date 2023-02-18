package com.gamedesign.notouching.screen;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import com.gamedesign.notouching.R;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Pixmap;
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
                if(inBounds(event, 813, 573, 320, 100) ) {
                    game.setScreen(new GameScreen(game));
                    Assets.click.play(1);
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                } else if (inBounds(event, 813, 790, 320, 55)) {
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
        g.drawPixmap(Assets.mainMenuPlayButton, 800, 500);
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
