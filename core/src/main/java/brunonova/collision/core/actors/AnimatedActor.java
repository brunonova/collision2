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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An actor that draws an animated image.
 */
public abstract class AnimatedActor extends BaseActor {
    /** The animation. */
    protected Animation<TextureRegion> animation;
    /** Time (in seconds) since the actor was created. */
    protected float time = 0;

    /**
     * Creates the actor.
     * @param game The game.
     * @param imageName File name of the sprite sheet of the actor.
     * @param numRows The number of rows in the sprite sheet.
     * @param numCols The number of columns in the sprite sheet.
     * @param frameDuration The number of seconds that each frame of the
     *                      animation is displayed.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public AnimatedActor(Collision game, String imageName, int numRows, int numCols, float frameDuration) {
        super(game);

        // Get the image and split it into a region matrix
        Texture image = game.getImage(imageName);
        setWidth(image.getWidth() / numCols);
        setHeight(image.getHeight() / numRows);
        TextureRegion[][] frameMatrix = TextureRegion.split(image, (int) getWidth(), (int) getHeight());

        // Transform the region matrix into an array
        TextureRegion[] frames = new TextureRegion[numRows * numCols];
        int index = 0;
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numCols; c++) {
                frames[index++] = frameMatrix[r][c];
            }
        }

        // Create the animation
        animation = new Animation<>(frameDuration, frames);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
    }

    /**
     * {@inheritDoc}
     * By default it draws the animation at the position of the actor.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // Draw the current frame of the animation
        Color oldColor = batch.getColor();
        Color newColor = getColor().cpy();
        newColor.a *= parentAlpha;
        batch.setColor(newColor);
        batch.draw(animation.getKeyFrame(time, true), getX(), getY());
        batch.setColor(oldColor);
    }
}
