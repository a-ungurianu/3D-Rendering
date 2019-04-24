package cs4102.faces;

import cs4102.faces.data.Triangle;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Canvas extends JPanel implements MouseWheelListener {


    private double scale = 1.0 / 500;

    private Dimension size = new Dimension(500, 500);

    private Vector translate = BasicVector.fromArray(new double[]{size.width / 2.0, size.height / 2.0});

    private Matrix getTransform() {
        return getTranslate().multiply(getScale());
    }

    private Matrix getScale() {
        return Basic2DMatrix.from2DArray(new double[][]{
                new double[]{scale, 0, 0, 0},
                new double[]{0, -scale, 0, 0},
                new double[]{0, 0, 1, 0},
                new double[]{0, 0, 0, 1}
        });
    }

    private Matrix getTranslate() {
        return Basic2DMatrix.from2DArray( new double[][]{
                new double[]{1, 0, 0, translate.get(0)},
                new double[]{0, 1, 0, translate.get(1)},
                new double[]{0, 0, 1, 0},
                new double[]{0, 0, 0, 1}
        });
    }

    private List<Triangle> triangles;

    Canvas(List<Triangle> triangles) {
        this.triangles = triangles;

        this.addMouseWheelListener(this);


    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    @Override
    protected void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);

        Matrix transform = getTransform();

        triangles.sort(Comparator.comparingDouble(t -> (t.getShape().get(0).get(2) + t.getShape().get(1).get(2) + t.getShape().get(2).get(2))/3));
        for(Triangle t:triangles) {
            drawTriangle(graphics, t, transform);
        }
    }

    private static Vector homogenize(Vector v) {
        Vector homogeneous = v.copyOfLength(4);

        homogeneous.set(3, 1);

        return homogeneous;
    }

    private static void drawPolygon(Graphics g, Color color, List<Vector> shape){
        int[] xs = shape.stream().mapToInt(v -> (int)v.get(0)).toArray();
        int[] ys = shape.stream().mapToInt(v -> (int)v.get(1)).toArray();



        g.setColor(color);
        g.fillPolygon(xs, ys, 3);
    }

    private void drawTriangle(Graphics g, Triangle t, Matrix transform) {
        List<Vector> shape = t.getShape();

        List<Vector> transformed = shape.stream().map(Canvas::homogenize)
                                                 .map(v -> transform.multiply(v.toColumnMatrix()))
                                                 .map(Matrix::toColumnVector)
                                                 .collect(Collectors.toList());
        Vector light = BasicVector.fromArray(new double[]{0,0,-1});

        light = light.divide(light.norm());

        Vector norm = t.getNormal();

        float brightness = (float) Double.max(light.innerProduct(norm), 0);

        drawPolygon(g, Utils.darker(t.getAverageColour(), brightness), transformed);

    }

    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
        this.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {

        if(mouseWheelEvent.getUnitsToScroll() > 0) {
            scale *= 1.1;
        }
        else {
            scale /= 1.1;
        }

        this.repaint();
    }
}
