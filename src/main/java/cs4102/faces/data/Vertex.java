package cs4102.faces.data;

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

    public Vector getColour() {
        return colour;
    }
}
