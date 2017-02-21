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
import brunonova.collision.core.actors.Enemy;
import brunonova.collision.core.actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    /** Time since the game started. */
    private float time;
    /** Time remaining to add a new enemy ball. */
    private float timerNewEnemy;

    public GameScreen(Collision game) {
        super(game);
    }

    @Override
    public void create() {
        super.create();

        // Add the player
        player = addActor(new Player(game));

        // Add the enemy balls
        enemies = new LinkedList<>();
        for(int i = 0; i < Constants.STARTING_NUMBER_OF_ENEMY_BALLS; i++) {
            enemies.add(addActor(new Enemy(game)));
        }

        // Set timers
        time = 0;
        timerNewEnemy = game.getCurrentDifficulty().getNewEnemyInterval();
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

        // Update timers
        time += delta;
        timerNewEnemy -= delta;

        // Exit if the Escape key is presses
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
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
        if(timerNewEnemy <= 0) {
            timerNewEnemy += game.getCurrentDifficulty().getNewEnemyInterval();
            enemies.add(addActor(new Enemy(game)));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void gameOver() {
        Gdx.app.exit();
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
