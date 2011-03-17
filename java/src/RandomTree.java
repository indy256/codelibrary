import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class RandomTree {

	static Node root;
	static int cnt;
	static int edges;
	static boolean narrow;

	static class Node {
		int key;
		List<Node> children = new ArrayList<Node>();

		public Node() {
			this.key = cnt++;
		}
	}

	static Random rnd = new Random(1);

	static Node generateTree(Node p, int nodes) {
		if (nodes <= 0)
			return null;
		if (nodes == 1)
			return new Node();

		Node node = new Node();
		--nodes;
		int nc = narrow ? rnd.nextInt(Math.min(nodes, 10)) + 1 : rnd.nextInt(nodes) + 1;
		for (int i = 0; i < nc; i++) {
			int count = nodes / nc + (i < nodes % nc ? 1 : 0);
			Node child = generateTree(node, count);
			node.children.add(child);
		}
		return node;
	}

	static int[] pa, pb;

	static void print(Node t) {
		if (t != null) {
			for (Node child : t.children) {
				pa[edges] = t.key;
				pb[edges] = child.key;
				++edges;
			}
			for (Node child : t.children) {
				print(child);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 400; i++) {
			list.add(i + 1);
		}
		Collections.shuffle(list);

		narrow = false;
		genCase(1, list);

		narrow = true;
		genCase(2, list);

		narrow = false;
		list.clear();
		for (int i = 0; i < 10; i++) {
			list.add(10000);
		}
		genCase(3, list);

		narrow = true;
		list.clear();
		for (int i = 0; i < 10; i++) {
			list.add(10000);
		}
		genCase(4, list);

		narrow = false;
		list.clear();
		list.add(90000);
		genCase(5, list);

		narrow = true;
		list.clear();
		list.add(90000);
		genCase(6, list);
	}

	static void genCase(int id, List<Integer> list) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter("D:/projects/cpp/spoj/treeiso_input" + (id-1) + ".txt");
		pw.println(list.size());
		for (int n : list) {
			if (rnd.nextBoolean())
				generateYesCase(pw, n);
			else
				generateNoCase(pw, n);

		}
		pw.close();
	}

	static void generateNoCase(PrintWriter pw, int n) {
		root = null;
		cnt = 0;
		edges = 0;

		pw.println(n);
		pa = new int[n - 1];
		pb = new int[n - 1];
		root = generateTree(null, n);
		print(root);

		List<Integer> pv = getP(n);
		List<Integer> pe = getP(n - 1);

		for (int i = 0; i < n - 1; i++) {
			int e = pe.get(i);
			pw.println((pv.get(pa[e]) + 1) + " " + (pv.get(pb[e]) + 1));
		}

		root = null;
		cnt = 0;
		edges = 0;
		root = generateTree(null, n);
		print(root);

		Collections.shuffle(pe);

		for (int i = 0; i < n - 1; i++) {
			int e = pe.get(i);
			int a = pv.get(pa[e]);
			int b = pv.get(pb[e]);
			pw.println((a + 1) + " " + (b + 1));
		}
	}

	static void generateYesCase(PrintWriter pw, int n) {
		root = null;
		cnt = 0;
		edges = 0;

		pw.println(n);
		pa = new int[n - 1];
		pb = new int[n - 1];
		root = generateTree(null, n);
		print(root);

		List<Integer> pv = getP(n);
		List<Integer> pe = getP(n - 1);
		for (int i = 0; i < n - 1; i++) {
			int e = pe.get(i);
			pw.println((pv.get(pa[e]) + 1) + " " + (pv.get(pb[e]) + 1));
		}

		Collections.shuffle(pe);

		for (int i = 0; i < n - 1; i++) {
			int e = pe.get(i);
			int a = pv.get(pa[e]);
			int b = pv.get(pb[e]);
			pw.println((a + 1) + " " + (b + 1));
		}
	}

	static List<Integer> getP(int n) {
		List<Integer> pe = new ArrayList<Integer>();
		for (int i = 0; i < n; i++)
			pe.add(i);
		Collections.shuffle(pe);
		return pe;
	}
}
