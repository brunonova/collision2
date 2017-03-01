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

/**
 * The options menu.
 */
public class OptionsScreen extends BaseScreen {
    private Menu menu;

    public OptionsScreen(Collision game) {
        super(game);
    }

    @Override
    public void create() {
        super.create();

        // Create the menu
        menu = addActor(new Menu(game, game.t("options.title")));
        menu.addButton(game.t("options.back"), this::back);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Return to the main menu when the Escape key is pressed
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            back();
        }
    }

    /**
     * Returns to the main menu.
     */
    private void back() {
        game.returnToMenu();
    }
}
