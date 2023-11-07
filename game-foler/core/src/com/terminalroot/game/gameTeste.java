package com.terminalroot.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;

public class gameTeste extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, tNave, tBullet, tEnemy;
	private Sprite nave, bullet;
	private float posX, posY, velocity, xBullet, yBullet;
	private boolean shoot, gameOver;
	private Array<Rectangle> enemies;
	private long lastEnemyTime;
	private int score, life, numEnemies;

	private FreeTypeFontGenerator generator;
	private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	private BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("bg.jpg");
		tNave = new Texture("spaceship.png");
		tBullet = new Texture("bullet.png");
		nave = new Sprite(tNave);
		bullet = new Sprite(tBullet);

		posX = 0;
		posY = 0;
		velocity = 14;
		xBullet = posX;
		yBullet = posY;
		shoot = false;

		tEnemy = new Texture("enemy.png");
		enemies = new Array<Rectangle>();
		lastEnemyTime = 0;
		score = 0;
		life = 3;
		gameOver = false;
		numEnemies = 999999999;

		generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 30;
		parameter.color = Color.WHITE;
		parameter.borderWidth = 1;
		parameter.borderColor = Color.PURPLE;
		
		font = generator.generateFont(parameter);
	}

	@Override
	public void render () {
		this.moveNave();
		this.moveBullet();
		this.moveEnemies();

		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		if(!gameOver){
			if(shoot) batch.draw(bullet, xBullet + nave.getWidth() / 2, yBullet + nave.getHeight() / 2 - 25);
			batch.draw(nave, posX, posY);
			for(Rectangle enemy: enemies){
				batch.draw(tEnemy, enemy.x, enemy.y);
			}
			font.draw(batch, "Score: " + score, 10, Gdx.graphics.getHeight() - 10);
			font.draw(batch, "Life: " + life, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 10);
		} else {
			font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2);	
			font.draw(batch, "Press R to restart", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 50);

			if(Gdx.input.isKeyPressed(Input.Keys.R)){
				gameOver = false;
				score = 0;
				life = 3;
				posX = 0;
				posY = 0;
				enemies.clear();
			}
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		tNave.dispose();
		tBullet.dispose();
	}

	private void moveNave(){
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			if(posX > 0) posX -= velocity;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			if(posX < Gdx.graphics.getWidth() - (nave.getWidth())) posX += velocity;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			if(posY < Gdx.graphics.getHeight() - (nave.getHeight())) posY += velocity;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			if(posY > 0) posY -= velocity;
		}
	}
	private void moveBullet(){
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !shoot){
			shoot = true;
			yBullet = posY + nave.getHeight() / 2 - 25;
		}

		if(shoot) {
			if(xBullet < Gdx.graphics.getWidth()) {
				xBullet += velocity * 4;
			} else {
				shoot = false;
				xBullet = posX + nave.getWidth() / 2;
			}

		} else {
			xBullet = posX + nave.getWidth() / 2;
			yBullet = posY + nave.getHeight() / 2 - 25;
		}
	}

	private void spawnEnemies(){
		Rectangle enemy = new Rectangle(Gdx.graphics.getWidth(), MathUtils.random(0, Gdx.graphics.getHeight() - tEnemy.getHeight()), tEnemy.getWidth(), tEnemy.getHeight());
		enemies.add(enemy);
		lastEnemyTime = TimeUtils.nanoTime();
	}

	private void moveEnemies(){
		if(TimeUtils.nanoTime() - lastEnemyTime > numEnemies) spawnEnemies();

		for(Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext();){
			Rectangle enemy = iter.next();
			enemy.x -= 200 * Gdx.graphics.getDeltaTime();

			if(checkCollision(enemy.x, enemy.y, enemy.width, enemy.height, xBullet, yBullet, bullet.getWidth(), bullet.getHeight()) && shoot){
				++score;
				if(score % 10 == 0) numEnemies -= 100;
				shoot = false;
				iter.remove();
			} else if(checkCollision(enemy.x, enemy.y, enemy.width, enemy.height, posX, posY, nave.getWidth(), nave.getHeight()) && !gameOver){
				--life;
				if(life == 0) gameOver = true;
				iter.remove();
			}

			if(enemy.x + tEnemy.getWidth() < 0) iter.remove();
		}
	}

	private boolean checkCollision(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2){
		if(x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2){
			return true;
		}
		return false;
	}
		
}
