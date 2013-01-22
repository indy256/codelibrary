import java.util.*;

public class PerfectMatchingCount {

	static class Vertex {
		List<Vertex> adj = new ArrayList<>();
		int id, max;
	}

	public static int numberOfPerfectMatchings(Vertex[] vertices) {
		int n = vertices.length;
		for (int i = 0; i < n; i++)
			vertices[i].id = i;
		for (Vertex u : vertices)
			for (Vertex v : u.adj)
				u.max = Math.max(u.max, v.id);
		int[] v2i = null;
		int[] i2v = null;
		int active = 0;
		int[] ways = { 1 };
		for (int k = 0; k < n; k++) {
			int[] nv2i = new int[k + 1];
			int[] ni2v = new int[k + 1];
			Arrays.fill(nv2i, -1);
			int nactive = 0;

			for (int i = 0; i <= k; i++) {
				if (vertices[i].max > k) {
					nv2i[i] = nactive;
					ni2v[nactive] = i;
					++nactive;
				}
			}
			int[] nways = new int[1 << nactive];
			loop: for (int mask = 0; mask < 1 << active; mask++) {
				if (ways[mask] > 0) {
					boolean need = false;
					int nmask = 0;
					for (int i = 0; i < active; i++) {
						if ((mask & 1 << i) != 0) {
							int ni = nv2i[i2v[i]];
							if (ni < 0) {
								if (need)
									continue loop;
								need = true;
							} else {
								nmask |= 1 << ni;
							}
						}
					}
					if (need) {
						nways[nmask] += ways[mask];
					} else {
						if (nv2i[k] >= 0) {
							nways[nmask | 1 << nv2i[k]] += ways[mask];
						}

						for (Vertex v : vertices[k].adj) {
							if (v.id < k && (mask & 1 << v2i[v.id]) != 0) {
								nways[nmask ^ 1 << nv2i[v.id]] += ways[mask];
							}
						}
					}
				}
			}
			v2i = nv2i;
			i2v = ni2v;
			active = nactive;
			ways = nways;
		}
		return ways[0];
	}

	static Vertex[] convert(boolean[][] g) {
		List<Vertex> list = new ArrayList<>();
		for (int i = 0; i < g.length; i++)
			list.add(new Vertex());
		for (int i = 0; i < g.length; i++)
			for (int j = 0; j < g.length; j++)
				if (g[i][j])
					list.get(i).adj.add(list.get(j));
		return list.toArray(new Vertex[0]);
	}

	// Usage example
	public static void main(String[] args) {
		boolean[][] g = new boolean[4][4];
		for (int i = 0; i < g.length; i++) {
			Arrays.fill(g[i], true);
		}
		System.out.println(3 == numberOfPerfectMatchings(convert(g)));
		g[0][2] = g[2][0] = false;
		g[1][3] = g[3][1] = false;
		System.out.println(2 == numberOfPerfectMatchings(convert(g)));
	}
}
