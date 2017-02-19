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
package brunonova.collision.core;

import brunonova.collision.core.screens.GameScreen;
import static brunonova.collision.core.Constants.RES_PATH;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * Main class of the game.
 */
public class Collision extends Game {
	private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private AssetManager assetManager;
    private GameScreen gameScreen;
    private final int width;
    private final int height;

    /**
     * Creates the game.
     * @param width Width of the game area.
     * @param height Height of the game area.
     */
    public Collision(int width, int height) {
        this.width = width;
        this.height = height;
    }

	@Override
	public void create() {
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        loadAssets();
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
	}

    @Override
    public void render() {
        super.render();

        // Enable or disable full-screen mode when pressing F11
        if(Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if(Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(getWidth(), getHeight());
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
    }

	@Override
	public void dispose() {
        super.dispose();
        assetManager.dispose();
        shapeRenderer.dispose();
		batch.dispose();
	}

    /**
     * Loads all game assets, blocking while doing so.
     */
    private void loadAssets() {
        assetManager = new AssetManager();

        // Load images
        assetManager.load(RES_PATH + "/images/player.png", Texture.class);
        assetManager.load(RES_PATH + "/images/enemy.png", Texture.class);

        // Block to load all assets synchronously
        assetManager.finishLoading();
    }

    /**
     * Returns the image with the specified file name.
     * @param fileName Name of the file.
     * @return The image.
     */
    public Texture getImage(String fileName) {
        return assetManager.get(RES_PATH + "/images/" + fileName, Texture.class);
    }

    /**
     * Returns the game's sprite batch.
     * @return The sprite batch.
     */
    public SpriteBatch getBatch() {
        return batch;
    }

    /**
     * Returns the game's shape renderer.
     * @return The shape renderer.
     */
    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    /**
     * Returns the game's asset manager.
     * @return The asset manager.
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * Returns the width of the game area (original window width).
     * @return Width of the game area.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the game area (original window height).
     * @return Height of the game area.
     */
    public int getHeight() {
        return height;
    }
}
