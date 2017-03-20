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
import brunonova.collision.core.widgets.ButtonChoice;
import brunonova.collision.core.widgets.Menu;
import brunonova.collision.core.widgets.MenuButton;
import com.badlogic.gdx.Input;
import java.util.List;

/**
 * The options menu.
 */
public class OptionsScreen extends BaseScreen {
    private Menu menu;
    private List<MenuButton> fullScreenOptions;

    /**
     * Creates the screen.
     * @param game The game.
     */
    public OptionsScreen(Collision game) {
        super(game);
    }

    @Override
    public void create() {
        super.create();

        // Create the menu
        menu = addActor(new Menu(game, game.t("options.title")));
        menu.addButtonChoices(game.t("options.mode"), game.getGameMode(),
                new ButtonChoice<>(game.t("options.mode.time"), GameMode.TIME, this::changeMode),
                new ButtonChoice<>(game.t("options.mode.coins"), GameMode.COINS, this::changeMode));
        menu.addButtonChoices(game.t("options.difficulty"), game.getDifficulty(),
                new ButtonChoice<>(game.t("options.difficulty.easy"), Difficulty.EASY, this::changeDifficulty),
                new ButtonChoice<>(game.t("options.difficulty.medium"), Difficulty.MEDIUM, this::changeDifficulty),
                new ButtonChoice<>(game.t("options.difficulty.hard"), Difficulty.HARD, this::changeDifficulty));
        fullScreenOptions = menu.addButtonChoices(game.t("options.fullScreen"), game.isFullScreen(),
                new ButtonChoice<>(game.t("options.off"), false, this::changeFullScreen),
                new ButtonChoice<>(game.t("options.on"), true, this::changeFullScreen));
        menu.addButtonChoices(game.t("options.sound"), game.getVolume(),
                new ButtonChoice<>(game.t("options.off"), 0f, this::changeVolume),
                new ButtonChoice<>(game.t("options.on"), 1f, this::changeVolume));
        menu.addButtonChoices(game.t("options.showFPS"), game.isShowFPS(),
                new ButtonChoice<>(game.t("options.off"), false, this::changeShowFPS),
                new ButtonChoice<>(game.t("options.on"), true, this::changeShowFPS));
        menu.addButton(game.t("options.back"), this::back);
    }

    @Override
    public boolean keyDown(int keycode) {
        // Return to the main menu when the Escape key is pressed
        if(keycode == Input.Keys.ESCAPE) {
            back();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the currently selected full screen option according to the
     * current mode.
     */
    public void updateFullScreenOption() {
        if(fullScreenOptions != null && fullScreenOptions.size() == 2) {
            // Get the ON or OFF button, depending on the full screen mode
            int index = game.isFullScreen() ? 1 : 0;
            MenuButton button = fullScreenOptions.get(index);

            // Check that button without firing events
            button.setProgrammaticChangeEvents(false);
            fullScreenOptions.get(index).setChecked(true);
            button.setProgrammaticChangeEvents(true);
        }
    }

    /**
     * Changes the game mode.
     * @param mode New mode.
     */
    private void changeMode(GameMode mode) {
        game.setGameMode(mode);
    }

    /**
     * Changes the game difficulty.
     * @param difficulty The new difficulty level.
     */
    private void changeDifficulty(Difficulty difficulty) {
        game.setDifficulty(difficulty);
    }

    /**
     * Enables or disables full screen mode.
     * @param fullScreen {@code true} to enable it.
     */
    private void changeFullScreen(Boolean fullScreen) {
        game.setFullScreen(fullScreen);
    }

    /**
     * Changes the sound volume.
     * @param volume New volume.
     */
    private void changeVolume(float volume) {
        game.setVolume(volume);
    }

    /**
     * Enables or disables the FPS indicator during the game.
     * @param value {@code true} to enable it.
     */
    private void changeShowFPS(Boolean value) {
        game.setShowFPS(value);
    }

    /**
     * Returns to the main menu.
     */
    private void back() {
        game.returnToMenu();
    }
}
