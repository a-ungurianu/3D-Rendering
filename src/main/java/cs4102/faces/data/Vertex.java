package cs4102.faces.data;

import cs4102.faces.Utils;
import org.la4j.Matrix;
import org.la4j.Vector;

public class Vertex {

    private Vector position;

    private Vector colour;


    public Vertex(Vector position, Vector colour) {
        this.position = position;
        this.colour = colour;
    }


    public Vector getPosition() {
        return position;
    }

    public Vertex applyTransformation(Matrix transformation) {
        return new Vertex(Utils.transform(transformation, position),colour);
    }

    public Vector getColour() {
        return colour;
    }
}
