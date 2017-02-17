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
import static brunonova.collision.core.Constants.RES_PATH;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class BaseScreen implements Screen {
    protected final Collision game;
    protected final Batch batch;
    protected final AssetManager assetManager;
    protected final OrthographicCamera camera;

    public BaseScreen(Collision game) {
        this.game = game;
        this.batch = game.getBatch();
        this.assetManager = game.getAssetManager();
        this.camera = game.getCamera();
    }

    /**
     * Returns the image with the specified file name.
     * @param fileName Name of the file.
     * @return The image.
     */
    public Texture getImage(String fileName) {
        return assetManager.get(RES_PATH + "/images/" + fileName, Texture.class);
    }
}
