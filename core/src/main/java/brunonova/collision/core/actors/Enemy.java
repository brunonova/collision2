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
import com.badlogic.gdx.math.Vector2;

/**
 * An enemy ball.
 */
public class Enemy extends Ball {
    /** The minimum distance to the player ball when created. */
    public static final float MINIMUM_DISTANCE_TO_PLAYER = 100;
    /** Mass of the ball (used when balls collide). */
    public static final float MASS = 1;
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

    /**
     * Bounces the two balls off one another after a collision.
     * @param b1 The first ball.
     * @param b2 The second ball.
     */
    public static void bounceBalls(Enemy b1, Enemy b2) {
        // Warning: LibGDX Vector2's operations modify the vector!
        Vector2 pos1 = new Vector2(b1.getX(), b1.getY());
        Vector2 pos2 = new Vector2(b2.getX(), b2.getY());
        Vector2 delta = pos1.cpy().sub(pos2);
        float dist = delta.len();

        // Prevent a possible division by zero
        if(dist == 0) {
            dist = b1.getRadius() + b2.getRadius() - 1;
            delta = new Vector2(b1.getRadius() + b2.getRadius(), 0);
        }

        // Minimum Translation Distance to push balls apart after the collision
        Vector2 mtd = delta.cpy().scl((b1.getRadius() + b2.getRadius() - dist) / dist);

        // Inverse mass quantities
        float im1 = 1 / Enemy.MASS;
        float im2 = 1 / Enemy.MASS;

        // Push-pull them apart
        pos1.add(mtd.cpy().scl(im1 / (im1 + im2)));
        pos2.sub(mtd.cpy().scl(im2 / (im1 + im2)));
        b1.setPosition(pos1.x, pos1.y);
        b2.setPosition(pos2.x, pos2.y);

        // Ensure the balls are still inside the window
        b1.keepInsideWindow();
        b2.keepInsideWindow();

        // Impact speed
        Vector2 speed1 = new Vector2(b1.speedX, b1.speedY);
        Vector2 speed2 = new Vector2(b2.speedX, b2.speedY);
        Vector2 v = speed1.cpy().sub(speed2);
        mtd.nor();  // normalize the vector
        float vn = v.dot(mtd);

        // Sphere intersecting but moving away from each other already
        if(vn > 0) return;

        // Collision impulse
        float restitution = 1;
        float i = (-(1 + restitution) * vn) / (im1 + im2);
        Vector2 impulse = mtd.cpy().scl(i);

        // Change in momentum
        speed1.add(impulse.cpy().scl(im1));
        speed2.sub(impulse.cpy().scl(im2));
        b1.speedX = speed1.x;
        b1.speedY = speed1.y;
        b2.speedX = speed2.x;
        b2.speedY = speed2.y;
    }
}
