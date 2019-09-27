package optimization;

import static org.junit.Assert.*;

import org.junit.Test;

public class MeetInTheMiddleTest {

	@Test
	public void sumsLowerBoundTest1() {
		long[] a = {-1, 2, 5, 4, 2, 6, 3};
		long result = MeetInTheMiddle.sumsLowerBound(a, 10);
		long expectedResult = 10;
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void sumsLowerBoundTest2() {
		long[] a = {1};
		long result = MeetInTheMiddle.sumsLowerBound(a, -1);
		long expectedResult = -9223372036854775808L;
		assertEquals(expectedResult, result);
	}

}
