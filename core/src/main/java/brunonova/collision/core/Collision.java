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

import brunonova.collision.core.enums.Difficulty;
import brunonova.collision.core.screens.GameScreen;
import static brunonova.collision.core.Constants.RES_PATH;
import brunonova.collision.core.enums.GameMode;
import brunonova.collision.core.screens.PauseScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.I18NBundle;


/**
 * Main class of the game.
 */
public class Collision extends Game {
	private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private AssetManager assetManager;
    private I18NBundle i18n;
    private final int width;
    private final int height;

    // Options
    private Difficulty difficulty;
    private GameMode gameMode = GameMode.COINS;  // TODO: parameterize this
    private boolean showFPS = true;  // TODO: parameterize this

    // Screens
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;

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

        setDifficulty(Difficulty.MEDIUM);

        startGame();
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
        if(assetManager != null) assetManager.dispose();
        if(shapeRenderer != null) shapeRenderer.dispose();
        if(batch != null) batch.dispose();
	}

    /**
     * Loads all game assets, blocking while doing so.
     */
    private void loadAssets() {
        assetManager = new AssetManager();

        // Load i18n bundle
        i18n = I18NBundle.createBundle(Gdx.files.internal(RES_PATH + "/i18n/Messages"));

        // Load images
        assetManager.load(RES_PATH + "/images/player.png", Texture.class);
        assetManager.load(RES_PATH + "/images/enemy.png", Texture.class);
        assetManager.load(RES_PATH + "/images/coin.png", Texture.class);

        // Load fonts
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        loadFont("font-hud.ttf", "Ubuntu-M.ttf", 22);
        loadFont("font-pause.ttf", "Ubuntu-B.ttf", 96);

        // Block to load all assets synchronously
        assetManager.finishLoading();
    }

    /**
     * Loads the font with the specified properties.
     * @param name The name to be used to retrieve the font from the asset
     *             manager (it should include ".ttf").
     * @param fileName The name of the file to load in the "fonts" folder.
     * @param size The size of the font.
     */
    private void loadFont(String name, String fileName, int size) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = RES_PATH + "/fonts/" + fileName;
        font.fontParameters.size = size;
        font.fontParameters.hinting = FreeTypeFontGenerator.Hinting.AutoFull;
        assetManager.load(name, BitmapFont.class, font);
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
     * Returns the font with the specified name.
     * @param fontName Name of the font.
     * @return The font.
     */
    public BitmapFont getFont(String fontName) {
        return assetManager.get(fontName, BitmapFont.class);
    }

    /**
     * Translates the given key to the current language.
     * @param key Key of the string to translate.
     * @param args Arguments of the key.
     * @return Translated string.
     */
    public String t(String key, Object... args) {
        if(args.length == 0) {
            return i18n.get(key);
        } else {
            return i18n.format(key, args);
        }
    }

    /**
     * Starts the game by switching to the game screen.
     */
    public void startGame() {
        if(gameScreen == null) gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    /**
     * Pauses the game by switching to the "PAUSE" screen.
     */
    public void pauseGame() {
        if(pauseScreen == null) pauseScreen = new PauseScreen(this);
        pauseScreen.setPreviousScreen(getScreen());
        setScreen(pauseScreen);
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

    /**
     * Returns the current difficulty level.
     * @return Current difficulty level.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the current difficulty level.
     * @param difficulty The new difficulty level.
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns the currently selected game mode.
     * @return Current game mode.
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Sets the current game mode.
     * @param gameMode The new game mode.
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Returns whether the FPS should be displayed in the game screen.
     * @return {@code true} to display the game screen.
     */
    public boolean isShowFPS() {
        return showFPS;
    }

    /**
     * Sets whether the FPS should be displayed in the game screen.
     * @param showFPS {@code true} to display the game screen.
     */
    public void setShowFPS(boolean showFPS) {
        this.showFPS = showFPS;
    }

    /**
     * Returns the actual game screen.
     * @return The game screen.
     */
    public GameScreen getGameScreen() {
        return gameScreen;
    }
}
