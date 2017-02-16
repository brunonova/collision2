package brunonova.collision.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Collision extends Game {
	SpriteBatch batch;
	Texture img;

	@Override
	public void create () {
		batch = new SpriteBatch();
        //setScreen(screen);
	}

	@Override
	public void render () {
        //super.render();
		Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
