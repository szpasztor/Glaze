package com.glaze.com.graph.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Author: Szabolcs Pásztor
 * Date: 10/18/13
 * Time: 7:54 PM
 */
public abstract class Button {
    public TextureRegion textureRegion;
    public int x;
    public int y;
    public int width;
    public int height;
    /** true - turned on, false - turned off */
    public boolean turnedOn;

    public Button (TextureRegion tr, int x, int y, int width, int height, boolean turnedOn) {
        this.textureRegion = tr;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.turnedOn = turnedOn;
    }
    public Button (TextureRegion tr, int x, int y, boolean turnedOn) {
        this(tr, x, y, tr.getRegionWidth(), tr.getRegionHeight(), turnedOn);
    }

    public abstract void onClick();

    public boolean inside(int x, int y) {
        return x>=this.x && x<=this.x+width && y>=this.y && y<=this.y+height;
    }

}
