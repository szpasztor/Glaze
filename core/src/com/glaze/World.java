package com.glaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.glaze.com.graph.types.Camera;
import com.glaze.com.graph.types.Edge;
import com.glaze.com.graph.types.Graph;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Author: Szabolcs Pásztor
 * Date: 9/13/13
 */
public class World {
    private static final String TAG = Inf.TAG + "|World";
    /** The biggest distance from where a vertex can be selected */
    public final static double MAX_SELECT_RANGE = 8;
    /** 1 world unit in screen pixels */
    public Vec2 conv;
    public Graph graph;
    public Camera camera;
    private JFileChooser fileChooser;

    public World() {
        Gdx.app.log(TAG, "Creating World object");
        camera = new Camera(0f, 0f, 120f, 80f);
        graph = new Graph();
        resetGraph();
    }

    public void update() {
        camera.update();
        int x = Gdx.input.getX();
        int y = Renderer.windowHeight-Gdx.input.getY();
        if (App.inputProcessor.m) {
            Vec2 v = new Vec2(App.world.pixToWorld(x, y));
            newVertex(v);
        }
        if (App.inputProcessor.cursorMode == InputProcessor.CursorMode.SELECTION) {
            updateRectSelection(x, y);
        }
    }

    public void open() {
        if (fileChooser == null) {
            Boolean o = UIManager.getBoolean("FileChooser.readOnly");
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);
            File file = new File(World.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String path = file.getPath();
            fileChooser = new JFileChooser(path);
            UIManager.put("FileChooser.readOnly", o);
        }
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            load(file);
            System.out.println("Opening " + file.getName());
        } else {
            System.out.println("Opening cancelled");
        }
        App.inputProcessor.ctrl = false;
        App.inputProcessor.shift = false;
        App.inputProcessor.m = false;
    }

    public void load (File file) {
        resetGraph();
        try {
            FileHandle handle = new FileHandle(file);
            BufferedReader reader = handle.reader(400);
            double width = Double.parseDouble(reader.readLine());
            camera.setWidth(width);
            double height = Double.parseDouble(reader.readLine());
            camera.setHeight(height);
            double cameraX = Double.parseDouble(reader.readLine());
            double cameraY = Double.parseDouble(reader.readLine());
            camera.setPosition(cameraX, cameraY);
            int order = Integer.parseInt(reader.readLine());
            graph.vertices.ensureCapacity(order);
            int size = Integer.parseInt(reader.readLine());
            graph.edges.ensureCapacity(size);
            boolean vertexNameVisible = Boolean.parseBoolean(reader.readLine());
            graph.vertexNameVisible = vertexNameVisible;
            for(int i=0;i<order; i++) {
                float x = Float.parseFloat(reader.readLine());
                float y = Float.parseFloat(reader.readLine());
                float r = Float.parseFloat(reader.readLine());
                String name = reader.readLine();
                graph.vertices.add(new Vertex(x, y, r, name));
            }
            for (int i=0; i<size; i++) {
                int a = Integer.parseInt(reader.readLine());
                int b = Integer.parseInt(reader.readLine());
                graph.edges.add(new Edge(a,b));
                graph.vertices.get(a).neighbors.add(b);
                graph.vertices.get(b).neighbors.add(a);
            }
            for (Vertex vertex : graph.vertices) {
                vertex.calcRadius();
            }
        } catch (IOException e) {
            Gdx.app.log(TAG, "Error while loading graph: " + e.getMessage());
        }
        Gdx.app.log(TAG, "Graph loaded");
        logGraphInf();
    }
    /** Loads an empty glaze */
    public void resetGraph() {
        camera.setWidth(120);
        camera.setHeight(80);
        camera.setPosition(0,0);
        graph.vertices.clear();
        graph.edges.clear();
        Gdx.app.log(TAG, "Graph reset");
        logGraphInf();
    }

    public void save() {
        if (fileChooser == null) {
            Boolean o = UIManager.getBoolean("FileChooser.readOnly");
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);
            File file = new File(World.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String path = file.getPath();
            fileChooser = new JFileChooser(path);
            UIManager.put("FileChooser.readOnly", o);
        }
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            saveFile(file);
            System.out.println("Saving to " + file.getName());
        } else {
            System.out.println("Saving cancelled");
        }
        App.inputProcessor.ctrl = false;
        App.inputProcessor.shift = false;
        App.inputProcessor.m = false;
    }

    public void saveFile(File file) {
        FileHandle handle = new FileHandle(file);
        handle.writeString(Double.toString(camera.width) + "\n" + Double.toString(camera.height) + "\n", false); // camera width, height
        handle.writeString(Double.toString(camera.p0.x) + "\n" + Double.toString(camera.p0.y) + "\n", true); // camera position
        handle.writeString(Integer.toString(graph.vertices.size) + "\n", true); // order
        handle.writeString(Integer.toString(graph.edges.size) + "\n", true); // size
        handle.writeString(Boolean.toString(graph.vertexNameVisible) + "\n", true); // vertex name visibility
        for (Vertex vertex : graph.vertices) {
            handle.writeString(Double.toString(vertex.pos.x) + "\n" // position x
                    + Double.toString(vertex.pos.y) + "\n" // position y
                    + Double.toString(vertex.r) + "\n" // radius
                    + vertex.name + "\n" // name
                    ,true);
        }
        for (Edge edge : graph.edges) {
            handle.writeString(Integer.toString(edge.a) + "\n" // edge a
                    + Integer.toString(edge.b) + "\n" // edge b
                    ,true);
        }
        Gdx.app.log(TAG, "Graph saved");
    }

    public void click (Vec2 v) {
        Vertex closest = null;
        double closestDist = 1000000000;
        for (Vertex vertex : graph.vertices) {
            double x = Math.pow(v.x-vertex.pos.x, 2);
            double y = Math.pow(v.y-vertex.pos.y, 2);
            double distance = Math.sqrt(x+y);
            if (distance < closestDist) {
                closestDist = distance;
                closest = vertex;
            }
        }
        if (App.inputProcessor.ctrl || App.inputProcessor.shift) {
            if (closestDist <= MAX_SELECT_RANGE) {
                closest.selected = !closest.selected;
            }
        } else {
            if (closestDist <= MAX_SELECT_RANGE) {
                for (Vertex vertex : graph.vertices) {
                    if (vertex == closest) {
                        vertex.selected = true;
                    } else {
                        vertex.selected = false;
                    }
                }
            } else {
                for (Vertex vertex : graph.vertices) {
                    vertex.selected = false;
                }
            }
        }
        updateButtonState();
    }

    /**
     * @param x The x position of the mouse in pixels.
     * @param y The y position of the mouse in pixels.
     */
    public void updateRectSelection(int x, int y) {
        for (Vertex vertex : graph.vertices) {
            if (vertex.visible) {
                int x1 = App.inputProcessor.selectionStartX;
                int x2 = x;
                int y1 = App.inputProcessor.selectionStartY;
                int y2 = y;
                Vec2 p0 = pixToWorld(x1<x2?x1:x2, y1<y2?y1:y2);
                Vec2 p2 = pixToWorld(x1<x2?x2:x1, y1<y2?y2:y1);
                Rectangle rectangle = new Rectangle(p0, p2);
                if (Rectangle.contains(rectangle, vertex.pos.x, vertex.pos.y)) {
                    vertex.selected = !vertex.prevSelected;
                } else {
                    vertex.selected = vertex.prevSelected;
                }
            }
        }
        updateButtonState();
    }

    public void all() {
        boolean everything = true;
        for (int i=0;i<graph.vertices.size && everything==true;i++) {
            if (graph.vertices.get(i).selected==false) { everything=false; }
        }
        if (everything) {
            for (Vertex vertex:graph.vertices) { vertex.selected = false; }
        } else {
            for (Vertex vertex:graph.vertices) { vertex.selected = true; }
        }
        updateButtonState();
    }

    /** Adds a new vertex and creates glaze.neighbors from it to the selected glaze.vertices */
    public void newVertex (Vec2 v) {
        graph.vertices.add(new Vertex(v.x, v.y, 0, Integer.toString(graph.vertices.size+1)));
        Vertex newVertex = graph.vertices.get(graph.vertices.size-1);
        int newVertexIndex = graph.vertices.size-1;
        for (int i=0;i<graph.vertices.size;i++) {
            Vertex vertex = graph.vertices.get(i);
            if (vertex.selected) {
                vertex.neighbors.add(newVertexIndex);
                newVertex.neighbors.add(i);
                graph.edges.add(new Edge(newVertexIndex, i));
                vertex.calcRadius();
            }
        }
        newVertex.calcRadius();
        logGraphInf();
    }

    /** Deletes the selected glaze.vertices */
    public void removeVertex() {
        for (int i=0; i<graph.vertices.size; i++) {
            if (graph.vertices.get(i).selected) {
                graph.vertices.removeIndex(i);
                for(int i2=0;i2<graph.edges.size;i2++) {
                    if (graph.edges.get(i2).a == i || graph.edges.get(i2).b == i) {
                        graph.edges.removeIndex(i2);
                        i2--;
                    }
                }
                for(int i2=0;i2<graph.edges.size;i2++) {
                    if (graph.edges.get(i2).a > i) { graph.edges.get(i2).a--; }
                    if (graph.edges.get(i2).b > i) { graph.edges.get(i2).b--; }
                }
                for (int i2=0;i2<graph.vertices.size;i2++) {
                    for(int i3=0;i3<graph.vertices.get(i2).neighbors.size;i3++) {
                        if (graph.vertices.get(i2).neighbors.get(i3) == i) {
                            graph.vertices.get(i2).neighbors.removeIndex(i3);
                        }
                    }
                    for(int i3=0;i3<graph.vertices.get(i2).neighbors.size;i3++) {
                        if (graph.vertices.get(i2).neighbors.get(i3) > i) {
                            graph.vertices.get(i2).neighbors.set(i3, graph.vertices.get(i2).neighbors.get(i3)-1);
                        }
                    }
                }
                i--;
            }
        }
        for (Vertex vertex : graph.vertices) {
            vertex.calcRadius();
        }
        updateButtonState();
        logGraphInf();
    }

    /**
     * Creates or removes all glaze.neighbors between selected glaze.vertices.
     * @param create True -> create edge; false -> removeVertex edge
     */
    public void edge(boolean create) {
        Array<Integer> selected = new Array<Integer>(10);
        for (int i=0;i<graph.vertices.size;i++) {
            if (graph.vertices.get(i).selected) {
                selected.add(i);
            }
        }
        for (int i=0;i<selected.size;i++) {
            for (int i2=i+1;i2<selected.size;i2++) {
                changeEdge(selected.get(i), selected.get(i2), create);
            }
        }
        for (Integer i:selected) {
            graph.vertices.get(i).calcRadius();
        }
        logGraphInf();
    }

    /** Creates/removes an edge between the two parameter glaze.vertices.
     * @param a The index of the first vertex (in glaze.vertices array).
     * @param b The index of the second vertex (in glaze.vertices array).
     * @param create True -> create edge; false -> removeVertex edge
     * @return
     */
    public void changeEdge(int a, int b, boolean create) {
        if (create) {
            boolean e = false;
            for (int i=0;i<graph.edges.size;i++) {
                if ((graph.edges.get(i).a == a && graph.edges.get(i).b == b) || (graph.edges.get(i).a == b && graph.edges.get(i).b == a)) {
                    e = true;
                    break;
                }
            }
            if (!e) {
                graph.vertices.get(a).neighbors.add(b);
                graph.vertices.get(b).neighbors.add(a);
                graph.edges.add(new Edge(a,b));
            }
        } else {
            for (int i=0;i<graph.edges.size;i++) {
                if ((graph.edges.get(i).a == a && graph.edges.get(i).b == b) || (graph.edges.get(i).a == b && graph.edges.get(i).b == a)) {
                    graph.edges.removeIndex(i);
                    graph.vertices.get(a).neighbors.removeValue(b, false);
                    graph.vertices.get(b).neighbors.removeValue(a, false);
                    break;
                }
            }
        }
    }

    /** Turns selected non-fixed glaze.vertices to fixed ones, fixed glaze.vertices to non-fixed ones. */
    public void fix() {
        for (Vertex vertex : graph.vertices) {
            if (vertex.selected) {
                vertex.fixed = !vertex.fixed;
            }
        }
    }

    /** Selects selected vertices' neighbors */
    public void selectNeighbors() {
        for (Vertex vertex : graph.vertices) {
            vertex.tmp = false;
        }
        for (Vertex vertex : graph.vertices) {
            if (vertex.selected) {
                vertex.tmp = true;
                for (Integer i : vertex.neighbors) {
                    graph.vertices.get(i).tmp = true;
                }
            }
        }
        for (Vertex vertex : graph.vertices) {
            vertex.selected = vertex.tmp;
        }
        updateButtonState();
    }

    public void zoom(int amount) {
        if (amount == 1) {
            App.world.camera.scale(1.1f, 1.1f);
        } else if (amount == -1) {
            if (App.world.camera.width>1.0E-4 && App.world.camera.height>6.68E-4) {
                App.world.camera.scale(0.9f, 0.9f);
            }
        }
    }

    /** Turns the visibility of buttons depending on the number of selected vertices */
    public void updateButtonState() {
        Renderer renderer = App.renderer;
        int n = 0;
        for(Vertex vertex : graph.vertices) {
            if (vertex.selected) { n++; }
        }
        if (n==0) {
            renderer.editName.turnedOn = false;
            renderer.editNameVisible = false;
            renderer.removeVertex.turnedOn = false;
            renderer.fix.turnedOn = false;
            renderer.selectNeighbors.turnedOn = false;
            renderer.makeEdge.turnedOn = false;
            renderer.removeEdge.turnedOn = false;
        } else if (n==1) {
            renderer.editNameVisible = true;
            renderer.editName.turnedOn = true;
            renderer.removeVertex.turnedOn = true;
            renderer.fix.turnedOn = true;
            renderer.selectNeighbors.turnedOn = true;
            renderer.makeEdge.turnedOn = false;
            renderer.removeEdge.turnedOn = false;
        } else if (n>1) {
            renderer.editNameVisible = true;
            renderer.editName.turnedOn = false;
            renderer.removeVertex.turnedOn = true;
            renderer.fix.turnedOn = true;
            renderer.selectNeighbors.turnedOn = true;
            renderer.makeEdge.turnedOn = true;
            renderer.removeEdge.turnedOn = true;
        }
    }

    public void logGraphInf() {
        Gdx.app.log(TAG, "order = "+graph.vertices.size+", size = "+graph.edges.size);
        /*Gdx.app.log(TAG, "glaze.vertices: ");
        for (int i=0;i<glaze.vertices.size;i++) {
            Gdx.app.log(TAG, "Vertex "+i+": degree = "+);
        }*/
    }

    /** Converts a world coordinate to a screen coordinate */
    public Vec2 worldToPix(Vec2 vec2) {
        Vec2 p = new Vec2(vec2);
        p.sub(camera.p0);
        p.mul(conv);
        return p;
    }

    /** Converts a screen pixel coordinate to a world coordinate */
    public Vec2 pixToWorld(int x, int y) {
        Vec2 p = new Vec2(x, y);
        p.div(conv);
        p.add(camera.p0);
        return p;
    }

    /** Updates the conversion vector (1 world unit to pixels).
     * Should be called every time the camera's or the window's width or height has changed.
     */
    public void updateConversionVect() {
        Vec2 r = Vec2.sub(camera.p2, camera.p0);
        Vec2 s = new Vec2(Renderer.windowWidth, Renderer.windowHeight);
        conv = Vec2.div(s, r);
        camera.latestConvVectWH.set(r);
    }

    public void flipVertecNameVisible() {
        graph.vertexNameVisible = !graph.vertexNameVisible;
    }

    public void editVertexName() {
        Vertex selected = null;
        for (Vertex vertex : graph.vertices) {
            if (vertex.selected) {
                if (selected == null) {
                    selected = vertex;
                } else return;
            }
        }
        if (selected == null) return;
        String s = JOptionPane.showInputDialog(null, null, "Edit name", JOptionPane.PLAIN_MESSAGE);
        if ((s != null) && (s.length() > 0)) {
            selected.name = s;
        }
        App.inputProcessor.ctrl = false;
        App.inputProcessor.shift = false;
        App.inputProcessor.m = false;
    }

}
