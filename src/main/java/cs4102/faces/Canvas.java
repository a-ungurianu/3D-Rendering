package cs4102.faces;

import cs4102.faces.data.FaceRepository;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.sqrt;

public class Canvas extends JPanel {

    private final FaceRepository repository;
    private final FaceCompositor composer;
    private double scale = 1.0 / 600;

    private Dimension size = new Dimension(1000, 500);
    private List<Vector> compositionTriangle;

    private int indexToChange = 0;
    private List<Integer> triangleFaceIndices = new ArrayList<>(Arrays.asList(0,1,2));

    Canvas(FaceRepository repository) {
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.setOpaque(true);
        this.requestFocus();
        this.repository = repository;
        this.composer = new FaceCompositor(repository);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Vector weights = computeWeight(mouseEvent.getX(), mouseEvent.getY());

                composer.setWeight(triangleFaceIndices.get(0), Math.max(weights.get(0), 0));
                composer.setWeight(triangleFaceIndices.get(1), Math.max(weights.get(1), 0));
                composer.setWeight(triangleFaceIndices.get(2), Math.max(weights.get(2), 0));
                repaint();
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_RIGHT: {
                        int newIdx = (triangleFaceIndices.get(indexToChange) + 1) % repository.faceCount();
                        while (triangleFaceIndices.contains(newIdx)) {
                            newIdx = (newIdx + 1) % repository.faceCount();
                        }
                        triangleFaceIndices.set(indexToChange, newIdx);
                        repaint();
                    }break;
                    case KeyEvent.VK_LEFT: {
                        int newIdx = (repository.faceCount() + triangleFaceIndices.get(indexToChange) - 1) % repository.faceCount();
                        while (triangleFaceIndices.contains(newIdx)) {
                            newIdx = (repository.faceCount() + newIdx - 1) % repository.faceCount();
                        }
                        triangleFaceIndices.set(indexToChange, newIdx);
                        repaint();
                    } break;
                    case KeyEvent.VK_D: {
                        indexToChange = (indexToChange + 1 ) % 3;
                        repaint();
                    } break;
                    case KeyEvent.VK_A: {
                        indexToChange = (3 + indexToChange - 1) % 3;
                        repaint();
                    } break;
                }
            }
        });
    }

    private Vector computeWeight(int mouseX, int mouseY) {
        Vector v1 = compositionTriangle.get(0);
        Vector v2 = compositionTriangle.get(1);
        Vector v3 = compositionTriangle.get(2);
        double w1 = ((v2.get(1) - v3.get(1))*(mouseX - v3.get(0)) + (v3.get(0) - v2.get(0))*(mouseY - v3.get(1))) /
                    ((v2.get(1) - v3.get(1))*(v1.get(0) - v3.get(0)) + (v3.get(0) - v2.get(0))* (v1.get(1) - v3.get(1)));

        double w2 = ((v3.get(1) - v1.get(1))*(mouseX - v3.get(0)) + (v1.get(0) - v3.get(0)) * (mouseY - v3.get(1)))/
                    ((v2.get(1) - v3.get(1))*(v1.get(0) - v3.get(0)) + (v3.get(0) - v2.get(0))* (v1.get(1) - v3.get(1)));

        double w3 = 1 - w1 - w2;

        return new BasicVector(new double[]{w1, w2, w3});
    }


    @Override
    public Dimension getPreferredSize() {
        return size;
    }



    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        long start = System.nanoTime();

        Graphics2D g2d = (Graphics2D) graphics;

        Renderer renderer = new Renderer(g2d);

        renderer.translate(size.width / 2, size.height/ 2);
        renderer.scale(1,-1);

        renderer.pushMatrix();
        renderer.rotateX(Double.parseDouble(App.config.getProperty("rotateX","0")));
        renderer.rotateY(Double.parseDouble(App.config.getProperty("rotateY","0")));
        renderer.rotateZ(Double.parseDouble(App.config.getProperty("rotateZ","0")));
        renderer.translate(250, 0);
        renderer.translate(Double.parseDouble(App.config.getProperty("translateX","0")),
                           Double.parseDouble(App.config.getProperty("translateY", "0")),
                           Double.parseDouble(App.config.getProperty("translateZ","0")));

        renderer.scale(BasicVector.fromArray(new double[]{scale, scale, scale}));
        renderer.scale(Double.parseDouble(App.config.getProperty("scaleX","1")),
                       Double.parseDouble(App.config.getProperty("scaleY", "1")),
                       Double.parseDouble(App.config.getProperty("scaleZ","1")));

        renderer.drawModel(composer.generateFace());

        renderer.popMatrix();

        renderer.pushMatrix();

        renderer.translate(-250, 0);

        float triangleSize = 200;

        List<Vector> triangle = Arrays.asList(new Vector4(0, sqrt(3) * triangleSize / 4),
                new Vector4(-triangleSize / 2, -sqrt(3) * triangleSize / 4),
                new Vector4(triangleSize / 2, -sqrt(3) * triangleSize / 4));

        this.compositionTriangle = triangle.stream().map(renderer::toScreen).collect(Collectors.toList());

        renderer.fillPolygon(Color.gray, triangle);

        for (int i = 0; i < triangle.size(); ++i) {
            Vector vector = triangle.get(i);
            renderer.pushMatrix();
            if(i == 0) {
                renderer.translate(vector.multiply(1.5));
            }else {
                renderer.translate(vector.multiply(1.3));
            }
            renderer.pushMatrix();
            renderer.scale(scale/ 5, scale / 5 ,scale / 5);
            renderer.drawModel(repository.getFace(triangleFaceIndices.get(i)));
            renderer.popMatrix();
            renderer.translate(vector.multiply(0.5));
            if(i == indexToChange) {
                renderer.drawText(Color.black, BasicVector.constant(2,0), "[" + triangleFaceIndices.get(i) + "]");
            }
            else {
                renderer.drawText(Color.black, BasicVector.constant(2,0), Integer.toString(triangleFaceIndices.get(i)));
            }
            renderer.popMatrix();
        }

        renderer.popMatrix();

        System.out.printf("Time to render: %.2f ms\n", (System.nanoTime() - start) / 1000000f);


    }

}
