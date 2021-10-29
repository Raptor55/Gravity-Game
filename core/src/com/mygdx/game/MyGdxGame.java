package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.*;

public class MyGdxGame extends ApplicationAdapter {
    int radius = 64;
    ArrayList<Pixel> pixelList;

    int levelX = 0, levelY = 0;

    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Texture level;
    Texture win;
    Texture lose;
    int v;
    int i = 0;
    int counter = 0;

    Pixmap pic;

    @Override
    public void create ()
    {
	batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        level = new Texture("testlevel5.png");
        win = new Texture("winner.png");
        lose = new Texture("loser.png");
        v = 2;
        levelY = -level.getHeight() + 1080;

        pixelList = buildCircleList(radius);

        pic = new Pixmap(Gdx.files.internal("testlevel5.png"));
    }

    @Override
    public void render ()
    {
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(level, levelX, levelY);
        batch.end();

        batch.begin();
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.circle(1920 / 2, 1080 / 2, radius);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.end();
        batch.end();

        String resultString = checkWinner();
        if(!resultString.equals(""))
        {
            if(resultString.equals("win") &&  counter < 15)
            {
                batch.begin();
                batch.draw(win, 0, 0);
                batch.end();
            }

            else if(resultString.equals("lose") && counter < 15)
            {
                batch.begin();
                batch.draw(lose, 0, 0);
                batch.end();
            }
            counter++;
            if(counter == 30)
            {
                counter = 0;
            }
        }

        else
        {
            update();
        }
   }

    public String checkWinner()
    {
        for(Pixel p:pixelList)
        {
            if(pic.getPixel(p.x, p.y) == 0x23B14DFF)
            {
                return "win";
            }

            else if((pic.getPixel(p.x, p.y) == 0x880016FF))
            {
                return "lose";
            }
        }
        return "";
    }

    public void update()
    {
        if (i == 40)
        {
            if (v < 10)
            {
                v++;
            }
            i = 0;
        }
        if (Gdx.input.getRoll() >= 0)
        {
            levelX += (int) ( v * Math.sin(Math.toRadians(Gdx.input.getPitch())));
            levelY += (int) (-v * Math.cos(Math.toRadians(Gdx.input.getPitch())));
        }
        else
        {
            levelX += (int) (v * Math.sin(Math.toRadians(Gdx.input.getPitch())));
            levelY += (int) (v * Math.cos(Math.toRadians(Gdx.input.getPitch())));
        }

        for(Pixel p: pixelList)
        {
            if (Gdx.input.getRoll() >= 0)
            {
                p.y += (int) (-v * Math.cos(Math.toRadians(Gdx.input.getPitch())));
                p.x -= (int) (v * Math.sin(Math.toRadians(Gdx.input.getPitch())));
            }
            else
            {
                p.y += (int) (v * Math.cos(Math.toRadians(Gdx.input.getPitch())));
                p.x -= (int) (v * Math.sin(Math.toRadians(Gdx.input.getPitch())));
            }
        }
        i++;
    }

    public ArrayList<Pixel> buildCircleList(int radius)
    {
        ArrayList<Pixel> pixels = new ArrayList<Pixel>();

        //corner things
        pixels.add(new Pixel( radius, 0));
        pixels.add(new Pixel(-radius, 0));
        pixels.add(new Pixel(0,  radius));
        pixels.add(new Pixel(0, -radius));

        //first
        pixels.add(new Pixel ((int) (radius * Math.cos(    (Math.PI) / 8)), (int) (radius * Math.sin(    (Math.PI) / 8))));
        pixels.add(new Pixel ((int) (radius * Math.cos(    (Math.PI) / 4)), (int) (radius * Math.sin(    (Math.PI) / 4))));
        pixels.add(new Pixel ((int) (radius * Math.cos(3 * (Math.PI) / 8)), (int) (radius * Math.sin(3 * (Math.PI) / 8))));

        //second quadrant
        pixels.add(new Pixel ((int) (-radius * Math.cos(    (Math.PI) / 8)), (int) (radius * Math.sin(    (Math.PI) / 8))));
        pixels.add(new Pixel ((int) (-radius * Math.cos(    (Math.PI) / 4)), (int) (radius * Math.sin(    (Math.PI) / 4))));
        pixels.add(new Pixel ((int) (-radius * Math.cos(3 * (Math.PI) / 8)), (int) (radius * Math.sin(3 * (Math.PI) / 8))));

        //third quadrant
        pixels.add(new Pixel ((int) (-radius * Math.cos(    (Math.PI) / 8)), (int) (-radius * Math.sin(    (Math.PI) / 8))));
        pixels.add(new Pixel ((int) (-radius * Math.cos(    (Math.PI) / 4)), (int) (-radius * Math.sin(    (Math.PI) / 4))));
        pixels.add(new Pixel ((int) (-radius * Math.cos(3 * (Math.PI) / 8)), (int) (-radius * Math.sin(3 * (Math.PI) / 8))));

        //fourth quadrant
        pixels.add(new Pixel ((int) (radius * Math.cos(    (Math.PI) / 8)), (int) (-radius * Math.sin(    (Math.PI) / 8))));
        pixels.add(new Pixel ((int) (radius * Math.cos(    (Math.PI) / 4)), (int) (-radius * Math.sin(    (Math.PI) / 4))));
        pixels.add(new Pixel ((int) (radius * Math.cos(3 * (Math.PI) / 8)), (int) (-radius * Math.sin(3 * (Math.PI) / 8))));

        for(Pixel p : pixels)
        {
            p.x += 1920/2 - levelX;
            p.y += 1080/2 + (level.getHeight() - 1080) + levelY;
        }

        return pixels;
    }
}
