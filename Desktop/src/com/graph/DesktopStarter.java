package com.graph;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/2/13
 * Time: 4:51 PM
 */
public class DesktopStarter {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Graph";
        cfg.useGL20 = true;
        cfg.width = Renderer.windowWidth;
        cfg.height = Renderer.windowHeight;
        cfg.resizable = true;
        new LwjglApplication(new App(), cfg);
    }
}
