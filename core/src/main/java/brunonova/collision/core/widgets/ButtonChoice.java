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
 * Definition of a choice for a "radio" button.
 * @param <T> Type of the value.
 */
public class ButtonChoice<T> {
    private final String text;
    private final T value;
    private final ChoiceRunnable<T> action;

    /**
     * Creates the choice.
     * @param text Text of the choice.
     * @param value The value that corresponds to this choice and that is sent
     *              as an argument to the
     *              {@link ChoiceRunnable#run(java.lang.Object)} method.
     * @param action Action to execute when the choice is selected.
     */
    public ButtonChoice(String text, T value, ChoiceRunnable<T> action) {
        this.text = text;
        this.value = value;
        this.action = action;
    }

    /**
     * Returns the text of the choice.
     * @return Text of the choice.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the value that corresponds to this choice.
     * @return Value of the choice.
     */
    public T getValue() {
        return value;
    }

    /**
     * Returns the action that is executed when the choice is selected.
     * @return Action to execute when selected.
     */
    public ChoiceRunnable<T> getAction() {
        return action;
    }


    /**
     * A runnable with an argument.
     * @param <T> Type of the argument passed to {@link #run(java.lang.Object)}.
     */
    public static interface ChoiceRunnable<T> {
        /**
         * Runs this action.
         * @param value The argument.
         */
        void run(T value);
    }
}
