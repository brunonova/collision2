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
import brunonova.collision.core.screens.BaseScreen;
import brunonova.collision.core.screens.GameOverScreen;
import brunonova.collision.core.screens.HighScoresScreen;
import brunonova.collision.core.screens.LoadingScreen;
import brunonova.collision.core.screens.MenuScreen;
import brunonova.collision.core.screens.OptionsScreen;
import brunonova.collision.core.screens.PauseScreen;
import brunonova.collision.core.screens.QuitScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Main class of the game.
 */
public class Collision extends Game {
    private static final String TAG = Collision.class.getName();

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private AssetManager assetManager;
    private I18NBundle i18n;
    private AsyncExecutor asyncExecutor;
    private final int width;
    private final int height;

    // Options
    private Preferences preferences;
    private HighScores highScores;
    private Difficulty difficulty;
    private GameMode gameMode;
    private boolean showFPS;
    private float volume;
    private boolean fullScreen;

    // Screens
    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;
    private OptionsScreen optionsScreen;
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;
    private QuitScreen quitScreen;
    private GameOverScreen gameOverScreen;
    private HighScoresScreen highScoresScreen;

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
        asyncExecutor = new AsyncExecutor(5);

        // Load the user preferences and high scores
        loadPreferences();
        highScores = new HighScores(this);

        // Load the assets
        startLoadingAssets();
    }

    @Override
    public void render() {
        super.render();

        // Enable or disable full screen mode when pressing F11
        if(Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            setFullScreen(!isFullScreen());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if(assetManager != null) assetManager.dispose();
        if(shapeRenderer != null) shapeRenderer.dispose();
        if(batch != null) batch.dispose();
        asyncExecutor.dispose();
    }

    /**
     * Shows the "Loading..." screen and starts loading the game assets.
     */
    private void startLoadingAssets() {
        // Start loading the assets (the assets for the "Loading" screen will be
        // fully loaded when this method returns)
        loadAssets();

        // Translate the window title and show the loading screen, while the
        // rest of the assets load
        Gdx.graphics.setTitle(t("game.title"));
        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    /**
     * Called when the assets have finished loading, to show the menu screen.
     */
    public void finishedLoadingAssets() {
        // Create and show the menu screen
        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
        loadingScreen.dispose();

        // Switch to full screen mode if it's the user preference
        if(isFullScreen()) {
            switchToFullScreen();
        }
    }

    /**
     * Loads all game assets.
     * <p>First, it loads the assets needed by the "Loading" screen
     * synchronously. Then it queues all the remaining assets to be loaded later
     * asynchronously.</p>
     */
    private void loadAssets() {
        assetManager = new AssetManager();

        // Load i18n bundle
        i18n = I18NBundle.createBundle(Gdx.files.internal(RES_PATH + "/i18n/Messages"));

        // Load the font needed by the "Loading" screen
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        loadFont("font-title.ttf", "Ubuntu-B.ttf", 60);

        // Block to load the previous assets synchronously
        assetManager.finishLoading();

        // Load the rest of the fonts
        loadFont("font-hud.ttf", "Ubuntu-M.ttf", 22);
        loadFont("font-pause.ttf", "Ubuntu-B.ttf", 96, Color.BLACK, 5, 5);
        loadFont("font-menu.ttf", "Ubuntu-M.ttf", 32);

        // Load images
        assetManager.load(RES_PATH + "/images/player.png", Texture.class);
        assetManager.load(RES_PATH + "/images/enemy.png", Texture.class);
        assetManager.load(RES_PATH + "/images/coin.png", Texture.class);
        assetManager.load(RES_PATH + "/images/bonus.png", Texture.class);
        assetManager.load(RES_PATH + "/images/player_frozen.png", Texture.class);
        assetManager.load(RES_PATH + "/images/player_invulnerable.png", Texture.class);
        assetManager.load(RES_PATH + "/images/missile.png", Texture.class);

        // Load sounds
        assetManager.load(RES_PATH + "/sounds/coin.mp3", Sound.class);
        assetManager.load(RES_PATH + "/sounds/bonus_good.mp3", Sound.class);
        assetManager.load(RES_PATH + "/sounds/bonus_bad.mp3", Sound.class);
        assetManager.load(RES_PATH + "/sounds/lose.mp3", Sound.class);
        assetManager.load(RES_PATH + "/sounds/high_score.mp3", Sound.class);
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
     * Loads the font with the specified properties.
     * <p>The font will have a shadow.</p>
     * @param name The name to be used to retrieve the font from the asset
     *             manager (it should include ".ttf").
     * @param fileName The name of the file to load in the "fonts" folder.
     * @param size The size of the font.
     * @param shadowColor The color of the shadow.
     * @param shadowOffsetX Offset of the shadow in the X axis.
     * @param shadowOffsetY Offset of the shadow in the Y axis.
     */
    private void loadFont(String name, String fileName, int size, Color shadowColor, int shadowOffsetX, int shadowOffsetY) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = RES_PATH + "/fonts/" + fileName;
        font.fontParameters.size = size;
        font.fontParameters.hinting = FreeTypeFontGenerator.Hinting.AutoFull;
        font.fontParameters.shadowColor = shadowColor;
        font.fontParameters.shadowOffsetX = shadowOffsetX;
        font.fontParameters.shadowOffsetY = shadowOffsetY;
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
     * Returns the sound with the specified file name.
     * @param fileName Name of the file.
     * @return The sound.
     */
    public Sound getSound(String fileName) {
        return assetManager.get(RES_PATH + "/sounds/" + fileName, Sound.class);
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
     * Loads the user preferences.
     */
    public void loadPreferences() {
        try {
            gameMode = GameMode.valueOf(getPreferences().getString("gameMode", "TIME"));
        } catch(IllegalArgumentException ex) {
            Gdx.app.error(TAG, "error loading gameMode preference", ex);
            gameMode = GameMode.TIME;
        }

        try {
            difficulty = Difficulty.valueOf(getPreferences().getString("difficulty", "EASY"));
        } catch(IllegalArgumentException ex) {
            Gdx.app.error(TAG, "error loading difficulty preference", ex);
            difficulty = Difficulty.EASY;
        }

        volume = getPreferences().getFloat("volume", 0f);
        showFPS = getPreferences().getBoolean("showFPS", false);
        fullScreen = getPreferences().getBoolean("fullScreen", false);
    }

    /**
     * Returns the user preferences.
     * @return The user preferences.
     */
    public Preferences getPreferences() {
        if(preferences == null) {
            preferences = Gdx.app.getPreferences("options.xml");
        }
        return preferences;
    }

    /**
     * Saves the user preferences to disk.
     */
    public void savePreferences() {
        // Flush the preferences asynchronously, so avoid any UI freezes
        asyncExecutor.submit(() -> {
            synchronized(Collision.this) {
                getPreferences().flush();
                return true;
            }
        });
    }

    /**
     * Starts the game by switching to the game screen.
     */
    public void startGame() {
        if(gameScreen == null) gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    /**
     * Returns to the main menu.
     */
    public void returnToMenu() {
        setScreen(menuScreen);
    }

    /**
     * Switches to the Options screen.
     */
    public void showOptionsMenu() {
        if(optionsScreen == null) optionsScreen = new OptionsScreen(this);
        setScreen(optionsScreen);
    }

    /**
     * Pauses the game by switching to the "PAUSE" screen.
     * <p>This method should be called at the end of the {@code render()}
     * method, or else the screenshot taken to be used as the background of the
     * pause screen won't be correct.</p>
     * @param screen The screen where the screenshot is taking place.
     */
    public void pauseGame(BaseScreen screen) {
        if(pauseScreen == null) pauseScreen = new PauseScreen(this);
        pauseScreen.setPreviousScreen(getScreen());
        pauseScreen.setBackground(takeScreenshot(screen));
        setScreen(pauseScreen);
    }

    /**
     * Show the screen that asks confirmation from the user to quit the game
     * screen.
     * <p>This method should be called at the end of the {@code render()}
     * method, or else the screenshot taken to be used as the background of the
     * pause screen won't be correct.</p>
     */
    public void confirmQuit() {
        if(quitScreen == null) quitScreen = new QuitScreen(this);
        quitScreen.setBackground(takeScreenshot(getGameScreen()));
        setScreen(quitScreen);
    }

    /**
     * Shows the "game over" screen.
     * @param score The achieved score.
     */
    public void showGameOverScreen(int score) {
        if(gameOverScreen == null) gameOverScreen = new GameOverScreen(this);
        gameOverScreen.setData(gameMode, difficulty, score);
        setScreen(gameOverScreen);
    }

    /**
     * Shows the High Scores screen.
     */
    public void showHighScores() {
        if(highScoresScreen != null)  highScoresScreen.dispose();
        highScoresScreen = new HighScoresScreen(this);
        setScreen(highScoresScreen);
    }

    /**
     * Shows the High Scores screen.
     * @param scoreIndexToHighlight The index of the score to highlight.
     */
    public void showHighScores(int scoreIndexToHighlight) {
        if(highScoresScreen != null)  highScoresScreen.dispose();
        highScoresScreen = new HighScoresScreen(this, scoreIndexToHighlight);
        setScreen(highScoresScreen);
    }

    /**
     * Takes a screenshot of the viewport.
     * <p>This method should be called at the end of the {@code render()}
     * method, or else the screenshot won't be correct.</p>
     * @param screen The screen where the screenshot is taking place.
     * @return The screenshot as a texture.
     */
    public Texture takeScreenshot(BaseScreen screen) {
        // Determine area of the viewport
        Viewport viewport = screen.getViewport();
        int x = viewport.getLeftGutterWidth();
        int y = viewport.getTopGutterHeight();
        int w = Gdx.graphics.getWidth() - viewport.getLeftGutterWidth() - viewport.getRightGutterWidth();
        int h = Gdx.graphics.getHeight() - viewport.getTopGutterHeight() - viewport.getBottomGutterHeight();

        // Take the screenshot
        byte[] pixels = ScreenUtils.getFrameBufferPixels(x, y, w, h, true);

        // Load the screenshot into a Pixmap, and then into a Texture
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        return new Texture(pixmap);
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

        // Save the preference
        getPreferences().putString("difficulty", difficulty.toString());
        savePreferences();
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

        // Save the preference
        getPreferences().putString("gameMode", gameMode.toString());
        savePreferences();
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

        // Save the preference
        getPreferences().putBoolean("showFPS", showFPS);
        savePreferences();
    }

    /**
     * Returns the current volume.
     * @return Current volume level.
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Sets the current volume.
     * @param volume New volume level.
     */
    public void setVolume(float volume) {
        this.volume = volume;

        // Save the preference
        getPreferences().putFloat("volume", volume);
        savePreferences();
    }

    /**
     * Returns whether the game is in full screen mode.
     * @return {@code true} if the game is full screen.
     */
    public boolean isFullScreen() {
        return fullScreen;
    }

    /**
     * Enables or disables full screen mode.
     * @param fullScreen {@code true} to enable full screen mode.
     */
    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;

        // Save the preference
        getPreferences().putBoolean("fullScreen", fullScreen);
        savePreferences();

        // Make the switch to full screen or windowed mode, accordingly
        if(fullScreen) {
            switchToFullScreen();
        } else {
            switchToWindowedMode();
        }
    }

    /**
     * Returns the actual game screen.
     * @return The game screen.
     */
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    /**
     * Returns the high scores.
     * @return The high scores.
     */
    public HighScores getHighScores() {
        return highScores;
    }

    /**
     * Returns the executor for asynchronous operations.
     * @return The asynchronous executor.
     */
    public AsyncExecutor getAsyncExecutor() {
        return asyncExecutor;
    }

    /**
     * Switches the game to full screen mode (without changing preferences).
     */
    private void switchToFullScreen() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }

    /**
     * Switches the game to windowed mode (without changing preferences).
     */
    private void switchToWindowedMode() {
        Gdx.graphics.setWindowedMode(getWidth(), getHeight());
    }
}
