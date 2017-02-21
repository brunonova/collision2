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
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * An actor that draws a sprite
 */
public abstract class SpriteActor extends BaseActor {
    protected Sprite sprite;

    /**
     * Creates the actor.
     * @param game The game.
     * @param imageName File name of the image of the actor.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public SpriteActor(Collision game, String imageName) {
        super(game);
        sprite = new Sprite(game.getImage(imageName));
        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());
    }

    /**
     * {@inheritDoc}
     * By default it draws the sprite at the position of the actor.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.draw(batch, parentAlpha * getColor().a);
    }

    /**
     * {@inheritDoc}
     * By default it updates the position of the sprite to the same position of
     * the actor.
     */
    @Override
    protected void positionChanged() {
        super.positionChanged();
        sprite.setPosition(getX(), getY());
    }

    /**
     * Returns the sprite of the actor.
     * @return The sprite of the actor.
     */
    public Sprite getSprite() {
        return sprite;
    }
}
