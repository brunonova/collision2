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
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A widget that displays a menu with a title.
 */
public class Menu extends Table {
    // Reference to the game
    private final Collision game;

    // Styles
    private Label.LabelStyle titleStyle, labelStyle;
    private TextButton.TextButtonStyle buttonStyle, choiceButtonStyle;
    private TextField.TextFieldStyle textFieldStyle;
    private final Color textColor;

    // Widgets
    private final Label titleLabel;
    private final List<TextButton> buttons;

    /**
     * Creates the menu.
     * @param game The game.
     * @param title Title of the menu.
     */
    public Menu(Collision game, String title) {
        this(game, title, Color.BLACK);
    }

    /**
     * Creates the menu.
     * @param game The game.
     * @param title Title of the menu.
     * @param textColor Color of the text (default: black).
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Menu(Collision game, String title, Color textColor) {
        this.game = game;
        setFillParent(true);
        top();
        buttons = new ArrayList<>();
        this.textColor = textColor;
        setupStyles();

        // Add the title label
        titleLabel = new Label(title, titleStyle);
        add(titleLabel).padBottom(100).padTop(20);
    }

    /**
     * Adds a button to the menu.
     * @param text Label of the button.
     * @param action Action to run when the button is clicked.
     * @return The added button.
     */
    public TextButton addButton(String text, Runnable action) {
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

        return button;
    }

    /**
     * Adds a multiple choice option.
     * @param <T> Type of the value of the option.
     * @param labelText Text of the label.
     * @param defaultValue Default value of the option.
     * @param choices Definition of the available options.
     * @return The added buttons.
     */
    public <T> List<TextButton> addButtonChoices(String labelText, T defaultValue, ButtonChoice<T>... choices) {
        row();  // add a row

        // Create a table for this row
        Table row = new Table();
        List<TextButton> newButtons = new ArrayList<>();
        add(row);

        // Create a button group to ensure that only 1 choice is selected
        ButtonGroup<TextButton> group = new ButtonGroup<>();
        group.setMinCheckCount(1);
        group.setMaxCheckCount(1);
        group.setUncheckLast(true);

        // Create the label
        Label label = new Label(labelText, labelStyle);
        row.add(label);

        // Create a "radio" button for each choice
        for(ButtonChoice choice: choices) {
            // Create the button
            TextButton button = new TextButton(choice.getText(), choiceButtonStyle);
            group.add(button);
            buttons.add(button);
            newButtons.add(button);
            row.add(button).padLeft(20);

            // Select this button if it has the default value
            if(Objects.equals(defaultValue, choice.getValue())) {
                button.setChecked(true);
            }

            // Setup the listener
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    TextButton button = (TextButton) actor;  // it's always a TextButton
                    if(button.isChecked()) {
                        choice.getAction().run(choice.getValue());
                    }
                }
            });
        }

        return newButtons;
    }

    /**
     * Adds a text field with a label to the menu.
     * @param labelText Text of the label.
     * @param defaultText Default text of the field.
     * @return The created text field.
     */
    public TextField addTextField(String labelText, String defaultText) {
        row();  // add a row

        // Create a table for this row
        Table row = new Table();
        add(row);

        // Create the label
        Label label = new Label(labelText, labelStyle);
        row.add(label);

        // Create the text field
        TextField textField = new TextField(defaultText, textFieldStyle);
        row.add(textField).width(200).padLeft(20);

        return textField;
    }

    /**
     * Creates the styles for the widgets.
     */
    private void setupStyles() {
        // Style of the title
        titleStyle = new Label.LabelStyle(game.getFont("font-title.ttf"), textColor);

        // Style of the labels
        labelStyle = new Label.LabelStyle(game.getFont("font-menu.ttf"), textColor);

        // Style of the buttons
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getFont("font-menu.ttf");
        buttonStyle.fontColor = textColor;
        buttonStyle.overFontColor = Color.FOREST;
        buttonStyle.downFontColor = Color.GREEN;

        // Style of the radio buttons
        choiceButtonStyle = new TextButton.TextButtonStyle();
        choiceButtonStyle.font = game.getFont("font-menu.ttf");
        choiceButtonStyle.fontColor = textColor;
        choiceButtonStyle.overFontColor = Color.FOREST;
        choiceButtonStyle.downFontColor = Color.GREEN;
        choiceButtonStyle.checkedFontColor = Color.RED;

        // Style of the text fields
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = game.getFont("font-menu.ttf");
        textFieldStyle.fontColor = textColor;
        textFieldStyle.cursor = new RectangleDrawable(game.getShapeRenderer(), textColor);
        textFieldStyle.selection = new RectangleDrawable(game.getShapeRenderer(), Color.GRAY);
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

    /**
     * Returns the style used by the title.
     * @return Title style.
     */
    public Label.LabelStyle getTitleStyle() {
        return titleStyle;
    }

    /**
     * Returns the style used by labels.
     * @return Label style.
     */
    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }

    /**
     * Returns the style used by buttons.
     * @return Button style.
     */
    public TextButton.TextButtonStyle getButtonStyle() {
        return buttonStyle;
    }

    /**
     * Returns the style used by multiple-choice buttons.
     * @return Multiple-choice button style.
     */
    public TextButton.TextButtonStyle getChoiceButtonStyle() {
        return choiceButtonStyle;
    }

    /**
     * Returns the style used by text fields.
     * @return Text field style.
     */
    public TextField.TextFieldStyle getTextFieldStyle() {
        return textFieldStyle;
    }

    /**
     * Returns the foreground color of the menu items.
     * @return Foreground color.
     */
    public Color getTextColor() {
        return textColor;
    }
}
