package cs4102.faces.data;

import java.util.List;

public class Model {

    Mesh mesh;
    List<Vertex> vertices;

    public Model(Mesh mesh, List<Vertex> vertices) {
        this.mesh = mesh;
        this.vertices = vertices;
    }

    public List<Triangle> getTriangles() {
        return mesh.getTriangles(vertices);
    }
}
