package string;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class testSort {
	@Test
	public void sortTest1() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = 0;
		int high = 5;
		int[] b = {1,2,2,3,4,5};
		st.mergeSort(a, low, high);
		assertArrayEquals(b,a);
	}
	@Test
	public void sortTest2() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = 1;
		int high = 5;
		int[] b = {2,1,2,3,4,5};
		st.mergeSort(a, low, high);
		assertArrayEquals(b, a);
	}
	@Test
	public void sortTest3() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = 1;
		int high = 6;
		int[] b = {2,1,2,3,4,5};
		st.mergeSort(a, low, high);
		assertArrayEquals(b, a);
	}
	@Test
	public void sortTest4() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = -1;
		int high = 5;
		int[] b = {1,2,2,3,4,5};
		st.mergeSort(a, low, high);
		assertArrayEquals(b, a);
	}
	@Test
	public void sortTest5() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = -1;
		int high = 6;
		int[] b = {1,2,2,3,4,5};
		st.mergeSort(a, low, high);
		assertArrayEquals(b, a);
	}
	@Test
	public void sortTest6() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = 0;
		int high = 4;
		int[] b = {1,2,2,3,5,4};
		st.mergeSort(a, low, high);
		assertArrayEquals(b, a);
	}
	@Test
	public void sortTest7() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = -1;
		int high = 4;
		int[] b = {1,2,2,3,5,4};
		st.mergeSort(a, low, high);
		assertArrayEquals(b, a);
	}
	@Test
	public void sortTest8() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = 1;
		int high = 4;
		int[] b = {2,1,2,3,5,4};
		st.mergeSort(a, low, high);
		assertArrayEquals(b, a);
	}
	@Test
	public void sortTest9() {
		Sort st = new Sort();
		int[] a = {2,1,3,2,5,4};
		int low = 0;
		int high = 6;
		int[] b = {1,2,2,3,4,5};
		st.mergeSort(a, low, high);
		assertArrayEquals(b, a);
	}
}
