package com.terminalroot.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class gameTeste extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, tNave;
	private Sprite nave;
	private float posX, posY;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("bg.jpg");
		tNave = new Texture("spaceship.png");
		nave = new Sprite(tNave);
		posX = 0;
		posY = 0;
	}

	@Override
	public void render () {
		this.moveNave();

		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(nave, posX, posY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		tNave.dispose();
	}

	private void moveNave(){
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			if(posX > 0) posX -= 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			if(posX < 1280 - 140) posX += 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			if(posY < 1280 - 140) posY += 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			if(posY > 0) posY -= 200 * Gdx.graphics.getDeltaTime();
		}
	}
}
