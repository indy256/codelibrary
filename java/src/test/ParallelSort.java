package test;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Andrey Naumenko
 */
public class ParallelSort {

	public static void main(String[] args) {
		int n = 30_000_000;
		double[] a = new double[n];
		Random rnd = new Random(1);
		for (int i = 0; i < n; i++)
			a[i] = rnd.nextDouble();
		double[] b1 = a.clone();
		long time = System.currentTimeMillis();
		Arrays.sort(b1);
		System.out.println(System.currentTimeMillis() - time);
		double[] b2 = a.clone();
		time = System.currentTimeMillis();
//		Arrays.parallelSort(b2);
		System.out.println(System.currentTimeMillis() - time);
	}
}
