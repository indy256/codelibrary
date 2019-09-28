package JunitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import sort.Sort;

class test {

	@Test
	void test1() {
		Sort sort = new Sort();
		int[] a = {0, 1, 2, 3, 4, 5};
		int[] b = {1, 4, 0, 3, 2, 5};
		
		int low = 0;
		int high = 5;
		sort.qSort(b, low, high);
		assertEquals(a, b);
	}
	
	@Test
	void test2() {
		Sort sort = new Sort();
		int[] a = {0, 1, 2, 3, 4, 5};
		int[] b = {6, 4, 0, 3, 5, 2};
		
		int low = 0;
		int high = 5;
		sort.qSort(b, low, high);
		assertEquals(a, b);
	}
	
	@Test
	void test3() {
		Sort sort = new Sort();
		int[] a = {0, 1, 2, 3, 4, 5};
		int[] b = {1, 4, 0, 3, 5, 6};
		
		int low = 0;
		int high = 5;
		sort.qSort(b, low, high);
		assertEquals(a, b);
	}
	
	@Test
	void test4() {
		Sort sort = new Sort();
		int[] a = {0, 1, 2, 3, 4, 5};
		int[] b = {1, 4, 0, 3, 5, 2};
		
		int low = 3;
		int high = 2;
		sort.qSort(b, low, high);
		assertEquals(a, b);
	}

}
