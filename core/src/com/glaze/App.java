package com.glaze;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/2/13
 */
public class App implements ApplicationListener {
    private static final String TAG = Inf.TAG + "|App";

    public static Renderer renderer;
    public static World world;
    public static Physics physics;
    public static InputProcessor inputProcessor;

    public static FPSLogger fpsLogger;

    @Override
    public void create() {
        Gdx.app.log(TAG, "Starting application");
        world = new World();
        physics = new Physics();
        renderer = new Renderer();
        inputProcessor = new InputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
        fpsLogger = new FPSLogger();
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        world.update();
        if (renderer.mode == Renderer.Mode.GRAPH) {
            physics.update();
        }
        renderer.render();
        fpsLogger.log();
    }

    @Override
    public void pause() {
        Gdx.app.log(TAG, "paused");
    }

    @Override
    public void resume() {
        Gdx.app.log(TAG, "resumed");
    }

    @Override
    public void dispose() {
        renderer.dispose();
        Gdx.app.log(TAG, "disposed");
    }

    @Override
    public void resize(int width, int height) {
        if (width<758) { width = 758; }
        if (height<675) { height = 675; }
        Gdx.app.log(TAG, "resized "+width+","+height);
        double a = (double)width/(double)Renderer.windowWidth;
        double b = (double)height/(double)Renderer.windowHeight;
        Renderer.windowWidth = width;
        Renderer.windowHeight = height;
        Gdx.graphics.setDisplayMode(width, height, false);
        renderer.spriteBatch = new SpriteBatch();
        renderer.shapeRenderer = new ShapeRenderer();
        renderer.font = new BitmapFont();
        world.camera.setWidth(world.camera.width*a);
        world.camera.setHeight(world.camera.height*b);

        renderer.plus.x = 15; renderer.plus.y = renderer.windowHeight-47;
        renderer.minus.x = 15; renderer.minus.y = renderer.plus.y-renderer.plus.height-10;
        renderer.open.x = renderer.windowWidth-10-renderer.openTR.getRegionWidth(); renderer.open.y = renderer.windowHeight-renderer.openTR.getRegionHeight()-10;
        renderer.save.x = renderer.windowWidth-10-renderer.saveTR.getRegionWidth(); renderer.save.y = renderer.open.y-7-renderer.saveTR.getRegionHeight();
        renderer.newButton.x = renderer.windowWidth-10-renderer.newTR.getRegionWidth(); renderer.newButton.y = renderer.save.y-7-renderer.newTR.getRegionHeight();
        renderer.names.x = renderer.windowWidth-10-renderer.namesOffTR.getRegionWidth(); renderer.names.y = renderer.newButton.y-15-renderer.namesOffTR.getRegionHeight();
        renderer.editName.x = renderer.windowWidth-10-renderer.editNameOnTR.getRegionWidth(); renderer.editName.y = renderer.names.y-7-renderer.editNameOnTR.getRegionHeight();
        renderer.removeVertex.x = renderer.windowWidth-10- renderer.removeVertexTR.getRegionWidth(); renderer.removeVertex.y = renderer.editName.y-7- renderer.removeVertexTR.getRegionHeight();
        renderer.fix.x = renderer.windowWidth-10-renderer.fixTR.getRegionWidth(); renderer.fix.y = renderer.removeVertex.y-7-renderer.fixTR.getRegionHeight();
        renderer.selectNeighbors.x = renderer.windowWidth-10-renderer.selectNeighborsTR.getRegionWidth(); renderer.selectNeighbors.y = renderer.fix.y-7-renderer.selectNeighborsTR.getRegionHeight();
        renderer.makeEdge.x = renderer.windowWidth-10-renderer.makeEdgeTR.getRegionWidth(); renderer.makeEdge.y = renderer.selectNeighbors.y-7-renderer.makeEdgeTR.getRegionHeight();
        renderer.removeEdge.x = renderer.windowWidth-10-renderer.removeEdgeTR.getRegionWidth(); renderer.removeEdge.y = renderer.makeEdge.y-7-renderer.removeEdgeTR.getRegionHeight();
        renderer.creditsButton.x = renderer.windowWidth-10- renderer.creditsButtonTR.getRegionWidth(); renderer.creditsButton.y = 10;
        renderer.closeCredits.x = renderer.windowWidth/2+renderer.creditsTR.getRegionWidth()/2-13-renderer.closeCreditsTR.getRegionWidth(); renderer.closeCredits.y = (renderer.windowHeight/2-renderer.creditsTR.getRegionHeight()/2)+13;
        renderer.helpButton.x = renderer.windowWidth-10- renderer.helpButtonTR.getRegionWidth(); renderer.helpButton.y = renderer.creditsButton.y+ renderer.creditsButton.height+7;
        renderer.closeHelp.x = renderer.windowWidth/2+renderer.helpTR.getRegionWidth()/2-13-renderer.closeHelpTR.getRegionWidth(); renderer.closeHelp.y = renderer.windowHeight/2-renderer.helpTR.getRegionHeight()/2+13;

    }

}
