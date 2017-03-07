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
import brunonova.collision.core.enums.Difficulty;
import brunonova.collision.core.enums.GameMode;
import brunonova.collision.core.widgets.Menu;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * The "game over" that allows the user to record an high score.
 */
public class GameOverScreen extends BaseScreen {
    private Menu menu;
    private TextField nameField;

    // Data for the high score
    private GameMode mode;
    private Difficulty difficulty;
    private int score;

    /**
     * Creates the screen.
     * @param game The game
     */
    public GameOverScreen(Collision game) {
        super(game);
    }

    @Override
    public void create() {
        super.create();

        // Create the menu
        menu = addActor(new Menu(game, game.t("gameOver.title")));
        nameField = menu.addTextField(game.t("gameOver.yourName"), game.t("gameOver.defaultName"));
        menu.addButton(game.t("gameOver.ok"), this::ok);
        menu.addButton(game.t("gameOver.ignore"), this::ignore);
    }

    @Override
    public boolean keyDown(int keycode) {
        // Return to the main menu when Escape is pressed
        if(keycode == Input.Keys.ESCAPE) {
            ignore();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the score to the high scores table.
     */
    private void ok() {
        String name = nameField.getText();
        if(name != null && !name.isEmpty()) {
            game.getHighScores().addScore(mode, difficulty, name, score);
            ignore();  // TODO: go to the high scores screen
        } else {
            ignore();
        }
    }

    /**
     * Returns to the main menu.
     */
    private void ignore() {
        game.returnToMenu();
    }

    /**
     * Sets the data for the high score.
     * @param mode The game mode.
     * @param difficulty The difficulty.
     * @param score The score.
     */
    public void setData(GameMode mode, Difficulty difficulty, int score) {
        this.mode = mode;
        this.difficulty = difficulty;
        this.score = score;
    }
}
