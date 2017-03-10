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
import brunonova.collision.core.HighScores;
import brunonova.collision.core.enums.Difficulty;
import brunonova.collision.core.enums.GameMode;
import brunonova.collision.core.widgets.ButtonChoice;
import brunonova.collision.core.widgets.Menu;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import java.util.List;

/**
 * The screen that shows the high scores.
 */
public class HighScoresScreen extends BaseScreen {
    private Menu menu;
    private Label[] tableHeaderColumns;
    private Label[][] tableCells;

    private GameMode mode;
    private Difficulty difficulty;
    private int scoreIndexToHighlight = -1;

    /**
     * Creates the screen.
     * @param game The game.
     */
    public HighScoresScreen(Collision game) {
        super(game);
        mode = game.getGameMode();
        difficulty = game.getDifficulty();
    }

    /**
     * Creates the screen.
     * @param game The game.
     * @param scoreIndexToHighlight The index of the score to highlight.
     */
    public HighScoresScreen(Collision game, int scoreIndexToHighlight) {
        this(game);
        this.scoreIndexToHighlight = scoreIndexToHighlight;
    }

    @Override
    public void create() {
        super.create();

        // Create the menu
        menu = addActor(new Menu(game, game.t("highScores.title")));
        menu.addButtonChoices(game.t("options.mode"), game.getGameMode(),
                new ButtonChoice<>(game.t("options.mode.time"), GameMode.TIME, this::changeMode),
                new ButtonChoice<>(game.t("options.mode.coins"), GameMode.COINS, this::changeMode));
        menu.addButtonChoices(game.t("options.difficulty"), game.getDifficulty(),
                new ButtonChoice<>(game.t("options.difficulty.easy"), Difficulty.EASY, this::changeDifficulty),
                new ButtonChoice<>(game.t("options.difficulty.medium"), Difficulty.MEDIUM, this::changeDifficulty),
                new ButtonChoice<>(game.t("options.difficulty.hard"), Difficulty.HARD, this::changeDifficulty));

        // Add the high scores table to the menu, before the "Back" button
        menu.row();
        menu.add(createScoresTable())
                .width(game.getWidth() - 20)
                .spaceTop(30)
                .spaceBottom(30);

        menu.addButton(game.t("options.back"), this::back);

        // Fill the high scores table
        updateScores();

        // Highlight the new high score, if there is one
        if(scoreIndexToHighlight >= 0 && scoreIndexToHighlight < tableCells.length) {
            Label[] row = tableCells[scoreIndexToHighlight];
            row[0].addAction(createBlinkAction());
            row[1].addAction(createBlinkAction());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        // Return to the main menu when Escape is pressed
        if(keycode == Input.Keys.ESCAPE) {
            back();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates and returns the table that will show the high scores.
     * @return Table that will show the high scores.
     */
    private Table createScoresTable() {
        Table table = new Table();
        Label.LabelStyle labelStyle = menu.getLabelStyle();

        // Create the header columns
        tableHeaderColumns = new Label[2];
        tableHeaderColumns[0] = new Label(game.t("highScores.name"), labelStyle);
        tableHeaderColumns[1] = new Label(game.t("highScores.time"), labelStyle);
        tableHeaderColumns[0].setAlignment(Align.left);
        tableHeaderColumns[1].setAlignment(Align.right);
        table.add(tableHeaderColumns[0]).expandX().left();
        table.add(tableHeaderColumns[1]).width(100).right();

        // Create the rows of the table body
        tableCells = new Label[HighScores.MAX_SCORES][2];
        for(Label[] row : tableCells) {
            row[0] = new Label("-", labelStyle);
            row[1] = new Label("-", labelStyle);
            row[0].setAlignment(Align.left);
            row[1].setAlignment(Align.right);

            table.row();
            table.add(row[0]).expandX().left();
            table.add(row[1]).width(150).right();
        }

        return table;
    }

    /**
     * Updates the scores in the high scores table.
     */
    private void updateScores() {
        // Update the 2nd column header according to the game mode
        switch(mode) {
            case TIME:
                tableHeaderColumns[1].setText(game.t("highScores.time"));
                break;
            case COINS:
                tableHeaderColumns[1].setText(game.t("highScores.coins"));
                break;
        }

        // Update the scores
        List<HighScores.Score> scores = game.getHighScores().getScores(mode, difficulty);
        for(int i = 0; i < tableCells.length; i++) {
            Label[] row = tableCells[i];
            if(i <scores.size()) {
                HighScores.Score score = scores.get(i);
                row[0].setText(score.getName());
                row[1].setText("" + score.getScore());
            } else {
                // No score in this index
                row[0].setText("-");
                row[1].setText("-");
            }

            // Stop blinking, if applicable
            row[0].clearActions();
            row[0].getColor().a = 1;
            row[1].clearActions();
            row[1].getColor().a = 1;
        }
    }

    /**
     * Creates and returns a blinking action.
     * @return A "blink" action.
     */
    private Action createBlinkAction() {
        return Actions.forever(
                Actions.sequence(
                        Actions.fadeOut(0.5f),
                        Actions.fadeIn(0.5f)));
    }

    /**
     * Changes the selected game mode.
     * @param mode The new game mode.
     */
    private void changeMode(GameMode mode) {
        this.mode = mode;
        updateScores();
    }

    /**
     * Changes the selected difficulty.
     * @param difficulty The new difficulty.
     */
    private void changeDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        updateScores();
    }

    /**
     * Returns to the main menu.
     */
    private void back() {
        game.returnToMenu();
    }
}
