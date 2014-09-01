package com.glaze;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/13/13
 */
public class Rectangle {

    public Vec2 p0 = new Vec2();
    public Vec2 p2 = new Vec2();
    public double width;
    public double height;
    public Vec2 center = new Vec2();

    public Rectangle (double p0x, double p0y, double p2x, double p2y) {
        this.set(p0x, p0y, p2x, p2y);
    }
    public Rectangle (Vec2 p1, Vec2 p2) {
        this(p1.x, p1.y, p2.x, p2.y);
    }
    public Rectangle () {
        this(0,0,0,0);
    }

    public void set (Vec2 p0, Vec2 p2) {
        this.p0.set(p0);
        this.p2.set(p2);
        this.width = p2.x-p0.x;
        this.height = p2.y-p0.y;
        this.center.set(p0.x + width / 2, p0.y + height / 2);
    }
    public void set (Vec2 p0, double width, double height) {
        set(p0, new Vec2(p0.x+width, p0.y+height));
    }
    public void set (double p0x, double p0y, double p2x, double p2y) {
        set(new Vec2(p0x, p0y), new Vec2(p2x, p2y));
    }
    public void set (Rectangle r) {
        set(r.p0, r.p2);
    }

    public void translate(double x, double y) {
        this.p0.add(x, y);
        this.p2.add(x, y);
        center.add(x, y);
    }

    /** Sets the rectangle's position (lower left corner) */
    public void setPosition(double x, double y) {
        translate(x-p0.x, y-p0.y);
    }

    /** x,y: percent */
    public void scale(double x, double y) {
        this.setWidth(width*x);
        this.setHeight(height*y);
    }

    public void setWidth(double w) {
        width = w;
        p0.x = center.x-w/2f;
        p2.x = center.x+w/2f;
    }

    public void setHeight(double h) {
        height = h;
        p0.y = center.y-h/2f;
        p2.y = center.y+h/2f;
    }

    /** Returns true if the rectangle collides with the vertex, false otherwise.
     * Not perfect solution, but good enough.
     */
    protected boolean collides(Vertex vertex) {
        Rectangle rect = new Rectangle(vertex.pos.x- vertex.r, vertex.pos.y- vertex.r, vertex.pos.x+ vertex.r, vertex.pos.y+ vertex.r);
        return (   contains(this, rect.p0.x, rect.p0.y)
                || contains(this, rect.p2.x, rect.p0.y)
                || contains(this, rect.p2.x, rect.p2.y)
                || contains(this, rect.p0.x, rect.p2.y)
                || contains(this, rect.p0.x+rect.width/3, rect.p0.y+rect.height/2)
                || contains(this, rect.p0.x+rect.width/3*2, rect.p0.y+rect.height/2)
                || contains(rect, this.p0.x, this.p0.y)
                || contains(rect, this.p2.x, this.p0.y)
                || contains(rect, this.p2.x, this.p2.y)
                || contains(rect, this.p0.x, this.p2.y)
                || contains(rect, this.p0.x+this.width/3, this.p0.y+this.height/2)
                || contains(rect, this.p0.x+this.width/3*2, this.p0.y+this.height/2));
    }

    /** Returns true if the parameter point is inside the rectangle, false otherwise. */
    public static boolean contains(Rectangle rect, double x, double y) {
        return x>=rect.p0.x && x<=rect.p2.x && y>=rect.p0.y && y<=rect.p2.y;
    }

}
