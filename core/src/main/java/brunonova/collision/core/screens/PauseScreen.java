/*
 * Copyright (C) 2017 Bruno Nova <brunomb.nova@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package brunonova.collision.core.screens;

import brunonova.collision.core.Collision;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

/**
 * The "PAUSE" screen.
 * <p>Call {@link #setPreviousScreen(com.badlogic.gdx.Screen)} to set the screen
 * that the game should return to when it is unpaused.</p>
 */
public class PauseScreen extends BaseScreen {
    /** The screen to return to when the game is unpaused. */
    protected Screen previousScreen;
    /** The (optional) background image. */
    protected Texture background;

    /**
     * Creates the screen.
     * @param game The game.
     */
    public PauseScreen(Collision game) {
        super(game);
        backgroundColor = Color.BLACK;
    }

    @Override
    public void create() {
        super.create();

        // Prepare the font
        BitmapFont font = game.getFont("font-pause.ttf");

        // Add the "PAUSE" label
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        Label label = new Label(game.t("pause"), style);
        label.setPosition(game.getWidth() / 2, game.getHeight() / 2, Align.center);
        addActor(label);

        // Add a blinking animation to the label
        label.addAction(Actions.repeat(
                RepeatAction.FOREVER, Actions.sequence(
                        Actions.fadeOut(0.3f),
                        Actions.fadeIn(0.3f)
                )
        ));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Draw the screenshot of the previous screen, if set (half transparent)
        if(background != null) {
            batch.begin();
            Color oldColor = batch.getColor();
            Color newColor = oldColor.cpy();
            newColor.a = 0.5f;
            batch.setColor(newColor);
            batch.draw(background, 0, 0, game.getWidth(), game.getHeight());
            batch.setColor(oldColor);
            batch.end();
        }

        // Unpause the game when 'P' is pressed
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.setScreen(previousScreen);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
    }

    /**
     * Returns the screen to return to when the game is unpaused.
     * @return The screen to return to when the game is unpaused.
     */
    public Screen getPreviousScreen() {
        return previousScreen;
    }

    /**
     * Sets the screen to return to when the game is unpaused.
     * @param previousScreen The new screen to return to.
     */
    public void setPreviousScreen(Screen previousScreen) {
        this.previousScreen = previousScreen;
    }

    /**
     * Returns the background image.
     * @return The background image.
     */
    public Texture getBackground() {
        return background;
    }

    /**
     * Sets the background image.
     * @param background The new image.
     */
    public void setBackground(Texture background) {
        if(this.background != null) this.background.dispose();
        this.background = background;
    }
}
