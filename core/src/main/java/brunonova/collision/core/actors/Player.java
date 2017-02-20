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


/**
 * The player ball.
 */
public class Player extends Ball {
    /** The speed of the player ball when using the keyboard (pixels/second). */
    public static final float KEYBOARD_SPEED = 400;

    /**
     * Creates the player.
     * @param game The game.
     */
    public Player(Collision game) {
        super(game, "player.png");
        centerOnScreen();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

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