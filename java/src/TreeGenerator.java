import java.util.*;

public class TreeGenerator {

	static class Node implements Comparable<Node> {
		int degree;
		int num;

		public Node(int degree, int num) {
			this.degree = degree;
			this.num = num;
		}

		public int compareTo(Node o) {
			if (degree != o.degree)
				return degree < o.degree ? -1 : 1;
			return num < o.num ? -1 : num > o.num ? 1 : 0;
		}
	}

	public static List<Integer>[] prufer2Tree(int[] a) {
		int n = a.length + 2;
		List<Integer>[] t = new List[n];
		for (int i = 0; i < n; i++) {
			t[i] = new ArrayList<Integer>();
		}
		int[] degree = new int[n];
		for (int x : a) {
			++degree[x];
		}
		PriorityQueue<Node> q = new PriorityQueue<Node>();
		for (int i = 0; i < n; i++) {
			q.add(new Node(degree[i] + 1, i));
		}
		for (int x : a) {
			Node node = q.poll();
			if (node.degree > 1) {
				--node.degree;
				q.add(node);
			}
			t[x].add(node.num);
			t[node.num].add(x);
		}
		Node u = q.poll();
		Node v = q.poll();
		t[u.num].add(v.num);
		t[v.num].add(u.num);
		return t;
	}

	// precondition: n >= 2
	public static List<Integer>[] generateRandomTree(int n, Random rnd) {
		int[] a = new int[n - 2];
		for (int i = 0; i < a.length; i++) {
			a[i] = rnd.nextInt(n);
		}
		return prufer2Tree(a);
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(prufer2Tree(new int[] { 3, 3, 3, 4 })));
		System.out.println(Arrays.toString(prufer2Tree(new int[] { 0, 0 })));
	}
}
