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
import brunonova.collision.core.Constants;
import brunonova.collision.core.widgets.Menu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * The main menu.
 */
public class MenuScreen extends BaseScreen {
    private Menu menu;

    /**
     * Creates the screen.
     * @param game The game.
     */
    public MenuScreen(Collision game) {
        super(game);
    }

    @Override
    public void create() {
        super.create();

        // Create the styles
        Label.LabelStyle versionStyle = new Label.LabelStyle(game.getFont("font-hud.ttf"), Color.BLACK);

        // Create the menu
        menu = addActor(new Menu(game, game.t("game.title")));
        menu.addButton(game.t("menu.play"), this::play);
        menu.addButton(game.t("menu.options"), this::options);
        menu.addButton(game.t("menu.highscores"), this::highscores);
        menu.addButton(game.t("menu.quit"), this::quit);

        // Add the version of the game to the bottom left corner
        Label version = new Label(game.t("menu.version", Constants.getVersion()), versionStyle);
        version.setPosition(10, 10);
        addActor(version);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Quit game when the Escape key is pressed
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            quit();
        }
    }

    /**
     * Starts a new game.
     */
    private void play() {
        game.startGame();
    }

    /**
     * Opens the options menu.
     */
    private void options() {
        game.showOptionsMenu();
    }

    /**
     * Opens the high scores screen.
     */
    private void highscores() {
        // TODO
    }

    /**
     * Exits the game.
     */
    private void quit() {
        Gdx.app.exit();
    }
}
