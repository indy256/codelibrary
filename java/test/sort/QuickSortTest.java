package test.sort;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import sort.Quicksort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import java.util.Random;

class QuickSortTest {

	 private int[] numbers;
	    private final static int SIZE = 10000;
	    private final static int MAX = 100000;

	    @BeforeEach
	    public  void setUp() throws Exception {
	        numbers = new int[SIZE];
	        Random generator = new Random();
	        for (int i = 0; i < numbers.length; i++) {
	            numbers[i] = generator.nextInt(MAX);
	        }
	    }

	    @Test
	    public void testNull() {
	        Quicksort.sort(null);
	    }

	    @Test
	    public void testEmpty() {
	    	int[] test = new int[1];
	    	Quicksort.sort(test);
	    }

	    @Test
	    public void testOneElement() {
	        int[] test = new int[1];
	        test[0] = 5;
	        Quicksort.sort(test);
	    }

	    @Test
	    public void testManyDuplicates() {
	        int[] test = {5, 5, 6, 6, 4, 4, 5, 5, 4, 4, 6, 6, 5, 5};
	        Quicksort.sort(test);
	        if (!validate(test)) {
	        	fail("Should not happen");
	        }
	        printResult(test);
	    }

	    @RepeatedTest(10)
	    public void testQuickSort() {
	        for ( int i : numbers) {
	            System.out.println(i + " ");
	        }
	        long startTime = System.currentTimeMillis();

	        Quicksort.sort(numbers);

	        long stopTime = System.currentTimeMillis();
	        long elapsedTime = stopTime - startTime;
	        System.out.println("Quicksort " + elapsedTime);
	        if (!validate(numbers)) {
	            fail("Should not happen");
	        }
	        assertTrue(true);
	    }

	    @RepeatedTest(10)
	    public void testStandardSort() {
	        long startTime = System.currentTimeMillis();
	        Arrays.sort(numbers);
	        long stopTime = System.currentTimeMillis();
	        long elapsedTime = stopTime - startTime;
	        System.out.println("Standard Java sort " + elapsedTime);
	        if (!validate(numbers)) {
	            fail("Should not happen");
	        }
	        assertTrue(true);
	    }

	    private boolean validate(int[] numbers) {
	        for (int i = 0; i < numbers.length - 1; i++) {
	            if (numbers[i] > numbers[i + 1]) {
	                return false;
	            }
	        }
	        return true;
	    }

	    private void printResult(int[] numbers) {
	        for (int i = 0; i < numbers.length; i++) {
	            System.out.print(numbers[i]);
	        }
	        System.out.println();
	    }

}
