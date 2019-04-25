/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package cs4102.faces;

import cs4102.faces.data.FaceRepository;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;

public class App {

    public static void main(String[] args) throws IOException {

        int noFaces = 100;

        FaceRepository repository = new FaceRepository(Paths.get("data/"), noFaces);

        JFrame frame = new JFrame("Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        Canvas canvas = new Canvas(repository);
        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

//        new Thread(() -> {
//            for(int i = 0; i < noFaces; ++i) {
//
//                final Model face = repository.getFace(i);
//                try {
//                    SwingUtilities.invokeAndWait(() -> canvas.setTriangles(face.getTriangles()));
//                    Thread.sleep(2000);
//                } catch (InterruptedException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).run();

    }
}
