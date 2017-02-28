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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 * The player ball.
 */
public class Player extends Ball {
    /** The speed of the player ball when using the keyboard (pixels/second). */
    public static final float KEYBOARD_SPEED = 400;

    /** Sprite used when the player is in the normal state. */
    private final Sprite normalSprite;
    /** Sprite used when the player is frozen. */
    private final Sprite frozenSprite;
    private boolean enabled = true;
    private boolean frozen = false;

    /**
     * Creates the player.
     * @param game The game.
     */
    public Player(Collision game) {
        super(game, "player.png");
        centerOnScreen();
        normalSprite = sprite;

        // Set the "frozen" sprite
        frozenSprite = new Sprite(game.getImage("player_frozen.png"));
        frozenSprite.setSize(sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(isEnabled() && !isFrozen()) {
            // Determine how many pixels to move the player by this frame
            float dx = 0, dy = 0;

            // Keyboard movement
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= KEYBOARD_SPEED * delta;
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += KEYBOARD_SPEED * delta;
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= KEYBOARD_SPEED * delta;
            if(Gdx.input.isKeyPressed(Input.Keys.UP)) dy += KEYBOARD_SPEED * delta;

            // Mouse movement
            // TODO: impose speed limit to avoid "teleporting"
            dx += Gdx.input.getDeltaX();
            dy -= Gdx.input.getDeltaY();

            // Move the player
            if(dx != 0 || dy != 0) {
                moveBy(dx, dy);
                keepInsideWindow();  // ensure the player is inside of the window
            }
        }
    }

    /**
     * Returns whether the player overlaps (collides with) the specified coin.
     * @param coin The coin.
     * @return {@code true} if the player and coin overlap.
     */
    public boolean overlaps(Coin coin) {
        return boundingCircle.overlaps(coin.boundingCircle);
    }

    /**
     * Freezes the player in place.
     */
    public void freeze() {
        if(!isFrozen()) {
            frozen = true;
            sprite = frozenSprite;
            positionChanged();  // ensure the sprite is drawn at the right position
        }
    }

    /**
     * Unfreezes the player.
     */
    public void unfreeze() {
        if(isFrozen()) {
            frozen = false;
            sprite = normalSprite;
            positionChanged();  // ensure the sprite is drawn at the right position
        }
    }

    /**
     * Returns whether the player is currently frozen.
     * @return {@code true} if the player is frozen.
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Returns whether the ball is enabled and responding to user input.
     * @return {@code true} if the ball is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * whether the ball is enabled and responding to user input.
     * @param enabled {@code true} to enable the ball, {@code false} to disable
     *                it.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}