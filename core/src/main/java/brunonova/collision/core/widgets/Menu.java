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
package brunonova.collision.core.widgets;

import brunonova.collision.core.Collision;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A widget that displays a menu with a title.
 */
public class Menu extends Table {
    // Reference to the game
    private final Collision game;

    // Styles
    private Label.LabelStyle titleStyle;
    TextButton.TextButtonStyle buttonStyle;

    // Widgets
    private final Label titleLabel;
    private final List<TextButton> buttons;

    /**
     * Creates the menu.
     * @param game The game.
     * @param title Title of the menu.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Menu(Collision game, String title) {
        this.game = game;
        setFillParent(true);
        top();
        buttons = new ArrayList<>();
        setupStyles();

        // Add the title label
        titleLabel = new Label(title, titleStyle);
        add(titleLabel).padBottom(100).padTop(20);
    }

    /**
     * Adds a button to the menu.
     * @param text Label of the button.
     * @param action Action to run when the button is clicked.
     */
    public void addButton(String text, Runnable action) {
        row();  // add a row

        // Create the button
        TextButton button = new TextButton(text, buttonStyle);
        buttons.add(button);
        add(button);

        // Setup the listener
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                action.run();
            }
        });
    }

    /**
     * Creates the styles for the widgets.
     */
    private void setupStyles() {
        // Style of the title
        titleStyle = new Label.LabelStyle(game.getFont("font-title.ttf"), Color.BLACK);

        // Style of the buttons
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getFont("font-menu.ttf");
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.overFontColor = Color.FOREST;
        buttonStyle.downFontColor = Color.GREEN;
    }

    /**
     * Returns the title label.
     * @return Title label.
     */
    public Label getTitleLabel() {
        return titleLabel;
    }

    /**
     * Returns the buttons of the menu.
     * @return Menu buttons.
     */
    public List<TextButton> getButtons() {
        return buttons;
    }
}
