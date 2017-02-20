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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Align;

/**
 * An actor with a sprite that contains a bounding circle that can be used for
 * collision detection.
 */
public abstract class Ball extends SpriteActor {
    /** The bounding circle for collision detection, updated automatically. */
    protected Circle boundingCircle;

    /**
     * Creates the ball.
     * @param game The game.
     * @param imageName File name of the image of the actor.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Ball(Collision game, String imageName) {
        super(game, imageName);
        boundingCircle = new Circle(0, 0, getWidth() / 2);
        updateBoundingCircle();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        updateBoundingCircle();
    }

    /**
     * Returns whether this ball overlaps (collides with) the other.
     * @param other The other ball.
     * @return {@code true} if the balls overlap.
     */
    public boolean overlaps(Ball other) {
        return boundingCircle.overlaps(other.boundingCircle);
    }

    /**
     * Updates the position of the bounding circle.
     */
    protected void updateBoundingCircle() {
        boundingCircle.setPosition(getX(Align.center), getY(Align.center));
    }
}
