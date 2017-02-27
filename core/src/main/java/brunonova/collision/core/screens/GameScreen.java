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
import brunonova.collision.core.actors.Coin;
import brunonova.collision.core.actors.Enemy;
import brunonova.collision.core.actors.Player;
import brunonova.collision.core.enums.GameMode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import java.util.LinkedList;
import java.util.List;


/**
 * The screen of the game itself.
 */
public class GameScreen extends BaseScreen {
    /** The player ball. */
    private Player player;
    /** The enemy balls. */
    private List<Enemy> enemies;
    /** The coin for the "Coins" mode. */
    private Coin coin;
    /** Whether the game is ending (before the "Game Over" screen is shown). */
    private boolean gameEnding = false;
    /** Time since the game started. */
    private float time;
    /** The number of collected coins for the "Coins" mode. */
    private int coins;
    /** Time remaining to add a new enemy ball. */
    private float timerNewEnemy;
    /** Font used on the HUD. */
    private BitmapFont hudFont;
    /** Sound played when the player catches a coin. */
    private Sound coinSound;

    /**
     * Creates the screen.
     * @param game The game.
     */
    public GameScreen(Collision game) {
        super(game);
    }

    @Override
    public void create() {
        super.create();

        // Prepare the font for the HUD
        hudFont = game.getFont("font-hud.ttf");
        hudFont.setColor(Color.BLACK);

        // Prepare the sounds
        coinSound = game.getSound("coin.mp3");

        // Add the player
        player = addActor(new Player(game));

        // Add the enemy balls
        enemies = new LinkedList<>();
        for(int i = 0; i < Constants.STARTING_NUMBER_OF_ENEMY_BALLS; i++) {
            addEnemy();
        }

        // Create the coin, if in "Coins" mode
        if(game.getGameMode() == GameMode.COINS) {
            coin = addActor(new Coin(game));
        }

        // Set counters and timers
        gameEnding = false;
        coins = 0;
        time = 0;
        timerNewEnemy = game.getDifficulty().getNewEnemyInterval();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(!gameEnding) {
            // Update timers
            time += delta;
            timerNewEnemy -= delta;

            // Exit if the Escape key is presses
            if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                Gdx.app.exit();
            }

            // Detect collision between player and coin
            if(game.getGameMode() == GameMode.COINS && coin.isEnabled() && player.overlaps(coin)) {
                coins++;
                coin.setRandomPositionFarFromPlayer(Coin.MINIMUM_DISTANCE_TO_PLAYER);
                coinSound.play(game.getVolume());

                // Add a new enemy ball?
                if(coins % game.getDifficulty().getNewEnemyCoins() == 0) {
                    addEnemy();
                }
            }

            // Detect collisions between enemy balls
            for(int i = 0; i < enemies.size() - 1; i++) {
                for(int j = i + 1; j < enemies.size(); j++) {
                    Enemy a = enemies.get(i);
                    Enemy b = enemies.get(j);
                    if(a.isEnabled() && b.isEnabled() && a.overlaps(b)) {
                        Enemy.bounceBalls(a, b);
                    }
                }
            }

            // Detect collision between player and enemy balls
            for(Enemy enemy: enemies) {
                if(enemy.isEnabled() && player.overlaps(enemy)) {
                    gameOver();
                }
            }

            // Time to add another enemy ball?
            if(game.getGameMode() == GameMode.TIME && timerNewEnemy <= 0) {
                timerNewEnemy += game.getDifficulty().getNewEnemyInterval();
                addEnemy();
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // Draw the HUD
        batch.begin();

        // Draw the score (time or coins)
        switch(game.getGameMode()) {
            case TIME:
                hudFont.draw(batch, game.t("hud.time", (int) time), 10, game.getHeight() - 15);
                break;
            case COINS:
                hudFont.draw(batch, game.t("hud.coins", coins), 10, game.getHeight() - 15);
                break;
        }

        // Draw the number of enemy balls (100px width, right aligned)
        hudFont.draw(batch, game.t("hud.balls", enemies.size()), game.getWidth() - 110,
                     game.getHeight() - 15, 100, Align.right, false);

        // Draw the FPS, if enabled
        if(game.isShowFPS()) {
            hudFont.draw(batch, game.t("hud.fps", Gdx.graphics.getFramesPerSecond()), 10, 25);
        }

        batch.end();

        // Pause the game when 'P' is pressed
        // This was put at the end of the "render" method so that the screenshot
        // taken by this method to be used as the background of the "PAUSE"
        // screen is complete.
        if(Gdx.input.isKeyJustPressed(Input.Keys.P) && !gameEnding) {
            game.pauseGame(this);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Adds a new enemy ball.
     */
    private void addEnemy() {
        enemies.add(addActor(new Enemy(game)));
    }

    /**
     * Ends the game.
     */
    private void gameOver() {
        gameEnding = true;

        // Disable the player ball
        player.setEnabled(false);

        // Stop and disable all enemy balls
        for(Enemy enemy: enemies) {
            enemy.disable();
        }

        // Fade-out the player, then end the game
        player.addAction(Actions.sequence(
                Actions.fadeOut(2),
                Actions.run(() -> {
                    // Exit the game
                    Gdx.app.exit();
                })));
    }

    /**
     * Returns the player ball.
     * @return The player ball.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the enemy balls.
     * @return List of enemy balls.
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }
}
