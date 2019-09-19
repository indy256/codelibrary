package test.numeric;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue; 
import numeric.Fraction;
import numeric.NewtonInterpolation;
import java.util.Random;

public class TestNewtonInterpolation {
	
	@Test
	void testNewtonInterpolation() {
		int x[] = {1, 2, 3, 6, 8};
		int y[] = {8, 27, 64, 343, 729};
		int dd[] = NewtonInterpolation.getDividedDifferences(x, y, 1000000007);
		for (int i=0; i<4; i++) {
			int v = NewtonInterpolation.interpolate(x, dd, 1000000007, x[i]);
			assertEquals(v, y[i]);
		}
	}
	
	@Test
	void testNewtonInterpolationWithDegreeOfTwo() {
		int x[] = {0, 1, 2, 3};
		int y[] = {1, 4, 9, 16};
		
		int dd[] = NewtonInterpolation.getDividedDifferences(x,  y, 1000000007);
		for (int i=0; i<4; i++) {
			int v = NewtonInterpolation.interpolate(x, dd, 1000000007, x[i]);
			assertEquals(v, y[i]);
		}
	}
	
	@Test
	void testNewtonInterpolationWithDegreeOfOne() {
		int x[] = {0, 1, 2, 3}; 
		int y[] = {1, 2, 3, 4};
		int dd[] = NewtonInterpolation.getDividedDifferences(x,  y, 1000000007);
		for (int i=0; i<4; i++) {
			int v = NewtonInterpolation.interpolate(x, dd, 1000000007, x[i]);
			assertEquals(v, y[i]);
		}
	}
	
	@Test
	void testNewtonInterpolationWithDegreeOfZero() {
		int x[] = {0, 1, 2, 3}; 
		int y[] = {5, 5, 5, 5};
		int dd[] = NewtonInterpolation.getDividedDifferences(x, y, 1000000007);
		for (int i=0; i<4; i++) {
			int v = NewtonInterpolation.interpolate(x, dd, 1000000007, x[i]);
			assertEquals(v, y[i]);
		}
	}
}
