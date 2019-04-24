package cs4102.faces;


import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import java.awt.*;

public final class Utils {

    public static Vector crossProduct(Vector a, Vector b) {
        assert a.length() == 3 && b.length() == 3;
        return BasicVector.fromArray(new double[]{  a.get(1)*b.get(2) - a.get(2)*b.get(1) ,
                                                  -(a.get(0)*b.get(2) - a.get(2)*b.get(0)),
                                                    a.get(0)*b.get(1) - a.get(1)*b.get(0)});
    }

    public static Color darker(Color c, float b) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]*b);
    }
}
