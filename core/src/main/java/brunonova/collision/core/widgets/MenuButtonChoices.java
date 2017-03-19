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

import java.util.List;

/**
 * Groups together multiple button choices, for keyboard navigation purposes.
 */
public class MenuButtonChoices implements SupportsKeyboardNavigation {
    private final List<MenuButton> buttons;

    /**
     * Creates the group.
     * @param buttons Buttons of the group
     */
    public MenuButtonChoices(List<MenuButton> buttons) {
        this.buttons = buttons;

        // Set up the references to the  left and right widgets in the buttons
        for(int i = 0; i < buttons.size() - 1; i++) {
            MenuButton left = buttons.get(i);
            MenuButton right = buttons.get(i + 1);
            left.setRightWidget(right);
            right.setLeftWidget(left);
        }
    }

    @Override
    public void focus() {
        // Focuses the currently selected option
        for(MenuButton button: buttons) {
            if(button.isChecked()) {
                button.focus();
                return;
            }
        }
    }

    @Override
    public SupportsKeyboardNavigation getNextWidget() {
        // Get the next widget of the first button
        return buttons.isEmpty() ? null : buttons.get(0).getNextWidget();
    }

    @Override
    public void setNextWidget(SupportsKeyboardNavigation nextWidget) {
        // Set the next widget for all buttons
        for(MenuButton button: buttons) {
            button.setNextWidget(nextWidget);
        }
    }

    @Override
    public SupportsKeyboardNavigation getPreviousWidget() {
        // Get the previous widget of the first button
        return buttons.isEmpty() ? null : buttons.get(0).getPreviousWidget();
    }

    @Override
    public void setPreviousWidget(SupportsKeyboardNavigation previousWidget) {
        // Set the previous widget for all buttons
        for(MenuButton button: buttons) {
            button.setPreviousWidget(previousWidget);
        }
    }
}
