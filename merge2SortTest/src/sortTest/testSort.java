package sortTest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class testSort {
	@Test
	public void sortTest1() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = 3,high = 4;
		int[] b = {2,1,3,2,5,4};
		st.mergeSort2(a, low, high);
		assertArrayEquals(b,a);
	}
	
	@Test
	public void sortTest2() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = 2,high = 4;
		int[] b = {2,1,2,3,5,4};
		st.mergeSort2(a, low, high);
		assertArrayEquals(b,a);
	}
}
