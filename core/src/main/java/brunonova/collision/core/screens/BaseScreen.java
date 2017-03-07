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
import brunonova.collision.core.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Base class for all game screens.
 */
public abstract class BaseScreen implements Screen, InputProcessor {
    /** The game. */
    protected final Collision game;
    /** The game's sprite batch. */
    protected final Batch batch;
    /** The game's shape renderer. */
    protected final ShapeRenderer shapeRenderer;
    /** The game's asset manager. */
    protected final AssetManager assetManager;
    /** The input multiplexer. */
    protected final InputMultiplexer inputMultiplexer;
    /** The stage for this screen (created in the {@link #create()} method). */
    protected Stage stage;
    /** The color used to clear the screen (also affects the black bars). */
    protected Color clearColor = new Color(0, 0, 0, 1);
    /** The (optional) background color of the stage (doesn't affect the black bars). */
    protected Color backgroundColor = Constants.BACKGROUND_COLOR;

    /** Whether the "created" method has already been called. */
    private boolean created = false;

    /**
     * Creates the screen.
     * @param game The game.
     */
    public BaseScreen(Collision game) {
        this.game = game;
        this.batch = game.getBatch();
        this.shapeRenderer = game.getShapeRenderer();
        this.assetManager = game.getAssetManager();
        this.inputMultiplexer = new InputMultiplexer();
    }

    /**
     * Creates the screen and the stage.
     * <p>When overriding this method, call {@code super.create()}!</p>
     */
    public void create() {
        stage = new Stage(new FitViewport(game.getWidth(), game.getHeight()), batch);

        // Add the screen and the stage as input processors
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(stage);
    }

    /**
     * {@inheritDoc}
     * The {@link #create()} method is called when this method is first called.
     * <p>When overriding this method, call {@code super.show()}!</p>
     */
    @Override
    public void show() {
        // Call the "create" method if it hasn't been called yet.
        if(!created) {
            create();
            created = true;
        }

        // Set this screen as the input processor, or else the previous screen
        // will still be the input processor
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /**
     * {@inheritDoc}
     * By default it does nothing.
     * <p>When overriding this method, call {@code super.hide()}!</p>
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Updates the state of the game and all actors, and is called by
     * {@link #render(float)}.
     * By default it calls {@link Stage#act(float)} on the stage.
     * <p>When overriding this method, call {@code super.act()}!</p>
     * @param delta The time in seconds since the last render.
     */
    public void act(float delta) {
        stage.act(delta);
    }

    /**
     * {@inheritDoc}
     * By default it clears the screen using {@link #clearColor} as the color,
     * calls {@link #act(float)} and {@link Stage#draw()} on the stage.
     * <p>When overriding this method, call {@code super.render()}!</p>
     */
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background color inside the viewport without touching the
        // black bars (glClear also affects the black bars)
        if(backgroundColor != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(backgroundColor);
            shapeRenderer.rect(0, 0, game.getWidth(), game.getHeight());
            shapeRenderer.end();
        }

        act(delta);
        stage.draw();
    }

    /**
     * Called when the window is resized.
     * By default it updates the viewport.
     * <p>When overriding this method, call {@code super.resize()}!</p>
     * @param width New width of the window.
     * @param height New height of the window.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * {@inheritDoc}
     * By default it disposes the stage.
     * <p>When overriding this method, call {@code super.dispose()}!</p>
     */
    @Override
    public void dispose() {
        if(stage != null) stage.dispose();
        created = false;
    }

    /**
     * {@inheritDoc}
     * By default it does nothing.
     * <p>When overriding this method, call {@code super.pause()}!</p>
     */
    @Override
    public void pause() {

    }

    /**
     * {@inheritDoc}
     * By default it does nothing.
     * <p>When overriding this method, call {@code super.resume()}!</p>
     */
    @Override
    public void resume() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * Shortcut method that adds the specified actor to the stage and returns
     * it.
     * @param <T> Type of the actor.
     * @param actor The actor to add.
     * @return The added actor.
     */
    public final <T extends Actor> T addActor(T actor) {
        stage.addActor(actor);
        return actor;
    }

    /**
     * Returns the viewport of the stage.
     * @return The viewport.
     */
    public Viewport getViewport() {
        return stage.getViewport();
    }
}
