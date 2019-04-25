package cs4102.faces.data;

import cs4102.faces.Utils;
import org.la4j.Matrix;
import org.la4j.Vector;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class Triangle {

    private final Vertex v1;
    private final Vertex v2;
    private final Vertex v3;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {

        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }



    public Color getAverageColour() {
        int r = (int) ((v1.getColour().get(0) + v2.getColour().get(0) + v3.getColour().get(0)) / 3);
        int g = (int)((v1.getColour().get(1) + v2.getColour().get(1) + v3.getColour().get(1)) /3);
        int b = (int)((v1.getColour().get(2) + v2.getColour().get(2) + v3.getColour().get(1)) /3 );

        return new Color(r,g,b);
    }

    public List<Vector> getShape() {
        return Arrays.asList(v1.getPosition(), v2.getPosition(), v3.getPosition());
    }

    public Vector getNormal() {
        Vector a = v2.getPosition().subtract(v1.getPosition());
        Vector b = v3.getPosition().subtract(v1.getPosition());

        Vector norm = Utils.crossProduct(a,b);

        return norm.divide(Utils.magnitude3D(norm));

    }

    public Triangle applyTransformation(Matrix transform) {
        return new Triangle(
                v1.applyTransformation(transform),
                v2.applyTransformation(transform),
                v3.applyTransformation(transform)
        );
    }
}
