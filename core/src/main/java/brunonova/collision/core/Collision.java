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
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main class of the game.
 */
public class Collision extends Game {
	private SpriteBatch batch;
    private AssetManager assetManager;
    private GameScreen gameScreen;

	@Override
	public void create() {
		batch = new SpriteBatch();
        loadAssets();
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
	}

	@Override
	public void dispose() {
        super.dispose();
        assetManager.dispose();
		batch.dispose();
	}

    /**
     * Loads all game assets.
     */
    private void loadAssets() {
        assetManager = new AssetManager();

        // Load images
        assetManager.load(RES_PATH + "/images/player.png", Texture.class);

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

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
