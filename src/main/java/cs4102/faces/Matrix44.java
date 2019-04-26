package cs4102.faces;

import org.la4j.Matrix;
import org.la4j.matrix.DenseMatrix;
import org.la4j.matrix.dense.Basic2DMatrix;

public class Matrix44 extends Basic2DMatrix {

    public Matrix44(double[][] values) {
        super(values);
        if(rows != 4 || columns != 4) {
            throw new IllegalArgumentException();
        }
    }


    @Override
    public Matrix multiply(Matrix that) {
        if(that.rows() == 4 && that.columns() == 4) {
            double[][] result = new double[][] {
                    new double[4],
                    new double[4],
                    new double[4],
                    new double[4]
            };

            for(int i = 0; i < 4; ++i) {
                for(int j = 0; j < 4; ++j) {
                    double val = 0;
                    for(int k = 0; k < 4; ++k) {
                        val += this.get(i,k) * that.get(k,j);
                    }
                    result[i][j] = val;
                }
            }

            return new Matrix44(result);
        }
        if(that.rows() == 4 && that.columns() == 1) {
            double[][] result = new double[][] {
                    new double[1],
                    new double[1],
                    new double[1],
                    new double[1]
            };

            for(int i = 0; i < 4; ++i) {
                for(int j = 0; j < 1; ++j) {
                    double val = 0;
                    for(int k = 0; k < 4; ++k) {
                        val += this.get(i,k) * that.get(k,j);
                    }
                    result[i][j] = val;
                }
            }

            return Basic2DMatrix.from2DArray(result);
        }
        else {
            return super.multiply(that);
        }
    }
}
