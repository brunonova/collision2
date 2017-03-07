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
import brunonova.collision.core.widgets.Menu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Screen that asks confirmation from the user to quit.
 */
public class QuitScreen extends BaseScreen {
    /** The (optional) background image. */
    protected Texture background;
    private Menu menu;

    /**
     * Creates the screen.
     * @param game The game.
     */
    public QuitScreen(Collision game) {
        super(game);
        backgroundColor = Color.BLACK;
    }

    @Override
    public void create() {
        super.create();

        // Create the menu
        menu = addActor(new Menu(game, game.t("quit.title"), Color.WHITE));
        menu.addButton(game.t("quit.yes"), this::yes);
        menu.addButton(game.t("quit.no"), this::no);
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
    }

    @Override
    public boolean keyDown(int keycode) {
        // Cancel when Escape is pressed
        if(keycode == Input.Keys.ESCAPE) {
            no();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if(background != null) background.dispose();
    }

    /**
     * Quits to the menu screen.
     */
    public void yes() {
        game.getGameScreen().dispose();
        game.returnToMenu();
    }

    /**
     * Returns to the game screen.
     */
    public void no() {
        game.startGame();
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
