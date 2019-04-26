package cs4102.faces;


import org.la4j.Matrix;
import org.la4j.Vector;

import java.awt.*;

public final class Utils {

    public static Vector crossProduct(Vector a, Vector b) {
        assert a.length() == 3 && b.length() == 3;
        return new Vector4(a.get(1)*b.get(2) - a.get(2)*b.get(1) ,
                                                  -(a.get(0)*b.get(2) - a.get(2)*b.get(0)),
                                                    a.get(0)*b.get(1) - a.get(1)*b.get(0));
    }

    public static Color darker(Color c, float b) {

        return new Color((int)(c.getRed()*b), (int)(c.getGreen()*b), (int)(c.getBlue()*b));
    }

    public static Vector homogenize(Vector v) {
        return new Vector4(v.get(0), v.get(1), v.length() > 2?v.get(2) : 1, 1);
    }

    public static Vector transform(Matrix transformation, Vector v) {
        return Vector4.fromColumnMatrix(transformation.multiply(homogenize(v).toColumnMatrix()));
    }

    public static Vector colorTo1Vector(Color c) {
        return new Vector4(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
    }


    public static double magnitude3D(Vector v) {
        double x = v.get(0);
        double y = v.get(1);
        double z = v.get(2);
        return Math.sqrt(x*x + y*y + z*z);
    }
}
