package cs4102.faces;

import cs4102.faces.data.Model;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;

public class Canvas extends JPanel implements MouseWheelListener {


    private final Model model;
    private double scale = 1.0 / 500;

    private Dimension size = new Dimension(500, 500);

    private Vector translate = BasicVector.fromArray(new double[]{size.width / 2.0, size.height / 2.0});

    Canvas(Model model) {
        this.model = model;
        this.addMouseWheelListener(this);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                repaint();
            }
        });
    }



    @Override
    public Dimension getPreferredSize() {
        return size;
    }



    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Renderer renderer = new Renderer(graphics);

        renderer.pushMatrix();

        renderer.scale(BasicVector.fromArray(new double[]{scale, scale, scale}));
        renderer.translate(translate);

        renderer.drawModel(model);

        renderer.popMatrix();


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
