package com.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.Input.Buttons;
import static com.badlogic.gdx.Input.Keys.*;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/4/13
 * Time: 9:25 PM
 */
public class InputProcessor implements com.badlogic.gdx.InputProcessor {
    private static final String TAG = Inf.TAG + "|InputProcessor";
    private Vec2 cursor = new Vec2();
    public boolean left = false;
    public boolean right = false;
    public boolean middle = false;
    public boolean shift = false;
    public boolean ctrl = false;
    public boolean m = false;
    public int selectionStartX;
    public int selectionStartY;
    public enum CursorMode {
        NORMAL,
        MOVE,
        DRAG,
        SELECTION,
        BUTTON,
    }
    /** Use setCursorMode(CursorMode) to set */
    public CursorMode cursorMode = CursorMode.NORMAL;

    public InputProcessor() {
        Gdx.app.log(TAG, "Creating InputProcessor object");
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Renderer.windowHeight-screenY;
        cursor.x = screenX;
        cursor.y = screenY;
        switch (button) {
            case Buttons.LEFT:
                Renderer renderer = App.renderer;
                if (App.renderer.mode == Renderer.Mode.GRAPH) {
                    left = true;
                    selectionStartX = screenX;
                    selectionStartY = screenY;
                    // Navigation
                    if (renderer.plus.turnedOn && renderer.plus.inside(screenX, screenY)) {
                        renderer.plus.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    } else if (renderer.minus.turnedOn && renderer.minus.inside(screenX, screenY)) {
                        renderer.minus.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    }
                    // File operation buttons
                    else if (renderer.open.turnedOn && renderer.open.inside(screenX, screenY)) {
                        renderer.open.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    } else if (renderer.save.turnedOn && renderer.save.inside(screenX, screenY)) {
                        renderer.save.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    } else if (renderer.newButton.turnedOn && renderer.newButton.inside(screenX, screenY)) {
                        renderer.newButton.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    }
                    // Editing buttons
                    else if (renderer.names.inside(screenX, screenY)) {
                        renderer.names.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    } else if (renderer.editNameVisible && renderer.editName.inside(screenX, screenY)) {
                        if (renderer.editName.turnedOn) {
                            renderer.editName.onClick();
                        }
                        setCursorMode(CursorMode.BUTTON);
                    } else if (renderer.removeVertex.turnedOn && renderer.removeVertex.inside(screenX, screenY)) {
                        renderer.removeVertex.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    } else if (renderer.selectNeighbors.turnedOn && renderer.selectNeighbors.inside(screenX, screenY)) {
                        renderer.selectNeighbors.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    } else if (renderer.makeEdge.turnedOn && renderer.makeEdge.inside(screenX, screenY)) {
                        renderer.makeEdge.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    } else if (renderer.removeEdge.turnedOn && renderer.removeEdge.inside(screenX, screenY)) {
                        renderer.removeEdge.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    } else if (renderer.fix.turnedOn && renderer.fix.inside(screenX, screenY)) {
                        renderer.fix.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    }
                    // Help
                    else if (renderer.helpButton.turnedOn && renderer.helpButton.inside(screenX, screenY)) {
                        renderer.helpButton.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    // Credits
                    } else if (renderer.creditsButton.turnedOn && renderer.creditsButton.inside(screenX, screenY)) {
                        renderer.creditsButton.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    }
                } else if (App.renderer.mode == Renderer.Mode.CREDITS) {
                    if (renderer.closeCredits.inside(screenX, screenY)) {
                        renderer.closeCredits.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    }
                } else if (App.renderer.mode == Renderer.Mode.HELP) {
                    if (renderer.closeHelp.inside(screenX, screenY)) {
                        renderer.closeHelp.onClick();
                        setCursorMode(CursorMode.BUTTON);
                    }
                }
                break;
            case Buttons.MIDDLE:
                if (App.renderer.mode == Renderer.Mode.GRAPH) {
                    middle = true;
                    Array<Vertex> vertices = App.world.graph.vertices;
                    for(Vertex vertex:vertices) {
                        if (vertex.selected) { vertex.tmpFixed = true; }
                    }
                    setCursorMode(CursorMode.DRAG);
                }
                break;
            case Buttons.RIGHT:
                if (App.renderer.mode == Renderer.Mode.GRAPH) {
                    right = true;
                    setCursorMode(CursorMode.MOVE);
                }
                break;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        screenY = Renderer.windowHeight-screenY;
        if (left) {
            if (App.renderer.mode == Renderer.Mode.GRAPH) {
                if (cursorMode == CursorMode.NORMAL) {
                    setCursorMode(CursorMode.SELECTION);
                }
            }
        } else if (middle) {
            if (App.renderer.mode == Renderer.Mode.GRAPH) {
                double x = (screenX-cursor.x)/ App.world.conv.x;
                double y = (screenY-cursor.y)/ App.world.conv.y;
                Array<Vertex> vertices = App.world.graph.vertices;
                for (Vertex vertex : vertices) {
                    if (vertex.selected) {
                        vertex.pos.add(x, y);
                    }
                }
            }
        } else if (right) {
            if (App.renderer.mode == Renderer.Mode.GRAPH) {
                double x = -(screenX-cursor.x)/ App.world.conv.x;
                double y = -(screenY-cursor.y)/ App.world.conv.y;
                App.world.camera.translate(x, y);
            }
        }
        cursor.x = screenX;
        cursor.y = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenY = Renderer.windowHeight-screenY;
        switch (button) {
            case Buttons.LEFT:
                left = false;
                if (App.renderer.mode == Renderer.Mode.GRAPH) {
                    if (cursorMode == CursorMode.NORMAL || cursorMode == CursorMode.SELECTION) {
                        if (Math.sqrt(Math.pow(selectionStartX-screenX,2) + Math.pow(selectionStartY-screenY,2)) < 20) {
                            Vec2 v = App.world.pixToWorld(screenX, screenY);
                            App.world.click(v);
                        }
                    }
                }
                break;
            case Buttons.MIDDLE:
                middle = false;
                if (App.renderer.mode == Renderer.Mode.GRAPH) {
                    Array<Vertex> vertices = App.world.graph.vertices;
                    for(Vertex vertex:vertices) {
                        if (vertex.selected) { vertex.tmpFixed = false; }
                    }
                }
                break;
            case Buttons.RIGHT:
                right = false;
                break;
        }
        setCursorMode(CursorMode.NORMAL);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == SHIFT_LEFT) {
            shift = true;
        } else if (keycode == CONTROL_LEFT) {
            ctrl = true;
        }
        if (ctrl) {
            if (App.renderer.mode == Renderer.Mode.GRAPH) {
                switch (keycode) {
                    case S:
                        App.world.save();
                        break;
                    case O:
                        App.world.open();
                        break;
                    case N:
                        App.world.resetGraph();
                        break;
                }
            }
        } else {
            if (App.renderer.mode == Renderer.Mode.GRAPH) {
                switch (keycode) {
                    case ESCAPE:
                        Gdx.app.exit();
                        break;
                    case X:
                        App.world.removeVertex();
                        break;
                    case N:
                        int x = Gdx.input.getX();
                        int y = Renderer.windowHeight-Gdx.input.getY();
                        Vec2 v = new Vec2(App.world.pixToWorld(x, y));
                        App.world.newVertex(v);
                        break;
                    case M:
                        m = true;
                        break;
                    case E:
                        App.world.edge(true);
                        break;
                    case R:
                        App.world.edge(false);
                        break;
                    case A:
                        App.world.all();
                        break;
                    case F:
                        App.world.fix();
                        break;
                    case D:
                        App.world.selectNeighbors();
                        break;
                    case V:
                        App.world.flipVertecNameVisible();
                        break;
                    case S:
                        App.world.editVertexName();
                        break;
                }
            } else if (App.renderer.mode == Renderer.Mode.CREDITS || App.renderer.mode == Renderer.Mode.HELP) {
                if (keycode == ESCAPE) {
                    App.renderer.mode = Renderer.Mode.GRAPH;
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case CONTROL_LEFT:
                ctrl = false;
                break;
            case SHIFT_LEFT:
                shift = false;
                break;
            case M:
                m = false;
                break;
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (App.renderer.mode == Renderer.Mode.GRAPH) {
            App.world.zoom(amount);
        }
        return false;
    }

    private void setCursorMode(CursorMode m) {
        switch (m) {
            case NORMAL:
                cursorMode = CursorMode.NORMAL;
                //Gdx.app.log(TAG, "normal mode");
                break;
            case MOVE:
                cursorMode = CursorMode.MOVE;
                //Gdx.app.log(TAG, "move mode");
                break;
            case DRAG:
                cursorMode = CursorMode.DRAG;
                //Gdx.app.log(TAG, "drag mode");
                break;
            case SELECTION:
                cursorMode = CursorMode.SELECTION;
                //Gdx.input.setCursorCatched(true);
                for (Vertex vertex:App.world.graph.vertices) {
                    if (shift || ctrl) {
                        vertex.prevSelected = vertex.selected;
                    } else {
                        vertex.selected = false;
                        vertex.prevSelected = false;
                    }
                }
                //Gdx.app.log(TAG, "selection mode");
                break;
            case BUTTON:
                cursorMode = CursorMode.BUTTON;
                //Gdx.app.log(TAG, "button mode");
                break;
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

}
