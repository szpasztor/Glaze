package com.glaze;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/6/13
 */
public class Vec2 {

    public double x;
    public double y;

    public Vec2(double px, double py) {
        this.x = px;
        this.y = py;
    }
    public Vec2(Vec2 v) {
        this(v.x, v.y);
    }
    public Vec2() {
        this(0f,0f);
    }

    public double len() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public void set (double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void set (Vec2 v) {
        set(v.x, v.y);
    }

    public void add (double x, double y) {
        this.x += x;
        this.y += y;
    }
    public void add (Vec2 v) {
        add(v.x, v.y);
    }

    public void sub (double x, double y) {
        this.x -= x;
        this.y -= y;
    }
    public void sub (Vec2 v) {
        sub(v.x, v.y);
    }

    public void mul (double x, double y) {
        this.x *= x;
        this.y *= y;
    }
    public void mul (Vec2 v) {
        mul(v.x, v.y);
    }
    public void mul (double a) {
        mul(a, a);
    }

    public void div (double x, double y) {
        this.x /= x;
        this.y /= y;
    }
    public void div (Vec2 v) {
        div(v.x, v.y);
    }
    public void div (double a) {
        div(a,a);
    }

    public void pow (double a) {
        this.x = Math.pow(this.x, a);
        this.y = Math.pow(this.y, a);
    }

    public void sqrt () {
        this.x = Math.sqrt(this.x);
        this.y = Math.sqrt(this.y);
    }

    public void abs () {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
    }


    public static Vec2 add (double x1, double y1, double x2, double y2) {
        return new Vec2(x1+x2, y1+y2);
    }
    public static Vec2 add (Vec2 v1, Vec2 v2) {
        return add(v1.x, v1.y, v2.x, v2.y);
    }
    public static Vec2 add (Vec2 v, double a) {
        return add(v.x, v.y, a, a);
    }

    public static Vec2 sub (double x1, double y1, double x2, double y2) {
        return new Vec2(x1-x2, y1-y2);
    }
    public static Vec2 sub (Vec2 v1, Vec2 v2) {
        return sub(v1.x, v1.y, v2.x, v2.y);
    }
    public static Vec2 sub (Vec2 v, double a) {
        return sub(v.x, v.y, a, a);
    }

    public static Vec2 mul (Vec2 v, double x, double y) {
        return new Vec2(v.x*x, v.y*y);
    }
    public static Vec2 mul (Vec2 v1, Vec2 v2) {
        return mul(v1, v2.x, v2.y);
    }
    public static Vec2 mul (Vec2 v, double a) {
        return mul(v, a, a);
    }

    public static Vec2 div (Vec2 v, double x, double y) {
        return new Vec2(v.x/x, v.y/y);
    }
    public static Vec2 div (Vec2 v1, Vec2 v2) {
        return div(v1, v2.x, v2.y);
    }
    public static Vec2 div (Vec2 v, double a) {
        return div(v, a, a);
    }

    public static Vec2 pow (Vec2 v, double a) {
        return  new Vec2(Math.pow(v.x,a), Math.pow(v.y,a));
    }

    public static Vec2 sqrt (Vec2 v) {
        return new Vec2(Math.sqrt(v.x), Math.sqrt(v.y));
    }

    public static Vec2 abs (Vec2 v) {
        return new Vec2(Math.abs(v.x), Math.abs(v.y));
    }


    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

}