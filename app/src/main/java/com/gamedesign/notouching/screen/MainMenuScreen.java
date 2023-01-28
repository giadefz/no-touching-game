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

import java.util.List;

public class MainMenuScreen extends Screen {

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {

        game.setScreen(new GameScreen(game));

        Graphics g = game.getGraphics();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                if(inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
                    /*Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled)*/
                        Assets.click.play(1);
                }
                if(inBounds(event, 64, 220, 192, 42) ) {
                    //game.setScreen(new GameScreen(game));
                    Assets.click.play(1);
                    /*if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;*/
                }
                if(inBounds(event, 64, 220 + 42, 192, 42) ) {
                    //game.setScreen(new HighscoreScreen(game));
                    Assets.click.play(1);
                    /*if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;*/
                }
                if(inBounds(event, 64, 220 + 84, 192, 42) ) {
                    //game.setScreen(new HelpScreen(game));
                    Assets.click.play(1);
                    /*if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;*/
                }
            }
        }
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.mainMenu, 64, 220);
        g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
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
