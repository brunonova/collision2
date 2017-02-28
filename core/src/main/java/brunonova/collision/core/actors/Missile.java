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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * A homing missile that tries to hit the player.
 */
public class Missile extends Ball {
    /** The minimum distance to the player ball when created. */
    public static final float MINIMUM_DISTANCE_TO_PLAYER = 200;

    /** The bounding circle for collision detection, updated automatically. */
    private boolean enabled = false;

    /**
     * Creates the missile.
     * @param game The game.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Missile(Collision game) {
        super(game, "missile.png");
        sprite.setOriginCenter();
        getColor().a = 0;  // make invisible
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Rotate the missile in the direction of the player
        float angleToPlayer = getAngleToPlayer();
        sprite.setRotation(angleToPlayer);

        if(isEnabled() && game.getGameScreen().getPlayer().isEnabled()) {
            // Move the missile towards the player
            float speed = game.getDifficulty().getMissileSpeed();
            float speedX = MathUtils.cosDeg(angleToPlayer) * speed * delta;
            float speedY = MathUtils.sinDeg(angleToPlayer) * speed * delta;
            setPosition(getX() + speedX, getY() + speedY);
        }
    }

    /**
     * Shows the missile and enables it.
     */
    public void show() {
        if(!isEnabled()) {
            setRandomPositionFarFromPlayer(MINIMUM_DISTANCE_TO_PLAYER);

            // Rotate the missile in the direction of the player
            sprite.setRotation(getAngleToPlayer());

            // Fade-in during half a second, then enable the missile
            clearActions();
            addAction(Actions.sequence(
                    Actions.fadeIn(0.5f),
                    Actions.run(() -> {
                        enabled = true;
                    })));
        }
    }

    /**
     * Hides the missile and disables it.
     */
    public void hide() {
        if(isEnabled()) {
            enabled = false;
            clearActions();
            getColor().a = 0;  // make invisible
        }
    }

    /**
     * Returns whether the missile is enabled.
     * @return {@code true} if the missile is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns the angle (in degrees) between the missile and the player.
     * @return Angle between the missile and the player, in degrees.
     */
    private float getAngleToPlayer() {
        Player player = game.getGameScreen().getPlayer();
        return new Vector2(player.getX() - getX(), player.getY() - getY()).angle();
    }
}
