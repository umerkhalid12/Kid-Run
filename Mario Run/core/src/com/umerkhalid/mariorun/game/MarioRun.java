package com.umerkhalid.mariorun.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import sun.java2d.windows.GDIBlitLoops;

public class MarioRun extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture DizzyMan;
	int manState = 0;
	int pause=0;
	float gravity = 0.2f;
	float velocity = 0;
	int manY = 0;

	int gameState = 0;
	int score;
	BitmapFont bitmapFont;

    Rectangle manRectanlgles;


	ArrayList<Integer> coinXs = new ArrayList<Integer>();
    ArrayList<Integer> coinYs = new ArrayList<Integer>();
    ArrayList<Rectangle> coinRectanlgles = new ArrayList<Rectangle>();
    Texture coin;
    int coinCount;

    ArrayList<Integer> bombXs = new ArrayList<Integer>();
    ArrayList<Integer> bombYs = new ArrayList<Integer>();
    ArrayList<Rectangle> bombRectanlgles = new ArrayList<Rectangle>();
    Texture bomb;
    int bombCount;

    Random random;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background =new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
        man[1] = new Texture("frame-2.png");
        man[2] = new Texture("frame-3.png");
        man[3] = new Texture("frame-4.png");
        DizzyMan = new Texture("dizzy-1.png");
        manY = Gdx.graphics.getHeight()/2;

        coin = new Texture("coin.png");
        bomb = new Texture("bomb.png");
        random = new Random();
        manRectanlgles = new Rectangle();

        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getData().setScale(10);
	}

	public void makeCoin(){
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        coinYs.add((int)height);
        coinXs.add(Gdx.graphics.getWidth());
    }
    public void makebomb(){
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        bombYs.add((int)height);
        bombXs.add(Gdx.graphics.getWidth());
    }

	@Override
	public void render () {


		batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        if(gameState == 1){
            //Game is live
            //BOMBS
            if(bombCount<250){
                bombCount++;
            }
            else {
                bombCount=0;
                makebomb();

            }
            bombRectanlgles.clear();
            for (int i = 9 ; i<bombXs.size(); i++){
                batch.draw(bomb,bombXs.get(i),bombYs.get(i));
                bombXs.set(i,bombXs.get(i)-8);
                bombRectanlgles.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
            }


            //COINS
            if(coinCount<100){
                coinCount++;
            }
            else {
                coinCount=0;
                makeCoin();

            }
            coinRectanlgles.clear();
            for (int i = 9 ; i<coinXs.size(); i++){
                batch.draw(coin,coinXs.get(i),coinYs.get(i));
                coinXs.set(i,coinXs.get(i)-4);
                coinRectanlgles.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));

            }

            if(Gdx.input.justTouched()){
                velocity = -10;
            }


            if(pause<10) {
                pause++;
            }
            else{
                pause = 0;
                if (manState < 3) {
                    manState++;
                } else {
                    manState = 0;
                }
            }

            velocity += gravity;
            manY -= velocity;

            if(manY <= 0){
                manY=0;
            }


        }
        else if(gameState == 0){
            //waiting to start
            if(Gdx.input.justTouched()){
                gameState=1;
            }
        }
        else if(gameState ==2){
            //gameover
            if(Gdx.input.justTouched()){
                gameState=1;
                manY = Gdx.graphics.getHeight()/2;
                score =0;
                velocity = 0;
                coinYs.clear();
                coinXs.clear();
                coinRectanlgles.clear();
                coinCount=0;
                bombXs.clear();
                bombYs.clear();
                bombRectanlgles.clear();
                bombCount=0;

            }
        }

        if(gameState==2){
            batch.draw(DizzyMan,Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
        }
        else {
            batch.draw(man[0], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
        }
        manRectanlgles = new Rectangle(Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2,manY, man[manState].getWidth(),man[manState].getHeight());

        for (int i=0; i<coinRectanlgles.size();i++){
            if(Intersector.overlaps(manRectanlgles,coinRectanlgles.get(i))){
                score++;
                coinRectanlgles.remove(i);
                coinXs.remove(i);
                coinYs.remove(i);
                break;
            }
        }

        for (int i=0; i<bombRectanlgles.size();i++){
            if(Intersector.overlaps(manRectanlgles,bombRectanlgles.get(i))){
                gameState=2;
            }
        }

        bitmapFont.draw(batch,String.valueOf(score), 100,200);

        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
