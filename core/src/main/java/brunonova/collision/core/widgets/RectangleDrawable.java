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
package brunonova.collision.core.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

/**
 * Drawable that draws a rectangle with width >= 1.
 */
public class RectangleDrawable extends BaseDrawable {
    private final ShapeRenderer shapeRenderer;
    private final Color color;

    /**
     * Creates the drawable.
     * @param shapeRenderer The game's shape renderer.
     * @param color The color to use.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public RectangleDrawable(ShapeRenderer shapeRenderer, Color color) {
        this.shapeRenderer = shapeRenderer;
        this.color = color;
        setMinWidth(1);
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.end();

        // Draw the rectangle using a shape renderer
        // TODO: find a way to do this without batch.end()/batch.begin()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(batch.getColor().mul(color));
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();

        batch.begin();
    }
}
