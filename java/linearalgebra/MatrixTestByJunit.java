package linearalgebra;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class MatrixTestByJunit {
    @Test
    public void matrixPowTest1() {
        int p = 0;
        int[][] matrix = Matrix.matrixUnit(3);
        int[][] a = new int[3][3];
        assertArrayEquals(matrix, Matrix.matrixPow(a, p));
    }

    @Test
    public void matrixPowTest2() {
        int p = 1;
        int[][] a = {{1, 2}, {4, 3}};
        assertArrayEquals(a, Matrix.matrixPow(a, p));
    }

    @Test
    public void matrixPowTest3() {
        int p = 2;
        int[][] a = {{1, 2}, {2, 3}};
        int[][] matrix = {{5, 8}, {8, 13}};
        assertArrayEquals(matrix, Matrix.matrixPow(a, p));
    }

    @Test
    public void matrixPowTest4() {
        int p = -1;
        int[][] a = {{2, 3}, {3, 4}};
        int[][] matrix = {{-4, 3}, {3, -2}};
        assertArrayEquals(matrix, Matrix.matrixPow(a, p));
    }

}
