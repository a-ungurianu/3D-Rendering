package cs4102.faces;

import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.DenseVector;

import java.awt.dnd.InvalidDnDOperationException;

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

    public static Vector4 fromColumnMatrix(Matrix columnMatrix) {
        return new Vector4(columnMatrix.get(0,0),
                           columnMatrix.get(1,0),
                           columnMatrix.get(2,0),
                           columnMatrix.get(3,0)
                );
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
        if(that.length() != this.length()) throw new IllegalArgumentException();

        Vector4 s = new Vector4(x - that.get(0), y - that.get(1), z - that.get(2), w - that.get(3));
        s.length = that.length();
        return s;
    }

    @Override
    public Vector multiply(double value) {
        Vector4 m = new Vector4(x * value, y* value, z* value, w * value);
        m.length = this.length;
        return m;
    }

    @Override

    public double innerProduct(Vector that) {
        double res = 0;

        switch(that.length()) {
            case 4:
                res += w * that.get(3);
            case 3:
                res += z * that.get(2);
            case 2:
                res += y * that.get(1);
            case 1:
                res += x * that.get(0);
        }

        return res;
    }

    @Override
    public double[] toArray() {
        return new double[0];
    }


    @Override
    public Matrix toColumnMatrix() {

        double result[][];

        switch(length) {
            case 1:{
                result = new double[][] {
                        new double[]{x}
                };
            }break;
            case 2:{
                result = new double[][] {
                        new double[]{x},
                        new double[]{y}
                };
            }break;
            case 3:{
                result = new double[][] {
                        new double[]{x},
                        new double[]{y},
                        new double[]{z}
                };
            }break;
            default:{
                result = new double[][] {
                        new double[]{x},
                        new double[]{y},
                        new double[]{z},
                        new double[]{w}
                };
            }break;
        }


        return Basic2DMatrix.from2DArray(result);
    }
}
