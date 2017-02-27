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
package brunonova.collision.core.actors;

import brunonova.collision.core.Collision;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * A bonus.
 */
public class Bonus extends Ball {
    /** The minimum distance to the player ball when created. */
    public static final float MINIMUM_DISTANCE_TO_PLAYER = 200;

    private boolean enabled = false;

    /**
     * Create the bonus.
     * @param game The game-
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Bonus(Collision game) {
        super(game, "bonus.png");
        getColor().a = 0;  // make invisible
    }

    /**
     * Show the bonus, enables it and moves it to a random position.
     */
    public void show() {
        if(!enabled) {
            setRandomPositionFarFromPlayer(MINIMUM_DISTANCE_TO_PLAYER);
            enabled = true;

            // Turn the bonus visible over half a second
            getColor().a = 0;
            addAction(Actions.fadeIn(0.5f));
        }
    }

    /**
     * Hides the bonus and disables it.
     */
    public void hide() {
        enabled = false;
        getColor().a = 0;
        clearActions();
    }

    /**
     * Returns whether the bonus is enabled, visible and catchable.
     * @return {@code true} if the bonus is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }
}
