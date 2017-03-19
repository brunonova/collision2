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

/**
 * Interface indicating that the user can navigate to and from this widget using
 * the keyboard.
 */
public interface SupportsKeyboardNavigation {
    /**
     * Called to change the focus to this widget.
     */
    void focus();

    /**
     * Returns the next widget.
     * @return Next widget.
     */
    SupportsKeyboardNavigation getNextWidget();

    /**
     * Sets the next widget.
     * @param nextWidget Next widget.
     */
    void setNextWidget(SupportsKeyboardNavigation nextWidget);

    /**
     * Returns the previous widget.
     * @return Previous widget.
     */
    SupportsKeyboardNavigation getPreviousWidget();

    /**
     * Sets the previous widget.
     * @param previousWidget Previous widget.
     */
    void setPreviousWidget(SupportsKeyboardNavigation previousWidget);

    /**
     * Changes the focus to the next widget.
     */
    default void focusNext() {
        if(getNextWidget() != null) {
            getNextWidget().focus();
        }
    }

    /**
     * Changes the focus to the previous widget.
     */
    default void focusPrevious() {
        if(getPreviousWidget() != null) {
            getPreviousWidget().focus();
        }
    }
}
