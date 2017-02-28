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

/**
 * Enumeration of the difficulty levels and their properties.
 */
public enum Difficulty {
    EASY(200, 15, 10, 200),
    MEDIUM(300, 15, 10, 200),
    HARD(400, 15, 10, 200);

    private final float enemySpeed;
    private final float newEnemyInterval;
    private final float newEnemyCoins;
    private final float missileSpeed;

    private Difficulty(float enemySpeed, float newEnemyInterval, float newEnemyCoins, float missileSpeed) {
        this.enemySpeed = enemySpeed;
        this.newEnemyInterval = newEnemyInterval;
        this.newEnemyCoins = newEnemyCoins;
        this.missileSpeed = missileSpeed;
    }

    /**
     * Returns the initial speed of the enemy balls for this difficulty level.
     * @return Initial speed of enemy balls.
     */
    public float getEnemySpeed() {
        return enemySpeed;
    }

    /**
     * Returns the number of seconds between enemy ball additions in the "Time"
     * mode for this difficulty level.
     * @return Number of seconds between enemy ball additions.
     */
    public float getNewEnemyInterval() {
        return newEnemyInterval;
    }

    /**
     * Returns the number of coins needed to collect for a new enemy ball to
     * appear in the "Coins" mode for this difficulty level.
     * @return Number of coins for a new enemy ball to be added.
     */
    public float getNewEnemyCoins() {
        return newEnemyCoins;
    }

    /**
     * Returns the speed of missiles for this difficulty level.
     * @return The speed of missiles.
     */
    public float getMissileSpeed() {
        return missileSpeed;
    }
}
