package com.glaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Author: Szabolcs Pásztor
 * Date: Oct 14 2013
 */
public class Physics {
    private static final String TAG = Inf.TAG + "|Physics";
    /** Min distance from which vertices attract each other */
    private static final double IDEAL_DISTANCE = 6;
    /** Controls gravity linearly */
    private static final double GRAVITY_LC = 100;
    /** Controls anti-gravity linearly */
    private static final double ANTI_GRAVITY_LC = 200;
    /** Below Ideal Distance force Multiplier */
    private static final double BID_LC = 0.1;

    private Random random = new Random();

    public Physics() {
        Gdx.app.log(TAG, "Creating Physics object");
    }

    public void update() {
        Array<Vertex> vertices = App.world.graph.vertices;
        int verticesSize = vertices.size;
        boolean[] n = new boolean[verticesSize];
        for (Vertex vertex : vertices) {
            vertex.force.set(0,0);
            vertex.forceCount = 0;
            if (vertex.fixed == false && vertex.tmpFixed == false) {
                for(int j=0;j<verticesSize;j++) {n[j]=false;}
                for (Integer i : vertex.neighbors) {
                    n[i] = true;
                    Vec2 v = Vec2.sub(vertices.get(i).pos, vertex.pos);
                    double d = v.len();
                    double q;
                    if (d >= IDEAL_DISTANCE) {
                        double x = v.len() - IDEAL_DISTANCE;
                        q = GRAVITY_LC * Math.pow(x, 2);
                        double limit = ((d- IDEAL_DISTANCE)/2)/d-0.01;
                        if (q > limit) {q = limit;}
                        Vec2 tmpF = Vec2.mul(v, q);
                        vertex.force.add(tmpF);
                        ++vertex.forceCount;
                    } else {
                        if (d!=0) {
                            double r = IDEAL_DISTANCE-d;
                            q = -1*Math.pow(r,2)*BID_LC;
                            Vec2 tmpF = Vec2.mul(v, q);
                            if (tmpF.len() > (IDEAL_DISTANCE-d)/2) {
                                tmpF.mul(((IDEAL_DISTANCE-d)/2)/tmpF.len());
                            }
                            vertex.force.add(tmpF);
                            ++vertex.forceCount;
                        } else {
                            double x = ((random.nextDouble()-0.5)/10)-0.01;
                            double y = ((random.nextDouble()-0.5)/10)+0.01;
                            Vec2 z = new Vec2(x, y);
                            vertex.force.add(z);
                            ++vertex.forceCount;
                        }
                    }
                }
                for (int i=0; i<verticesSize; i++) {
                    if (n[i]==false && vertices.get(i)!=vertex) {
                        Vec2 v = Vec2.sub(vertices.get(i).pos, vertex.pos);
                        double d = v.len();
                        if (d!=0) {
                            double q = 1/Math.pow(d,3);
                            q *= ANTI_GRAVITY_LC;
                            if (q>1) { q = 1; }
                            Vec2 tmpF = Vec2.mul(v, -q);
                            vertex.force.add(tmpF);
                            ++vertex.forceCount;
                        } else {
                            double x = (random.nextDouble()-0.5)+0.01;
                            double y = (random.nextDouble()-0.5)-0.01;
                            Vec2 z = new Vec2(x,y);
                            vertex.force.add(z);
                            ++vertex.forceCount;
                        }
                    }
                }
                if (vertex.forceCount > 0) {
                    vertex.force.div(vertex.forceCount);
                }
            }
        }
        for (Vertex vertex : vertices) {
            vertex.pos.add(vertex.force);
        }
    }

}