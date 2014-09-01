package com.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.graph.com.graph.types.Button;
import com.graph.com.graph.types.Edge;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/4/13
 * Time: 9:25 PM
 */
public class Renderer {
    private static final String TAG = Inf.TAG + "|Renderer";
    private static final Color BLACK = new Color(0f, 0f, 0f, 1f);
    private static final Color BLUE = new Color(0f, 0.54f, 1f, 1f);
    private static final Color ORANGE = new Color(1f, 0.64f, 0f, 1f);
    /** Window height used in projection and cursor position*/
    public static int windowWidth = 1200;
    /** Window width used in projection and cursor position*/
    public static int windowHeight = 800;
    public enum Mode {
        GRAPH, CREDITS, HELP,
    }
    public Mode mode = Mode.GRAPH;
    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;
    public Texture texturePack1;
    public Texture texturePack2;

    TextureRegion plusTR;
    Button plus;
    TextureRegion minusTR;

    Button minus;
    TextureRegion openTR;
    Button open;
    TextureRegion saveTR;
    Button save;
    TextureRegion newTR;

    Button newButton;
    TextureRegion namesOffTR;
    Button names;
    TextureRegion namesOnTR;
    TextureRegion editNameOnTR;
    TextureRegion editNameOffTR;
    Button editName;
    boolean editNameVisible = false;
    TextureRegion removeVertexTR;
    Button removeVertex;
    TextureRegion makeEdgeTR;
    Button makeEdge;
    TextureRegion removeEdgeTR;
    Button removeEdge;
    TextureRegion fixTR;
    Button fix;
    TextureRegion selectNeighborsTR;

    Button selectNeighbors;
    TextureRegion helpButtonTR;
    Button helpButton;
    TextureRegion helpTR;
    TextureRegion closeHelpTR;

    Button closeHelp;
    TextureRegion creditsButtonTR;
    Button creditsButton;
    TextureRegion creditsTR;
    TextureRegion closeCreditsTR;

    Button closeCredits;

    public Renderer() {
        Gdx.app.log(TAG, "Creating Renderer object");
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(BLACK);

        texturePack1 = new Texture(Gdx.files.internal("textures/texture_packed1.png"));
        Gdx.app.log(TAG, "Texture pack 1 loaded");
        texturePack2 = new Texture(Gdx.files.internal("textures/texture_packed2.png"));
        Gdx.app.log(TAG, "Texture pack 2 loaded");

        plusTR = new TextureRegion(texturePack1, 122,43,32,32);
        plus = new Button(plusTR, 15, windowHeight-47, plusTR.getRegionWidth(), plusTR.getRegionHeight(), true) {
            @Override
            public void onClick() {
                App.world.zoom(-1);
            }
        };
        minusTR = new TextureRegion(texturePack1, 914,827,32,32);
        minus = new Button(minusTR, 15, plus.y-plus.height-10, minusTR.getRegionWidth(), minusTR.getRegionHeight(), true) {
            @Override
            public void onClick() {
                App.world.zoom(1);
            }
        };

        openTR = new TextureRegion(texturePack1,1,27,119,24);
        open = new Button(openTR, windowWidth-10-openTR.getRegionWidth(), windowHeight-openTR.getRegionHeight()-10, true) {
            @Override
            public void onClick() {
                App.world.open();
            }
        };
        saveTR = new TextureRegion(texturePack1,793,759,119,24);
        save = new Button(saveTR, windowWidth-10-saveTR.getRegionWidth(), open.y-7-saveTR.getRegionHeight(), true) {
            @Override
            public void onClick() {
                App.world.save();
            }
        };
        newTR = new TextureRegion(texturePack1,122,103,119,24);
        newButton = new Button(newTR, windowWidth-10-newTR.getRegionWidth(), save.y-7-newTR.getRegionHeight(), true) {
            @Override
            public void onClick() {
                App.world.resetGraph();
            }
        };

        namesOffTR = new TextureRegion(texturePack1,1,53,119,24);
        namesOnTR = new TextureRegion(texturePack1, 122,77,119,24);
        names = new Button(null, windowWidth-10-namesOffTR.getRegionWidth(),newButton.y-15-namesOffTR.getRegionHeight(),
                namesOffTR.getRegionWidth(), namesOffTR.getRegionHeight(), true) {
            @Override
            public void onClick() {
                App.world.flipVertecNameVisible();
            }
        };
        editNameOffTR = new TextureRegion(texturePack1, 793,733,119,24);
        editNameOnTR = new TextureRegion(texturePack1, 1,79,119,24);
        editName = new Button(null, windowWidth-10-editNameOnTR.getRegionWidth(), names.y-7-editNameOnTR.getRegionHeight(),
                editNameOnTR.getRegionWidth(), editNameOnTR.getRegionHeight(), false) {
            @Override
            public void onClick() {
                App.world.editVertexName();
            }
        };

        removeVertexTR = new TextureRegion(texturePack1,1,105,119,48);
        removeVertex = new Button(removeVertexTR, windowWidth-10- removeVertexTR.getRegionWidth(),
                editName.y-7- removeVertexTR.getRegionHeight(), false) {
            @Override
            public void onClick() {
                App.world.removeVertex();
            }
        };
        fixTR = new TextureRegion(texturePack1, 793,785,119,24);
        fix = new Button(fixTR, windowWidth-10-fixTR.getRegionWidth(), removeVertex.y-7-fixTR.getRegionHeight(), false) {
            @Override
            public void onClick() {
                App.world.fix();
            }
        };
        selectNeighborsTR = new TextureRegion(texturePack1,793,811,119,48);
        selectNeighbors = new Button(selectNeighborsTR,
                windowWidth-10-selectNeighborsTR.getRegionWidth(), fix.y-7-selectNeighborsTR.getRegionHeight(), false) {
            @Override
            public void onClick() {
                App.world.selectNeighbors();
            }
        };
        makeEdgeTR = new TextureRegion(texturePack1,243,129,119,24);
        makeEdge = new Button(makeEdgeTR,
                windowWidth-10-makeEdgeTR.getRegionWidth(), selectNeighbors.y-7-makeEdgeTR.getRegionHeight(), false) {
            @Override
            public void onClick() {
                App.world.edge(true);
            }
        };
        removeEdgeTR = new TextureRegion(texturePack1,364,129,119,24);
        removeEdge = new Button(removeEdgeTR,
                windowWidth-10-removeEdgeTR.getRegionWidth(), makeEdge.y-7-removeEdgeTR.getRegionHeight(), false) {
            @Override
            public void onClick() {
                App.world.edge(false);
            }
        };

        creditsButtonTR = new TextureRegion(texturePack1, 1,1,119,24);
        creditsButton = new Button(creditsButtonTR, windowWidth-10- creditsButtonTR.getRegionWidth(), 10, true) {
            @Override
            public void onClick() {
                mode = Mode.CREDITS;
            }
        };
        creditsTR = new TextureRegion(texturePack2, 1,1,411,322);
        closeCreditsTR = new TextureRegion(texturePack1,243,103,119,24);
        closeCredits = new Button(closeCreditsTR,
                windowWidth/2+creditsTR.getRegionWidth()/2-13-closeCreditsTR.getRegionWidth(),
                (windowHeight/2-creditsTR.getRegionHeight()/2)+13, false) {
            @Override
            public void onClick() {
                mode = Mode.GRAPH;
            }
        };

        helpButtonTR = new TextureRegion(texturePack1, 122,129,119,24);
        helpButton = new Button(helpButtonTR,
                windowWidth-10- helpButtonTR.getRegionWidth(), creditsButton.y+ creditsButton.height+7, true) {
            @Override
            public void onClick() {
                mode = Mode.HELP;
            }
        };
        helpTR = new TextureRegion(texturePack1, 1,155,790,704);
        closeHelpTR = new TextureRegion(texturePack1,243,103,119,24);
        closeHelp = new Button(closeHelpTR,
                windowWidth/2+helpTR.getRegionWidth()/2-13-closeHelpTR.getRegionWidth(),
                windowHeight/2-helpTR.getRegionHeight()/2+13, false) {
            @Override
            public void onClick() {
                mode = Mode.GRAPH;
            }
        };
        Gdx.app.log(TAG, "TextureRegions created");
    }

    public void render() {
        App.world.camera.updateVertexProjections();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderEdges();
        renderVertices();
        if (App.world.graph.vertexNameVisible) {
            renderVertexNames();
        }
        renderCursorModes();
        //renderButtonBackgrounds();
        renderButtons();
        switch (mode) {
            case GRAPH:
                break;
            case CREDITS:
                renderCredits();
                break;
            case HELP:
                renderHelp();
                break;
        }
    }

    public void renderEdges() {
        World world = App.world;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Edge edge : world.graph.edges) {
            Vec2 p1 = world.worldToPix(world.graph.vertices.get(edge.a).pos);
            Vec2 p2 = world.worldToPix(world.graph.vertices.get(edge.b).pos);
            Color c1 = world.graph.vertices.get(edge.a).selected? BLUE : BLACK;
            Color c2 = world.graph.vertices.get(edge.b).selected? BLUE : BLACK;
            shapeRenderer.line((float) p1.x, (float) p1.y, (float) p2.x, (float) p2.y, c1, c2);
        }
        shapeRenderer.end();
    }

    public void renderVertices() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Array<Vertex> vertices = App.world.graph.vertices;
        for(Vertex vertex:vertices) {
            if (vertex.visible) {
                float x = (float) (vertex.projection.center.x);
                float y = (float) (vertex.projection.center.y);
                float r1 = (float) (vertex.projection.width/2);
                float r2 = (float) (r1*0.833);
                if (!vertex.selected) {
                    if ( !(vertex.fixed || vertex.tmpFixed)) {
                        shapeRenderer.setColor(BLACK);
                        shapeRenderer.circle(x, y, r1);
                    } else {
                        shapeRenderer.setColor(ORANGE);
                        shapeRenderer.circle(x, y, r1);
                        shapeRenderer.setColor(BLACK);
                        shapeRenderer.circle(x, y, r2);

                    }
                } else {
                    if ( !(vertex.fixed || vertex.tmpFixed)) {
                        shapeRenderer.setColor(BLUE);
                        shapeRenderer.circle(x, y, r1);
                    } else {
                        shapeRenderer.setColor(ORANGE);
                        shapeRenderer.circle(x, y, r1);
                        shapeRenderer.setColor(BLUE);
                        shapeRenderer.circle(x, y, r2);
                    }
                }
            }
        }
        shapeRenderer.end();
    }

    public void renderVertexNames() {
        Array<Vertex> vertices = App.world.graph.vertices;
        spriteBatch.begin();
        for (Vertex vertex:vertices) {
            if (vertex.visible) {
                float x = (float) (vertex.projection.center.x);
                float y = (float) (vertex.projection.center.y);
                float a = (float) (Math.sqrt(Math.pow(vertex.projection.width/2,2)/2));
                if (vertex.selected) {
                    font.setColor(BLUE);
                } else {
                    font.setColor(BLACK);
                }
                font.draw(spriteBatch, vertex.name, x+a, y-a);
            }
        }
        spriteBatch.end();
    }

    public void renderCursorModes() {
        switch (App.inputProcessor.cursorMode) {
            case NORMAL:
                break;
            case MOVE:
                break;
            case DRAG:
                break;
            case SELECTION:
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                Gdx.gl.glEnable(GL20.GL_BLEND);
                shapeRenderer.setColor(0.30f, 0.47f, 1f, 0.5f);
                float x = App.inputProcessor.selectionStartX;
                float y = App.inputProcessor.selectionStartY;
                float width = Gdx.input.getX()-x;
                float height = (Renderer.windowHeight-Gdx.input.getY())-y;
                shapeRenderer.rect(x, y, width, height);
                shapeRenderer.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(0.00f, 0.20f, 1f, 0.9f);
                shapeRenderer.rect(x, y, width, height);
                shapeRenderer.end();
                break;
            case BUTTON:
                break;
        }
    }

    public void renderButtons() {
        spriteBatch.begin();
        // Navigation
        drawButton(plus);
        drawButton(minus);
        // File operation buttons
        drawButton(open);
        drawButton(save);
        drawButton(newButton);
        // Editing buttons
        if (App.world.graph.vertexNameVisible) {
            spriteBatch.draw(namesOnTR, names.x, names.y);
        } else {
            spriteBatch.draw(namesOffTR, names.x, names.y);
        }
        if (editNameVisible) {
            if (editName.turnedOn) {
                spriteBatch.draw(editNameOnTR, editName.x, editName.y);
            } else {
                spriteBatch.draw(editNameOffTR, editName.x, editName.y);
            }
        }
        drawButton(removeVertex);
        drawButton(fix);
        drawButton(selectNeighbors);
        //
        drawButton(makeEdge);
        drawButton(removeEdge);
        // Help button
        drawButton(helpButton);
        // Credits button
        drawButton(creditsButton);
        spriteBatch.end();
    }

    public void drawButton(Button button) {
        if (button.turnedOn) {
            spriteBatch.draw(button.textureRegion, button.x, button.y);
        }
    }

    public void renderCredits() {
        spriteBatch.begin();
        spriteBatch.draw(creditsTR, windowWidth / 2 - creditsTR.getRegionWidth() / 2, windowHeight / 2 - creditsTR.getRegionHeight() / 2);
        spriteBatch.draw(closeCreditsTR, closeCredits.x, closeCredits.y);
        spriteBatch.end();
    }

    public void renderHelp() {
        spriteBatch.begin();
        spriteBatch.draw(helpTR, windowWidth / 2 - helpTR.getRegionWidth() / 2, windowHeight / 2 - helpTR.getRegionHeight() / 2);
        spriteBatch.draw(closeHelpTR, closeHelp.x, closeHelp.y);
        spriteBatch.end();
    }

    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        texturePack1.dispose();
        texturePack2.dispose();
    }

}
