package com.graph.com.graph.types;

import com.badlogic.gdx.Gdx;
import com.graph.*;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/13/13
 * Time: 7:12 PM
 */
public class Camera extends Rectangle {
    public static final String TAG = Inf.TAG + "|Camera";

    /** The latest camera width and height that is used in convVect. */
    public Vec2 latestConvVectWH = new Vec2(-1, -1);

    public Camera(float p0x, float p0y, float p2x, float p2y) {
        super(p0x, p0y, p2x, p2y);
        Gdx.app.log(TAG, "Creating camera object");
    }

    public void update() {
        if (latestConvVectWH.x != this.width || latestConvVectWH.y != this.height) {
            App.world.updateConversionVect();
        }
    }

    public void updateVertexProjections() {
        for (Vertex vertex : App.world.graph.vertices) {
            if (this.collides(vertex)) {
                Vec2 p0 = App.world.worldToPix(Vec2.sub(vertex.pos, vertex.r));
                Vec2 p2 = App.world.worldToPix(Vec2.add(vertex.pos, vertex.r));
                vertex.projection.set(p0, p2);
                vertex.visible = true;
            } else {
                vertex.visible = false;
            }
        }
    }

    public String toString() {
        return new String("["+this.p0.x+","+this.p0.y+", "+this.p2.x+","+this.p2.y+"]");
    }

}
