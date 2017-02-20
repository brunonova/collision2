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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;


/**
 * Base class for all game actors.
 */
public abstract class BaseActor extends Actor {
    /** The game. */
    protected Collision game;

    /**
     * Creates the actor.
     * @param game The game.
     */
    public BaseActor(Collision game) {
        this.game = game;
    }

    /**
     * Ensures the actor is inside of the game window, moving it if necessary.
     */
    public final void keepInsideWindow() {
        if(getX() < 0) setX(0);
        if(getRight() > game.getWidth()) setX(game.getWidth() - getWidth());
        if(getY() < 0) setY(0);
        if(getTop() > game.getHeight()) setY(game.getHeight()- getHeight());
    }

    /**
     * Centers the actor on the game window.
     */
    public final void centerOnScreen() {
        setPosition(game.getWidth() / 2, game.getHeight() / 2, Align.center);
    }

    /**
     * Positions the actor in a random position on the screen.
     */
    public final void setRandomPosition() {
        float x = MathUtils.random(0, game.getWidth() - getWidth());
        float y = MathUtils.random(0, game.getHeight() - getHeight());
        setPosition(x, y);
    }

    /**
     * Positions the actor in a random position on the screen at a minimum
     * distance from the specified point.
     * @param minDistance The minimum distance from the specified point.
     * @param x X coordinate of the point.
     * @param y Y coordinate of the point.
     */
    public final void setRandomPositionFarFromPoint(float minDistance, float x, float y) {
        do {
            setRandomPosition();
        } while(Vector2.dst(getX(), getY(), x, y) < minDistance);
    }

    /**
     * Positions the actor in a random position on the screen at a minimum
     * distance from the player ball, if found.
     * @param minDistance The minimum distance from the player.
     */
    public final void setRandomPositionFarFromPlayer(float minDistance) {
        if(game.getGameScreen() != null && game.getGameScreen().getPlayer() != null) {
            setRandomPositionFarFromPoint(minDistance,
                    game.getGameScreen().getPlayer().getX(),
                    game.getGameScreen().getPlayer().getY());
        } else {
            setRandomPosition();
        }
    }
}
