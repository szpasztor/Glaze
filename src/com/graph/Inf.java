package com.graph;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/4/13
 * Time: 9:25 PM
 */
public abstract class Inf {
    public static final String TAG = "Graph";
    public static final Application.ApplicationType platform;

    static {
        platform = Gdx.app.getType();
        switch (platform) {
            case Android:
                Gdx.app.log(TAG, "Android is not supported yet. The app will exit.");
                Gdx.app.exit();
                break;
            case Desktop:
                Gdx.app.log(TAG, "Running on desktop");
                break;
            case Applet:
                Gdx.app.log(TAG, "Applets are not supported yet. The application will exit.");
                Gdx.app.exit();
                break;
            case iOS:
                Gdx.app.log(TAG, "iOS is not supported yet. The application will exit.");
                Gdx.app.exit();
                break;
            case WebGL:
                Gdx.app.log(TAG, "WebGL is not supported yet. The application will exit.");
                Gdx.app.exit();
                break;
        }
    }

}
