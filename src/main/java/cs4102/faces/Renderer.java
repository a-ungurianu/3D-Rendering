package cs4102.faces;

import cs4102.faces.data.Model;
import cs4102.faces.data.Triangle;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class Renderer {

    private final Graphics2D g;

    private final Vector ambient = new Vector4(BasicVector.fromCSV(App.config.getProperty("ambientColor", "0.2, 0.2, 0.2")));
    private final Vector directLight = new Vector4(BasicVector.fromCSV(App.config.getProperty("directLightDirection","0,0,1")));
    private final Vector lightColour =new Vector4(BasicVector.fromCSV(App.config.getProperty("directLightColor","1,1,1")));

    private Matrix projectionMatrix = new Matrix44(new double[][] {
            new double[]{1,0,0,0},
            new double[]{0,1,0,0},
            new double[]{0,0,0,0},
            new double[]{0,0,0,1}
    });

    private Matrix transformMatrix = new Matrix44(new double[][] {
            new double[]{1,0,0,0},
            new double[]{0,1,0,0},
            new double[]{0,0,1,0},
            new double[]{0,0,0,1}
    });

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

        applyTransform(new Matrix44( new double[][]{
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

        Vector lightNorm = directLight.divide(directLight.norm()).multiply(-1);
        for (Triangle triangle : sortedTriangles) {

            Vector norm = triangle.getNormal();
            Color c = triangle.getAverageColour();


            Vector light = ambient.add(lightColour.multiply(lightNorm.innerProduct(norm)));

            Color finalColor = new Color(
                    Math.max(0, Math.min(255,(int)(c.getRed() * light.get(0)))),
                    Math.max(0, Math.min(255,(int)(c.getGreen() * light.get(1)))),
                    Math.max(0, Math.min(255,(int)(c.getBlue() * light.get(2))))
            );


            drawPolygonOnScreen(finalColor, triangle.getShape());
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

        applyTransform(new Matrix44(new double[][]{
                new double[]{cos(theta), -sin(theta), 0, 0},
                new double[]{sin(theta), cos(theta), 0, 0},
                new double[]{0,0,1,0},
                new double[]{0,0,0,1}
        }));
    }

    public void rotateY(double angleInDegrees) {
        double theta = toRadians(angleInDegrees);

        applyTransform(new Matrix44(new double[][] {
                new double[]{cos(theta), 0, sin(theta), 0},
                new double[]{0, 1, 0, 0},
                new double[]{-sin(theta), 0, cos(theta), 0},
                new double[]{0,0,0,1}
        }));
    }

    public void rotateX(double angleInDegrees) {
        double theta = toRadians(angleInDegrees);

        applyTransform(new Matrix44(new double[][]{
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
        applyTransform(new Matrix44(new double[][]{
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

    public void translate(double x, double y, double z) {
        translate(new Vector4(x,y,z));
    }
}
