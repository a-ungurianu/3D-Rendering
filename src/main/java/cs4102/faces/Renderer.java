package cs4102.faces;

import cs4102.faces.data.Model;
import cs4102.faces.data.Triangle;
import jdk.nashorn.internal.ir.ForNode;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class Renderer {

    private final Graphics2D g;

    private final Vector light = new BasicVector(new double[]{0,0,1});

    private Matrix projectionMatrix = Basic2DMatrix.from2DArray(new double[][] {
            new double[]{1,0,0,0},
            new double[]{0,1,0,0},
            new double[]{0,0,1/10f,0},
            new double[]{0,0,0,1}
    });

    private Matrix transformMatrix = Matrix.identity(4);

    private Stack<Matrix> matrixStack = new Stack<>();

    Renderer(Graphics2D g) {
        this.g = g;
    }

    public void applyTransform(Matrix transform) {
        transformMatrix = transformMatrix.multiply(transform);
    }

    private double getOrDefault(Vector v, int idx, double defaultValue) {
        return v.length() > idx ? v.get(idx) : defaultValue;
    }

    public void translate(Vector t) {

        applyTransform(Basic2DMatrix.from2DArray( new double[][]{
                new double[]{1, 0, 0, getOrDefault(t, 0,0)},
                new double[]{0, 1, 0, getOrDefault(t, 1,0)},
                new double[]{0, 0, 1, getOrDefault(t, 2,0)},
                new double[]{0, 0, 0, 1}
        }));
    }

    public void scale(Vector scale) {
       scale(getOrDefault(scale,0,1), getOrDefault(scale,1,1), getOrDefault(scale,2,1));
    }

    public void drawModel(Model model) {
        List<Triangle> sortedTriangles =  model.getTriangles().stream()
                                                  .map(triangle -> triangle.applyTransformation(transformMatrix))
                                                  .collect(Collectors.toList());

        Comparator<Triangle> triangleComparator = Comparator.comparingDouble(t -> (t.getShape().get(0).get(2) +
                t.getShape().get(1).get(2) +
                t.getShape().get(2).get(2)) / 3);

        sortedTriangles.sort(triangleComparator);

        Vector lightNorm = light.multiply(-1);
        for (Triangle triangle : sortedTriangles) {

            Vector norm = triangle.getNormal();
            float brightness = (float) Double.max(lightNorm.innerProduct(norm), lightNorm.innerProduct(norm.multiply(-1)));
            drawPolygonOnScreen(Utils.darker(triangle.getAverageColour(), brightness), triangle.getShape());
        }
    }

    private void drawPolygonOnScreen(Color color, List<Vector> shape){
        Polygon polygon = new Polygon();
        for(Vector v:shape) {
            Vector screenV = Utils.transform(projectionMatrix, v);
            polygon.addPoint((int)screenV.get(0), (int)screenV.get(1));
        }


        g.setColor(color);
        g.fillPolygon(polygon);
    }

    private Vector transformVector(Vector v) {
        return Utils.transform(transformMatrix,v);
    }

    public void pushMatrix() {
        matrixStack.push(transformMatrix);
    }

    public void popMatrix() {
        transformMatrix = matrixStack.pop();
    }

    public Vector toScreen(Vector v) {
        return Utils.transform(projectionMatrix, Utils.transform(transformMatrix, v));
    }

    public void rotateZ(double angleInDegrees) {
        double theta = toRadians(angleInDegrees);

        applyTransform(Basic2DMatrix.from2DArray(new double[][]{
                new double[]{cos(theta), -sin(theta), 0, 0},
                new double[]{sin(theta), cos(theta), 0, 0},
                new double[]{0,0,1,0},
                new double[]{0,0,0,1}
        }));
    }

    public void rotateY(double angleInDegrees) {
        double theta = toRadians(angleInDegrees);

        applyTransform(Basic2DMatrix.from2DArray(new double[][] {
                new double[]{cos(theta), 0, sin(theta), 0},
                new double[]{0, 1, 0, 0},
                new double[]{-sin(theta), 0, cos(theta), 0},
                new double[]{0,0,0,1}
        }));
    }

    public void rotateX(double angleInDegrees) {
        double theta = toRadians(angleInDegrees);

        applyTransform(Basic2DMatrix.from2DArray(new double[][]{
                new double[]{1,0,0,0},
                new double[]{0,cos(theta), -sin(theta), 0},
                new double[]{0,sin(theta), cos(theta), 0},
                new double[]{0,0,0,1},
        }));
    }

    public void fillPolygon(Color color, List<Vector> shape) {
        drawPolygonOnScreen(color,shape.stream().map(this::transformVector).collect(Collectors.toList()));
    }

    public void translate(double x, double y) {
        translate(BasicVector.fromArray(new double[]{x, y}));
    }

    public void scale(double x, double y) {
        scale(x,y,1);
    }

    public void scale(double x, double y, double z) {
        applyTransform(Basic2DMatrix.from2DArray(new double[][]{
                new double[]{x,0,0,0},
                new double[]{0,y,0,0},
                new double[]{0,0,z,0},
                new double[]{0,0,0,1}
        }));
    }

    public void drawText(Color color, Vector p, String text) {
        Vector screenCoords = toScreen(p);
        g.setFont(new Font("sans-serif", Font.BOLD, 20));
        g.setColor(color);
        g.drawString(text, (int) screenCoords.get(0), (int) screenCoords.get(1));
    }
}
