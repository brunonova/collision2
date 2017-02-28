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
package brunonova.collision.core.enums;

import com.badlogic.gdx.math.MathUtils;

/**
 * Enumeration of the available bonus types, along with the time they last.
 */
public enum BonusType {
    /** Slows down the enemy balls by 50%. */
    SLOW_DOWN_ENEMIES(6),
    /** Speeds up the enemy balls by 50%. */
    SPEED_UP_ENEMIES(3),
    /** Freezes the enemy balls in place. */
    FREEZE_ENEMIES(5),
    /** Freezes the player ball in place. */
    FREEZE_PLAYER(0.6f);

    private final float duration;

    private BonusType(float duration) {
        this.duration = duration;
    }


    /**
     * Chooses a random bonus type from the available options.
     * @return A random bonus type.
     */
    public static BonusType pickRandom() {
        int numberOfTypes = BonusType.values().length;
        return BonusType.values()[MathUtils.random(numberOfTypes - 1)];
    }

    /**
     * Returns the duration of this bonus type.
     * @return The duration (in seconds) of this bonus.
     */
    public float getDuration() {
        return duration;
    }
}
