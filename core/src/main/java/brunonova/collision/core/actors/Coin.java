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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

/**
 * A coin used in the "Coins" mode.
 */
public class Coin extends AnimatedActor {
    /** The minimum distance to the player ball when created. */
    public static final float MINIMUM_DISTANCE_TO_PLAYER = 200;

    /** The bounding circle for collision detection, updated automatically. */
    protected Circle boundingCircle;
    private boolean enabled = false;

    /**
     * Creates this coin.
     * @param game The game.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Coin(Collision game) {
        super(game, "coin.png", 1, 61, 0.02f);
        boundingCircle = new Circle(0, 0, getRadius());
        setRandomPositionFarFromPlayer(MINIMUM_DISTANCE_TO_PLAYER);

        // Set ball transparent, fade-in during 1 second then enable the coin
        getColor().a = 0;
        addAction(Actions.sequence(Actions.fadeIn(1), Actions.run(this::enable)));
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        updateBoundingCircle();
    }

    /**
     * Updates the position of the bounding circle.
     */
    protected final void updateBoundingCircle() {
        boundingCircle.setPosition(getX(Align.center), getY(Align.center));
    }

    /**
     * Enables the coin.
     */
    private void enable() {
        enabled = true;
    }

    /**
     * Returns whether the coin is enabled and sensible to collisions.
     * @return {@code true} if the coin is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns the radius of the coin.
     * @return The radius of the coin.
     */
    public final float getRadius() {
        return getWidth() / 2;
    }
}
