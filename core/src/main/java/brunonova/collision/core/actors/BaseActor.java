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
}
