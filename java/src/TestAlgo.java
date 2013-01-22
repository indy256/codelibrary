import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

public class TestAlgo extends TestCase {
	static Random rnd = new Random(1);

	public static List<Integer>[] getRandomGraph(int n) {
		List<Integer>[] res = new List[n];

		for (int i = 0; i < n; i++) {
			res[i] = new ArrayList<>();
			for (int j = 0; j < n; j++)
				if (rnd.nextBoolean())
					res[i].add(j);

		}
		return res;
	}

	public void testSCC() {
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(20);
			List<Integer>[] g = getRandomGraph(n);

			List<List<Integer>> scc1 = SCCTransitiveClosure.scc(g);
			List<List<Integer>> scc2 = SCCKosaraju.scc(g);
			List<List<Integer>> scc3 = new SCCTarjan().scc(g);
			List<List<Integer>> scc4 = SCCTarjanNoRecursion.scc(g);

			Set<Set<Integer>> s1 = new HashSet<>();
			for (List<Integer> cmp : scc1)
				s1.add(new HashSet<>(cmp));
			Set<Set<Integer>> s2 = new HashSet<>();
			for (List<Integer> cmp : scc2)
				s2.add(new HashSet<>(cmp));
			Set<Set<Integer>> s3 = new HashSet<>();
			for (List<Integer> cmp : scc3)
				s3.add(new HashSet<>(cmp));
			Set<Set<Integer>> s4 = new HashSet<>();
			for (List<Integer> cmp : scc4)
				s4.add(new HashSet<>(cmp));

			assertEquals(s1, s2);
			assertEquals(s1, s3);
			assertEquals(s1, s4);
		}
	}

	/*
	 * public void testBinaryHeapWithDecreaseKey() { Queue<Integer> ref = new PriorityQueue<Integer>(); int maxSize =
	 * 20000; BinaryHeapWithDecreaseKey<Integer> heap = new BinaryHeapWithDecreaseKey<Integer>(maxSize);
	 * 
	 * for (int steps = 0; steps < maxSize / 2; steps++) { int x = rnd.nextInt(1000); heap.add(steps, x); ref.add(x); }
	 * 
	 * for (int steps = maxSize / 2; steps < maxSize; steps++) { int x = rnd.nextInt(1000); heap.add(steps, x);
	 * ref.add(x);
	 * 
	 * int v1 = heap.remove(); int v2 = ref.poll(); if (v1 != v2) { System.out.println("Error"); } }
	 * 
	 * while (!heap.isEmpty()) { int v1 = heap.remove(); int v2 = ref.poll();
	 * 
	 * if (v1 != v2) { System.out.println("Error"); } } }
	 */

	public void testSuffixArrayBuilder() throws Exception {
		for (int steps = 0; steps < 1000; steps++) {
			int n = rnd.nextInt(rnd.nextInt(100) + 1);
			StringBuilder sb = randomString(n);
			String[] suffixes = new String[n];
			for (int j = 0; j < n; j++) {
				suffixes[j] = sb.substring(j);
			}
			Arrays.sort(suffixes);
			Integer[] p = new Integer[n];
			int[] rank = new int[n];
			for (int j = 0; j < n; j++) {
				rank[j] = Arrays.binarySearch(suffixes, sb.substring(j));
				p[rank[j]] = j;
			}

			Integer[] sa = SuffixArray2.suffixArray(sb.toString());
			assertTrue(Arrays.equals(p, sa));
			if (n == 0) {
				continue;
			}
			int[] lcp = new int[n - 1];
			for (int j = 0; j < n - 1; j++) {
				lcp[j] = lcp(suffixes[j], suffixes[j + 1]);
			}
			//assertTrue(Arrays.equals(lcp, SuffixArray2.lcp(sa, sb.toString())));
		}
	}

	static StringBuilder randomString(int n) {
		char[] b = new char[n];
		for (int i = 0; i < n; i++) {
			b[i] = (char) ('a' + rnd.nextInt(5));
		}
		StringBuilder sb = new StringBuilder();
		sb.append(b);
		return sb;
	}

	static int lcp(String a, String b) {
		int res = 0;
		for (int i = 0; i < Math.min(a.length(), b.length()) && a.charAt(i) == b.charAt(i); i++) {
			++res;
		}
		return res;
	}

	static int[][] getRandomMatrixWithTriangleInequality(int n) {
		int[][] matrix = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j) {
					matrix[i][j] = rnd.nextInt(1000);
				}
			}
		}
		// Run Floyd-Warshall algorithm to establish triangle inequality
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					matrix[i][j] = Math.min(matrix[i][j], matrix[i][k] + matrix[k][j]);
				}
			}
		}
		return matrix;
	}

	public void testTsp() {
		for (int step = 0; step < 100; step++) {
			int n = rnd.nextInt(8) + 1;
			int[][] d = getRandomMatrixWithTriangleInequality(n);
			int[] p = new int[n];
			for (int i = 0; i < n; i++)
				p[i] = i;
			int res1 = Integer.MAX_VALUE;
			int res2 = Integer.MAX_VALUE;
			do {
				int s1 = 0;
				int s2 = 0;
				for (int i = 0; i < n; i++) {
					s1 += d[p[i]][p[(i + 1) % n]];
					if (i + 1 < n)
						s2 += d[p[i]][p[i + 1]];
				}
				res1 = Math.min(res1, s1);
				res2 = Math.min(res2, s2);
			} while (Permutations.nextPermutation(p));
			// assertEquals(res1, ShortestHamiltonianCycle.getShortestHamiltonianCycle(d));
			assertEquals(res1, ShortestHamiltonianCycle2.getShortestHamiltonianCycle(d));
			assertEquals(res2, ShortestHamiltonianPath.getShortestHamiltonianPath(d));
		}
	}

	static long[][] getRandomMatrix1(int n) {
		long[][] matrix = new long[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = rnd.nextInt(20);
			}
		}
		return matrix;
	}

	static long[][] getRandomMatrix(int n) {
		long[][] matrix = new long[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = -1;
			}
			matrix[i][i] = n;
		}
		return matrix;
	}

	public void testDeterminant() {
		for (int step = 0; step < 1000; step++) {
			// System.out.println(step);
			// int n = rnd.nextInt(20) + 1;
			int n = 10;
			long[][] a = getRandomMatrix(n);

			int[] p = new int[n];
			for (int i = 0; i < n; i++) {
				p[i] = i;
			}
			// long res1 = 0;
			// do {
			// long m = 1;
			// int cnt = 0;
			// for (int i = 0; i < n; i++) {
			// m *= a[i][p[i]];
			// for (int j = i + 1; j < n; j++) {
			// if (p[i] > p[j])
			// ++cnt;
			// }
			// }
			// if (cnt % 2 == 0)
			// res1 += m;
			// else
			// res1 -= m;
			// } while (Permutations.nextPermutation(p));

			// long res2 = Determinant.det(a);
			// System.out.println();
			// assertEquals(res1, res2);
		}
	}
	/*
	 * public void testSegmentTree() { for (int step = 0; step < 100; step++) { int n = rnd.nextInt(1000) + 1;
	 * RSQSegmentTreeFast rsq = new RSQSegmentTreeFast(n); RMQSegmentTreeFast rmq = new RMQSegmentTreeFast(n);
	 * FenwickTreeFull ft = new FenwickTreeFull(n); ReferenceSegmentTree refTree = new ReferenceSegmentTree(n);
	 * 
	 * for (int i = 0; i < 1000; i++) { int p = rnd.nextInt(n); int v = rnd.nextInt(); rsq.add(p, v); rmq.add(p, v);
	 * ft.add(p, v); refTree.add(p, v);
	 * 
	 * p = rnd.nextInt(n); v = rnd.nextInt(); rsq.set(p, v); rmq.set(p, v); ft.set(p, v); refTree.set(p, v);
	 * 
	 * int a = rnd.nextInt(n); int b = rnd.nextInt(n); if (a > b) { int t = a; a = b; b = t; }
	 * 
	 * assertEquals(refTree.sum(a, b), rsq.sum(a, b)); assertEquals(refTree.sum(0, b), rsq.sum(b));
	 * assertEquals(refTree.get(a), rsq.get(a)); assertEquals(refTree.max(a, b), rmq.max(a, b));
	 * assertEquals(refTree.get(a), rmq.get(a)); assertEquals(refTree.sum(a, b), ft.sum(a, b));
	 * assertEquals(refTree.sum(0, b), ft.sum(b)); assertEquals(refTree.get(a), ft.get(a)); } } }
	 * 
	 * public void testZeroOneTree() { for (int step = 0; step < 100; step++) { int n = rnd.nextInt(1000) + 1;
	 * RSQSegmentTreeFast rsq = new RSQSegmentTreeFast(n); FenwickTreeFull ft = new FenwickTreeFull(n);
	 * ReferenceSegmentTree refTree = new ReferenceSegmentTree(n);
	 * 
	 * for (int i = 0; i < 1000; i++) { int p = rnd.nextInt(n); int v = rnd.nextInt(2); rsq.set(p, v); ft.set(p, v);
	 * refTree.set(p, v);
	 * 
	 * int a = rnd.nextInt(n); int b = rnd.nextInt(n); if (a > b) { int t = a; a = b; b = t; }
	 * 
	 * int x1 = rnd.nextInt(n); assertEquals(refTree.getPrevZero(x1), rsq.getPrevZero(x1)); int k = rnd.nextInt(n);
	 * assertEquals(refTree.getKthZeroCyclic(x1, k), ft.getKthZeroCyclic(x1, k)); } } }
	 */

}
