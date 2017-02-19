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
import com.badlogic.gdx.graphics.g2d.Batch;


/**
 * The player ball.
 */
public class Player extends BaseActor {
    /**
     * Creates the player.
     * @param game The game.
     */
    public Player(Collision game) {
        super(game, "player.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(image, 0, 0, 32, 32);
    }
}
