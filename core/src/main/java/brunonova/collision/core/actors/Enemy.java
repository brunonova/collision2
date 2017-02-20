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
import com.badlogic.gdx.math.MathUtils;

/**
 * An enemy ball.
 */
public class Enemy extends SpriteActor {
    /** The minimum distance to the player ball when created. */
    public static final float MINIMUM_DISTANCE_TO_PLAYER = 100;
    /** Initial speed of the enemy balls on the easy mode. */
    public static final float SPEED_EASY = 200;
    /** Initial speed of the enemy balls on the medium mode. */
    public static final float SPEED_MEDIUM = 300;
    /** Initial speed of the enemy balls on the hard mode. */
    public static final float SPEED_HARD = 400;

    private float speedX, speedY;

    /**
     * Creates this enemy ball.
     * @param game The game.
     */
    public Enemy(Collision game) {
        super(game, "enemy.png");
        setRandomPositionFarFromPlayer(MINIMUM_DISTANCE_TO_PLAYER);

        // Choose the initial direction and speed
        float angle = MathUtils.random(MathUtils.PI);
        speedX = MathUtils.cos(angle) * SPEED_EASY;
        speedY = MathUtils.sin(angle) * SPEED_EASY;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Move the ball
        moveBy(speedX * delta, speedY * delta);

        // Bounce the ball off the borders of the window
        if(getX() < 0 || getRight() > game.getWidth()) speedX = -speedX;
        if(getY() < 0 || getTop() > game.getHeight()) speedY = -speedY;

        // Ensure the ball is inside the window
        keepInsideWindow();
    }
}
