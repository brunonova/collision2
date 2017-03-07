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
import brunonova.collision.core.actors.Bonus;
import brunonova.collision.core.actors.Coin;
import brunonova.collision.core.actors.Enemy;
import brunonova.collision.core.actors.Missile;
import brunonova.collision.core.actors.Player;
import brunonova.collision.core.enums.BonusType;
import brunonova.collision.core.enums.GameMode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import java.util.LinkedList;
import java.util.List;


/**
 * The screen of the game itself.
 */
public class GameScreen extends BaseScreen {
    // Actors
    /** The player ball. */
    private Player player;
    /** The enemy balls. */
    private List<Enemy> enemies;
    /** The coin for the "Coins" mode. */
    private Coin coin;
    /** The bonus (only 1 bonus is created, and then it's reused). */
    private Bonus bonus;
    /** The missile (only 1 missile is created, and then it's reused). */
    private Missile missile;

    // Resources
    /** Font used on the HUD. */
    private BitmapFont hudFont;
    /** Sound played when the player catches a coin. */
    private Sound coinSound;

    // Control variables
    /** Whether the game is ending (before the "Game Over" screen is shown). */
    private boolean gameEnding = false;
    /** Time since the game started. */
    private float time;
    /** The number of collected coins for the "Coins" mode. */
    private int coins;

    // Timers
    /** Time remaining to add a new enemy ball. */
    private float timerNewEnemy;
    /** Time remaining to add a new bonus. */
    private float timerNewBonus;
    /** Time until the {@link BonusType#SLOW_DOWN_ENEMIES} bonus is over. */
    private float timerSlowDownEnemiesBonus;
    /** Time until the {@link BonusType#SPEED_UP_ENEMIES} bonus is over. */
    private float timerSpeedUpEnemiesBonus;
    /** Time until the {@link BonusType#FREEZE_ENEMIES} bonus is over. */
    private float timerFreezeEnemiesBonus;
    /** Time until the {@link BonusType#FREEZE_PLAYER} bonus is over. */
    private float timerFreezePlayerBonus;
    /** Time until the {@link BonusType#INVULNERABILITY} bonus is over. */
    private float timerInvulnerabilityBonus;
    /** Time until the {@link BonusType#MISSILE} bonus is over. */
    private float timerMissileBonus;

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

        // Add the bonus (disabled and invisible)
        bonus = addActor(new Bonus(game));

        // Add the player
        player = addActor(new Player(game));

        // Add the enemy balls
        enemies = new LinkedList<>();
        for(int i = 0; i < Constants.STARTING_NUMBER_OF_ENEMY_BALLS; i++) {
            addEnemy();
        }

        // Add the missile (disabled and invisible)
        missile = addActor(new Missile(game));

        // Create the coin, if in "Coins" mode
        if(game.getGameMode() == GameMode.COINS) {
            coin = addActor(new Coin(game));
        }

        // Set counters and timers
        gameEnding = false;
        coins = 0;
        time = 0;
        timerNewEnemy = game.getDifficulty().getNewEnemyInterval();
        timerNewBonus = MathUtils.random(Constants.NEW_BONUS_MIN_TIME, Constants.NEW_BONUS_MAX_TIME);
        timerSlowDownEnemiesBonus = 0;
        timerSpeedUpEnemiesBonus = 0;
        timerFreezeEnemiesBonus = 0;
        timerFreezePlayerBonus = 0;
        timerInvulnerabilityBonus = 0;
        timerMissileBonus = 0;
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
            timerNewBonus -= delta;
            timerSlowDownEnemiesBonus -= delta;
            timerSpeedUpEnemiesBonus -= delta;
            timerFreezeEnemiesBonus -= delta;
            timerFreezePlayerBonus -= delta;
            timerInvulnerabilityBonus -= delta;
            timerMissileBonus -= delta;

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

            // Detect collision between player and bonus
            if(bonus.isEnabled() && player.overlaps(bonus)) {
                giveBonus();
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
            if(player.isEnabled() && !player.isInvulnerable()) {
                for(Enemy enemy: enemies) {
                    if(enemy.isEnabled() && player.overlaps(enemy)) {
                        gameOver();
                    }
                }
            }

            if(player.isEnabled() && !player.isInvulnerable()) {
                if(missile.isEnabled() && player.overlaps(missile)) {
                    gameOver();
                }
            }

            // Time to add another enemy ball?
            if(game.getGameMode() == GameMode.TIME && timerNewEnemy <= 0) {
                timerNewEnemy += game.getDifficulty().getNewEnemyInterval();
                addEnemy();
            }

            // Time to show the bonus?
            if(timerNewBonus <= 0 && !bonus.isEnabled()) {
                bonus.show();
            }

            // Time to unfreeze the player?
            if(timerFreezePlayerBonus <= 0 && player.isFrozen() && player.isEnabled()) {
                player.unfreeze();
            }

            // Time to make the player vulnerable again?
            if(timerInvulnerabilityBonus <= 0 && player.isInvulnerable() && player.isEnabled()) {
                player.makeVulnerable();
            }

            // Time to hide the missile?
            if(timerMissileBonus <= 0 && missile.isEnabled()) {
                missile.hide();
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
    }

    @Override
    public boolean keyDown(int keycode) {
        if(!gameEnding) {
            switch(keycode) {
                // Exit if the Escape key is pressed
                case Input.Keys.ESCAPE:
                    game.confirmQuit();
                    return true;

                // Pause the game when 'P' is pressed
                case Input.Keys.P:
                    game.pauseGame(this);
                    return true;
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Returns whether the {@link BonusType#SLOW_DOWN_ENEMIES} is currently
     * active.
     * @return {@code true} if the bonus is active.
     */
    public boolean isSlowDownEnemiesBonusActive() {
        return timerSlowDownEnemiesBonus > 0;
    }

    /**
     * Returns whether the {@link BonusType#SPEED_UP_ENEMIES} is currently
     * active.
     * @return {@code true} if the bonus is active.
     */
    public boolean isSpeedUpEnemiesBonusActive() {
        return timerSpeedUpEnemiesBonus > 0;
    }

    /**
     * Returns whether the {@link BonusType#FREEZE_ENEMIES} is currently
     * active.
     * @return {@code true} if the bonus is active.
     */
    public boolean isFreezeEnemiesBonusActive() {
        return timerFreezeEnemiesBonus > 0;
    }

    /**
     * Returns the factor to multiply enemy balls speed by (based on the
     * currently active bonus).
     * @return The speed factor.
     */
    public float getEnemyBallsSpeedFactor() {
        if(isSlowDownEnemiesBonusActive()) {
            return 0.5f;
        } else if(isSpeedUpEnemiesBonusActive()) {
            return 1.5f;
        } else if(isFreezeEnemiesBonusActive()) {
            return 0f;
        } else {
            return 1f;
        }
    }

    /**
     * Adds a new enemy ball.
     */
    private void addEnemy() {
        enemies.add(addActor(new Enemy(game)));
    }

    /**
     * Gives the player a random bonus (or anti-bonus), and resets the bonus
     * timer.
     */
    private void giveBonus() {
        // Hide the bonus and reset the timer
        bonus.hide();
        timerNewBonus = MathUtils.random(Constants.NEW_BONUS_MIN_TIME, Constants.NEW_BONUS_MAX_TIME);

        // Select the bonus
        BonusType type = BonusType.pickRandom();
        switch(type) {
            case SLOW_DOWN_ENEMIES:
                timerSlowDownEnemiesBonus = type.getDuration();
                timerSpeedUpEnemiesBonus = 0;
                timerFreezeEnemiesBonus = 0;
                break;
            case SPEED_UP_ENEMIES:
                timerSlowDownEnemiesBonus = 0;
                timerSpeedUpEnemiesBonus = type.getDuration();
                timerFreezeEnemiesBonus = 0;
                break;
            case FREEZE_ENEMIES:
                timerSlowDownEnemiesBonus = 0;
                timerSpeedUpEnemiesBonus = 0;
                timerFreezeEnemiesBonus = type.getDuration();
                break;
            case FREEZE_PLAYER:
                timerFreezePlayerBonus = type.getDuration();
                player.freeze();
                break;
            case INVULNERABILITY:
                timerInvulnerabilityBonus = type.getDuration();
                player.makeInvulnerable();
                break;
            case MISSILE:
                timerMissileBonus = type.getDuration();
                missile.show();
                break;
        }
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
            enemy.clearActions();
        }

        // Fade-out the player, then end the game
        player.addAction(Actions.sequence(
                Actions.fadeOut(2),
                Actions.run(() -> {
                    // Exit the game
                    dispose();
                    game.returnToMenu();
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
