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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main class of the game.
 */
public class Collision extends Game {
	private SpriteBatch batch;
    private AssetManager assetManager;
    private OrthographicCamera camera;
    private GameScreen gameScreen;

	@Override
	public void create() {
		batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);  // center the camera on the screen
        camera.update();
        loadAssets();
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
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

	@Override
	public void dispose() {
        super.dispose();
        assetManager.dispose();
		batch.dispose();
	}

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
