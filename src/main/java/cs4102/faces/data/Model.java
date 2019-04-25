package cs4102.faces.data;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;

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

    public Matrix getTextureMatrix() {

        double[][] textures = new double[vertices.size()][];

        for (int i = 0; i < vertices.size(); ++i) {
            Vertex vertex = vertices.get(i);
            Vector p = vertex.getColour();
            textures[i] = new double[]{p.get(0), p.get(1), p.get(2)};
        }

        return Basic2DMatrix.from2DArray(textures);
    }

    public Matrix getPositionMatrix() {

        double[][] positions = new double[vertices.size()][];

        for (int i = 0; i < vertices.size(); ++i) {
            Vertex vertex = vertices.get(i);
            Vector p = vertex.getPosition();
            positions[i] = new double[]{p.get(0), p.get(1), p.get(2)};
        }

        return Basic2DMatrix.from2DArray(positions);
    }
}
