package cs4102.faces;

import org.la4j.Vector;
import org.la4j.vector.DenseVector;

public class Vector4 extends DenseVector {
    public double x,y,z,w;

    public Vector4(double x, double y, double z, double w) {
        super(4);
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4(double x, double y, double z) {
        super(3);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector4(double x, double y) {
        super(2);
        this.x = x;
        this.y = y;
    }

    public Vector4(Vector row) {
        super(Math.max(row.length(), 4));

        int length = row.length();
        this.x = length > 0 ?row.get(0) : 0;
        this.y = length > 1 ? row.get(1) : 0;
        this.z = length > 2 ? row.get(2) : 0;
        this.w = length > 3 ? row.get(3) : 0;
    }


    @Override
    public double get(int i) {
        switch (i) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            case 3: return w;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void set(int i, double value) {
        switch(i) {
            case 0:
                x = value;
                break;
            case 1:
                y = value;
                break;
            case 2:
                z = value;
                break;
            case 3:
                w = value;
                break;
        }
    }

    @Override
    public Vector blankOfLength(int length) {
        return new Vector4(0,0,0,0);
    }

    @Override
    public Vector copyOfLength(int length) {
        return new Vector4(x,y,z,w);
    }

    @Override
    public byte[] toBinary() {
        return new byte[0];
    }

    @Override
    public double norm() {
        return Math.sqrt(x*x + y*y + z*z + w*w);
    }

    @Override
    public Vector subtract(Vector that) {
        return new Vector4(x - that.get(0), y - that.get(1), z - that.get(2), w - that.get(3));
    }

    @Override
    public Vector multiply(double value) {
        return new Vector4(x * value, y* value, z* value, w * value);
    }

    @Override
    public double[] toArray() {
        return new double[0];
    }
}
