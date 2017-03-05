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
package brunonova.collision.core.screens;

import brunonova.collision.core.Collision;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

/**
 * The "Loading..." screen shown before the menu screen.
 */
public class LoadingScreen extends BaseScreen {
    private final float progressX, progressY, progressWidth, progressHeight;
    private BitmapFont loadingFont;

    /**
     * Creates the screen.
     * @param game The game.
     */
    public LoadingScreen(Collision game) {
        super(game);
        backgroundColor = Color.BLACK;

        // Determine progress bar position and size
        progressX = 100;
        progressY = 100;
        progressWidth = game.getWidth() - (progressX * 2);
        progressHeight = 20;
    }

    @Override
    public void create() {
        super.create();

        // Prepares the "Loading..." font
        loadingFont = game.getFont("font-title.ttf");
        loadingFont.setColor(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // Keep loading the assets
        boolean finished = game.getAssetManager().update(100);
        float progress = game.getAssetManager().getProgress();

        // Draw the "Loading..." text
        batch.begin();
        loadingFont.draw(batch, game.t("loading"), 0, game.getHeight() - 100, game.getWidth(), Align.center, false);
        batch.end();

        // Draw the progress bar's current progress
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(progressX, progressY, progressWidth * progress, progressHeight);
        shapeRenderer.end();

        // Draw the progress bar's outline
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(progressX, progressY, progressWidth, progressHeight);
        shapeRenderer.end();

        // If finished loading, go to the menu scree
        if(finished) {
            game.finishedLoadingAssets();
        }
    }
}
