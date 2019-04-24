package cs4102.faces.data;

import java.util.List;
import java.util.stream.Collectors;

public class Mesh {

    private final List<int[]> triangles;

    Mesh(List<int[]> triangles) {
        this.triangles = triangles;
    }

    List<Triangle> getTriangles(List<Vertex> vertices) {
        return triangles.stream().map(indices -> new Triangle(vertices.get(indices[0]-1),
                                                              vertices.get(indices[1]-1),
                                                              vertices.get(indices[2]-1))).collect(Collectors.toList());
    }
}
