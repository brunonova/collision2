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

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

/**
 * A custom {@link TextButton} that supports keyboard navigation.
 */
public class MenuButton extends TextButton implements SupportsKeyboardNavigation {
    // Next and previous widgets for keyboard navigation, and also the left and
    // right widgets for multiple choice groups of buttons
    private SupportsKeyboardNavigation nextWidget, previousWidget, leftWidget, rightWidget;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MenuButton(String text, TextButtonStyle style) {
        super(text, style);

        // Focus listener to make the button blink when it gains focus
        addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                actor.clearActions();
                actor.getColor().a = 1;
                if(focused) {
                    actor.addAction(createBlinkAction());
                }
            }
        });

        // Mouse listener to focus the button when the mouse cursor enters it
        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                focus();
            }
        });

        // Keyboard listener to execute the button when Enter is pressed, and to
        // change the focused widget when the arrow keys are pressed
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode) {
                    case Input.Keys.ENTER:
                        toggle();
                        return true;
                    case Input.Keys.DOWN:
                        focusNext();
                        return true;
                    case Input.Keys.UP:
                        focusPrevious();
                        return true;
                    case Input.Keys.LEFT:
                        focusLeft();
                        return true;
                    case Input.Keys.RIGHT:
                        focusRight();
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

    /**
     * Changes the focus to the left widget.
     */
    public void focusLeft() {
        if(getLeftWidget() != null) {
            getLeftWidget().focus();
        }
    }

    /**
     * Changes the focus to the right widget.
     */
    public void focusRight() {
        if(getRightWidget() != null) {
            getRightWidget().focus();
        }
    }

    /**
     * Returns the left widget, for multiple choice button groups.
     * @return Left widget.
     */
    public SupportsKeyboardNavigation getLeftWidget() {
        return leftWidget;
    }

    /**
     * Sets the left widget, for multiple choice button groups.
     * @param leftWidget Left widget.
     */
    public void setLeftWidget(SupportsKeyboardNavigation leftWidget) {
        this.leftWidget = leftWidget;
    }

    /**
     * Returns the right widget, for multiple choice button groups.
     * @return Right widget.
     */
    public SupportsKeyboardNavigation getRightWidget() {
        return rightWidget;
    }

    /**
     * Sets the right widget, for multiple choice button groups.
     * @param rightWidget Right widget.
     */
    public void setRightWidget(SupportsKeyboardNavigation rightWidget) {
        this.rightWidget = rightWidget;
    }

    /**
     * Creates and returns a blinking action.
     * @return A "blink" action.
     */
    private Action createBlinkAction() {
        return Actions.forever(
                Actions.sequence(
                        Actions.fadeOut(0.25f),
                        Actions.fadeIn(0.25f)));
    }
}
