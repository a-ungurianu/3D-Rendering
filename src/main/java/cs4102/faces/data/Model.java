package cs4102.faces.data;

import java.util.List;

public class Model {

    private final int index;
    Mesh mesh;
    List<Vertex> vertices;

    public Model(int index, Mesh mesh, List<Vertex> vertices) {
        this.index = index;
        this.mesh = mesh;
        this.vertices = vertices;
    }

    public List<Triangle> getTriangles() {
        return mesh.getTriangles(vertices);
    }
}
