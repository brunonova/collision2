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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

/**
 * A custom {@link TextField} that supports keyboard navigation.
 */
public class MenuTextField extends TextField implements SupportsKeyboardNavigation {
    // Next and previous widgets for keyboard navigation
    private SupportsKeyboardNavigation nextWidget, previousWidget;

    /**
     * Creates the text field.
     * @param text Default text of the field.
     * @param style Style of the text field.
     * @param navigateSound Sound played on keyboard navigation.
     * @param game The game.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MenuTextField(String text, TextFieldStyle style, Sound navigateSound, Collision game) {
        super(text, style);

        // Focus listener to make the text field blink when it gains focus
        addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                actor.clearActions();
                actor.getColor().a = 1;
                if(focused) {
                    actor.addAction(Menu.createBlinkAction());
                }
            }
        });

        // Mouse listener to focus the text field when the mouse cursor enters it
        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                focus();
            }
        });

        // Keyboard listener and to change the focused widget when the arrow
        // or Enter keys are pressed
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode) {
                    case Input.Keys.ENTER:
                    case Input.Keys.DOWN:
                        if(getNextWidget() != null) {
                            focusNext();
                            navigateSound.play(game.getVolume() * Menu.MENU_NAVIGATE_VOLUME_FACTOR);
                        }
                        return true;
                    case Input.Keys.UP:
                        if(getPreviousWidget() != null) {
                            focusPrevious();
                            navigateSound.play(game.getVolume() * Menu.MENU_NAVIGATE_VOLUME_FACTOR);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void focus() {
        getStage().setKeyboardFocus(this);
    }

    @Override
    public SupportsKeyboardNavigation getNextWidget() {
        return nextWidget;
    }

    @Override
    public void setNextWidget(SupportsKeyboardNavigation nextWidget) {
        this.nextWidget = nextWidget;
    }

    @Override
    public SupportsKeyboardNavigation getPreviousWidget() {
        return previousWidget;
    }

    @Override
    public void setPreviousWidget(SupportsKeyboardNavigation previousWidget) {
        this.previousWidget = previousWidget;
    }
}
