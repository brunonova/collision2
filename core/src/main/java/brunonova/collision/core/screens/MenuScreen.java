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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * The main menu.
 */
public class MenuScreen extends BaseScreen {
    /**
     * Creates the screen.
     * @param game The game.
     */
    public MenuScreen(Collision game) {
        super(game);
    }

    @Override
    public void create() {
        super.create();

        // Create the styles
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.getFont("font-title.ttf"), Color.BLACK);
        Label.LabelStyle versionStyle = new Label.LabelStyle(game.getFont("font-hud.ttf"), Color.BLACK);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getFont("font-menu.ttf");
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.overFontColor = Color.FOREST;
        buttonStyle.downFontColor = Color.GREEN;

        // Create the menu
        Table menu = new Table();
        menu.setFillParent(true);
        addActor(menu);

        Label title = new Label(game.t("game.title"), titleStyle);
        menu.add(title).spaceBottom(100);
        menu.row();

        TextButton playButton = new TextButton(game.t("menu.play"), buttonStyle);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                onPlay();
            }
        });
        menu.add(playButton);
        menu.row();

        TextButton quitButton = new TextButton(game.t("menu.quit"), buttonStyle);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                onQuit();
            }
        });
        menu.add(quitButton);

        // Add the version of the game to the bottom left corner
        Label version = new Label(game.t("menu.version", Constants.getVersion()), versionStyle);
        version.setPosition(10, 10);
        addActor(version);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            onQuit();
        }
    }

    private void onPlay() {
        game.startGame();
    }

    private void onQuit() {
        Gdx.app.exit();
    }
}
