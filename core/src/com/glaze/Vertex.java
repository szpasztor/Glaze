package com.glaze;

import com.badlogic.gdx.utils.Array;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/6/13
 */
public class Vertex {
    public Vec2 pos;
    public Array<Integer> neighbors = new Array<Integer>();
    public double r;
    public Vec2 force = new Vec2();
    public int forceCount;
    public boolean selected = false;
    /** Used in rectangle-selecting */
    public boolean prevSelected = false;
    /** Used in neighbor selecting */
    public boolean tmp = false;
    public boolean fixed = false;
    public boolean tmpFixed = false;
    public String name;

    public Rectangle projection = new Rectangle(); // rectangle on the screen (in pixels)
    public boolean visible = false; // true if the camera can see it, false otherwise

    public Vertex(double x, double y, double r, String name) {
        this.pos = new Vec2(x,y);
        this.r = r;
        this.name = name;
    }
    public Vertex(Vec2 v, double r, String name) {
        this(v.x, v.y, r, name);
    }

    /** Recalculates the radius of the vertex depending on the degree of the vertex
     * Always invoke after the degree has changed (neighbors have been added/removed) */
    public void calcRadius() {
        final double c = 6;
        int n = this.neighbors.size;
        double a = ((n-1)/c)+1;
        this.r = Math.sqrt(a)/2;
    }

}
