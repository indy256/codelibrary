import java.io.PrintStream;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Random;

public class LinkCutTreeTest1 {

	public static void main(String[] args) throws Exception {
		PrintStream ps = new PrintStream("dynalca.in");
		int n = 100000;
		LinkCutTreeLca.Node[] nodes1 = new LinkCutTreeLca.Node[n];
		TreeForest.Node[] nodes2 = new TreeForest.Node[n];
		Map<LinkCutTreeLca.Node, Integer> set1 = new IdentityHashMap<>();
		Map<TreeForest.Node, Integer> set2 = new IdentityHashMap<>();
		for (int i = 0; i < n; i++) {
			nodes1[i] = new LinkCutTreeLca.Node();
			nodes2[i] = new TreeForest.Node();
			set1.put(nodes1[i], i);
			set2.put(nodes2[i], i);
		}
		Random rnd = new Random(1);
		int linkCount = 0;
		int cutCount = 0;
		int lcaCount = 0;
		ps.println(n);
		for (int step = 0; step < 1020; step++) {
			if (rnd.nextInt(10) < 9) {
				if (linkCount < 50000) {
					int u = rnd.nextInt(n);
					int v = rnd.nextInt(n);
					if (TreeForest.findRoot(nodes2[u]) != TreeForest.findRoot(nodes2[v]) && nodes2[u].parent == null) {
						TreeForest.link(nodes2[u], nodes2[v]);
						LinkCutTreeLca.link(nodes1[u], nodes1[v]);
						ps.println("link " + (u + 1) + " " + (v + 1));
						++linkCount;
					}
				}
			} else {
				int u = rnd.nextInt(n);
				if (nodes2[u].parent != null) {
					if (cutCount < 5000) {
						TreeForest.cut(nodes2[u]);
						LinkCutTreeLca.cut(nodes1[u]);
						ps.println("cut " + (u + 1));
						++cutCount;
					}
				}
			}

			if (lcaCount < 50000 && step > 1000) {
				m1: while (true) {
					int u = rnd.nextInt(n);
					int v = rnd.nextInt(n);
					if (u != v && TreeForest.findRoot(nodes2[u]) == TreeForest.findRoot(nodes2[v])) {
						TreeForest.Node lca2 = TreeForest.lca(nodes2[u], nodes2[v]);
						LinkCutTreeLca.Node lca1 = LinkCutTreeLca.lca(nodes1[u], nodes1[v]);
						int r1 = set1.get(lca1);
						int r2 = set2.get(lca2);
						if (r1 != r2) {
							System.err.println(u + " " + v + " " + r1 + " " + r2);
						}
						ps.println("lca " + (u + 1) + " " + (v + 1));
						++lcaCount;
						break m1;
					}
				}
			}
		}
		System.out.println("linkCount = " + linkCount);
		System.out.println("cutCount = " + cutCount);
		System.out.println("lcaCount = " + lcaCount);
	}
}
