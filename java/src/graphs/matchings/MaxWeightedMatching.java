package graphs.matchings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MaxWeightedMatching {

	static class Edge {
		final int u, v;
		final int w;

		public Edge(int u, int v, int w) {
			this.u = u;
			this.v = v;
			this.w = w;
		}
	}

	Edge[] edges;
	boolean maxcardinality;
	int nedge;
	int nvertex;
	int maxweight;
	int[] endpoint;
	List<Integer>[] neighbend;
	int[] mate;
	int[] label;
	int[] labelend;
	int[] inblossom;
	int[] blossomparent;
	List<Integer>[] blossomchilds;
	int[] blossombase;
	List<Integer>[] blossomendps;
	int[] bestedge;
	List<Integer>[] blossombestedges;
	List<Integer> unusedblossoms;
	int[] dualvar;
	boolean[] allowedge;
	List<Integer> queue;

	int slack(int k) {
		Edge e = edges[k];
		return dualvar[e.u] + dualvar[e.v] - 2 * e.w;
	}

	void blossomLeaves(int b, Collection<Integer> result) {
		if (b < nvertex) {
			result.add(b);
		} else {
			for (int t : blossomchilds[b])
				if (t < nvertex)
					result.add(t);
				else
					blossomLeaves(t, result);
		}
	}

	void assignLabel(int w, int t, int p) {
		int b = inblossom[w];
		label[w] = label[b] = t;
		labelend[w] = labelend[b] = p;
		bestedge[w] = bestedge[b] = -1;
		if (t == 1) {
			blossomLeaves(b, queue);
		} else if (t == 2) {
			int base = blossombase[b];
			assignLabel(endpoint[mate[base]], 1, mate[base] ^ 1);
		}
	}

	int scanBlossom(int v, int w) {
		List<Integer> path = new ArrayList<>();
		int base = -1;
		while (v != -1 || w != -1) {
			int b = inblossom[v];
			if ((label[b] & 4) != 0) {
				base = blossombase[b];
				break;
			}
			path.add(b);
			label[b] = 5;
			if (labelend[b] == -1) {
				v = -1;
			} else {
				v = endpoint[labelend[b]];
				b = inblossom[v];
				v = endpoint[labelend[b]];
			}
			if (w != -1) {
				int t = v;
				v = w;
				w = t;
			}
		}
		for (int b : path)
			label[b] = 1;
		return base;
	}

	void addBlossom(int base, int k) {
		int bb = inblossom[base];
		int bv = inblossom[edges[k].u];
		int bw = inblossom[edges[k].v];

		int b = unusedblossoms.remove(unusedblossoms.size() - 1);

		blossombase[b] = base;
		blossomparent[b] = -1;
		blossomparent[bb] = b;

		List<Integer> path = blossomchilds[b] = new ArrayList<>();
		List<Integer> endps = blossomendps[b] = new ArrayList<>();

		while (bv != bb) {
			blossomparent[bv] = b;
			path.add(bv);
			endps.add(labelend[bv]);
			int v = endpoint[labelend[bv]];
			bv = inblossom[v];
		}
		path.add(bb);
		Collections.reverse(path);
		Collections.reverse(endps);
		endps.add(2 * k);
		while (bw != bb) {
			blossomparent[bw] = b;
			path.add(bw);
			endps.add(labelend[bw] ^ 1);
			int w = endpoint[labelend[bw]];
			bw = inblossom[w];
		}
		label[b] = 1;
		labelend[b] = labelend[bb];
		dualvar[b] = 0;
		List<Integer> res = new ArrayList<>();
		blossomLeaves(b, res);
		for (int v : res) {
			if (label[inblossom[v]] == 2) {
				queue.add(v);
			}
			inblossom[v] = b;
		}
		int[] bestedgeto = IntStream.generate(() -> -1).limit(2 * nvertex).toArray();
		for (int bv1 : path) {
			List<List<Integer>> nblists;
			if (blossombestedges[bv1] == null) {
				nblists = new ArrayList<>();
				List<Integer> res1 = new ArrayList<>();
				blossomLeaves(bv1, res1);
				for (int v : res1) {
					List<Integer> list = new ArrayList<>();
					for (int p : neighbend[v]) {
						list.add(p / 2);
					}
					nblists.add(list);
				}
			} else {
				nblists = Collections.singletonList(blossombestedges[bv1]);
			}
			for (List<Integer> nblist : nblists) {
				for (int k1 : nblist) {
					int i = edges[k1].u;
					int j = edges[k1].v;
					if (inblossom[j] == b) {
						int t = i;
						i = j;
						j = t;
					}
					int bj = inblossom[j];
					if (bj != b && label[bj] == 1 && (bestedgeto[bj] == -1 || slack(k1) < slack(bestedgeto[bj]))) {
						bestedgeto[bj] = k1;
					}
				}
			}
			blossombestedges[bv1] = null;
			bestedge[bv1] = -1;
		}
		blossombestedges[b] = Arrays.stream(bestedgeto).filter(x -> x != -1).boxed().collect(Collectors.toList());
		bestedge[b] = -1;
		for (int k1 : blossombestedges[b]) {
			if (bestedge[b] == -1 || slack(k1) < slack(bestedge[b])) {
				bestedge[b] = k1;
			}
		}
	}

	void expandBlossom(int b, boolean endstage) {
		for (int s : blossomchilds[b]) {
			blossomparent[s] = -1;
			if (s < nvertex) {
				inblossom[s] = s;
			} else if (endstage && dualvar[s] == 0) {
				expandBlossom(s, endstage);
			} else {
				List<Integer> res = new ArrayList<>();
				blossomLeaves(s, res);
				for (int v : res) {
					inblossom[v] = s;
				}
			}
		}
		if (!endstage && label[b] == 2) {
			int entrychild = inblossom[endpoint[labelend[b] ^ 1]];
			int j = blossomchilds[b].indexOf(entrychild);
			int jstep;
			int endptrick;
			if ((j & 1) != 0) {
				j -= blossomchilds[b].size();
				jstep = 1;
				endptrick = 0;
			} else {
				jstep = -1;
				endptrick = 1;
			}
			int p = labelend[b];
			while (j != 0) {
				label[endpoint[p ^ 1]] = 0;
				label[endpoint[blossomendps[b].get((j - endptrick + blossomendps[b].size()) % blossomendps[b].size()) ^ endptrick ^ 1]] = 0;
				assignLabel(endpoint[p ^ 1], 2, p);
				allowedge[blossomendps[b].get((j - endptrick + blossomendps[b].size()) % blossomendps[b].size()) / 2] = true;
				j += jstep;
				p = blossomendps[b].get((j - endptrick + blossomendps[b].size()) % blossomendps[b].size()) ^ endptrick;
				allowedge[p / 2] = true;
				j += jstep;
			}
			int bv = blossomchilds[b].get(j);
			label[endpoint[p ^ 1]] = label[bv] = 2;
			labelend[endpoint[p ^ 1]] = labelend[bv] = p;
			bestedge[bv] = -1;
			j += jstep;
			while (blossomchilds[b].get((j + blossomchilds[b].size()) % blossomchilds[b].size()) != entrychild) {
				int bv1 = blossomchilds[b].get((j + blossomchilds[b].size()) % blossomchilds[b].size());
				if (label[bv1] == 1) {
					j += jstep;
					continue;
				}
				List<Integer> res1 = new ArrayList<>();
				blossomLeaves(bv1, res1);
				for (int v : res1) {
					if (label[v] != 0) {
						label[v] = 0;
						label[endpoint[mate[blossombase[bv1]]]] = 0;
						assignLabel(v, 2, labelend[v]);
						break;
					}
				}
				j += jstep;
			}
		}
		label[b] = labelend[b] = -1;
		blossomchilds[b] = blossomendps[b] = null;
		blossombase[b] = -1;
		blossombestedges[b] = null;
		bestedge[b] = -1;
		unusedblossoms.add(b);
	}

	void augmentBlossom(int b, int v) {
		int t = v;
		while (blossomparent[t] != b)
			t = blossomparent[t];
		if (t >= nvertex)
			augmentBlossom(t, v);
		int i = blossomchilds[b].indexOf(t);
		int j = i;
		int jstep;
		int endptrick;
		if ((i & 1) != 0) {
			j -= blossomchilds[b].size();
			jstep = 1;
			endptrick = 0;
		} else {
			jstep = -1;
			endptrick = 1;
		}
		while (j != 0) {
			j += jstep;
			t = blossomchilds[b].get((j + blossomchilds[b].size()) % blossomchilds[b].size());
			int p = blossomendps[b].get((j - endptrick + blossomendps[b].size()) % blossomendps[b].size()) ^ endptrick;
			if (t >= nvertex)
				augmentBlossom(t, endpoint[p]);
			j += jstep;
			t = blossomchilds[b].get((j + blossomchilds[b].size()) % blossomchilds[b].size());
			if (t >= nvertex)
				augmentBlossom(t, endpoint[p ^ 1]);
			mate[endpoint[p]] = p ^ 1;
			mate[endpoint[p ^ 1]] = p;
		}
		blossomchilds[b] = Stream.concat(blossomchilds[b].subList(i, blossomchilds[b].size()).stream(), blossomchilds[b].subList(0, i).stream()).collect(Collectors.toList());
		blossomendps[b] = Stream.concat(blossomendps[b].subList(i, blossomendps[b].size()).stream(), blossomendps[b].subList(0, i).stream()).collect(Collectors.toList());
		blossombase[b] = blossombase[blossomchilds[b].get(0)];
	}

	void augmentMatching(int k) {
		int[][] a = {{edges[k].u, 2 * k + 1}, {edges[k].v, 2 * k}};
		for (int[] aa : a) {
			int s = aa[0];
			int p = aa[1];
			while (true) {
				int bs = inblossom[s];
				if (bs >= nvertex)
					augmentBlossom(bs, s);
				mate[s] = p;
				if (labelend[bs] == -1)
					break;
				int t = endpoint[labelend[bs]];
				int bt = inblossom[t];
				s = endpoint[labelend[bt]];
				int j = endpoint[labelend[bt] ^ 1];
				if (bt >= nvertex)
					augmentBlossom(bt, j);
				mate[j] = labelend[bt];
				p = labelend[bt] ^ 1;
			}
		}
	}

	int[] maxWeightMatching(Edge[] edges, boolean maxcardinality) {
		if (edges.length == 0)
			return new int[0];

		this.edges = edges;
		this.maxcardinality = maxcardinality;

		nedge = edges.length;
		nvertex = 0;
		maxweight = 0;
		for (Edge edge : edges) {
			nvertex = Math.max(nvertex, edge.u + 1);
			nvertex = Math.max(nvertex, edge.v + 1);
			maxweight = Math.max(maxweight, edge.w);
		}

		endpoint = new int[2 * edges.length];
		for (int i = 0; i < edges.length; i++) {
			endpoint[2 * i] = edges[i].u;
			endpoint[2 * i + 1] = edges[i].v;
		}

		neighbend = Stream.generate(ArrayList::new).limit(nvertex).toArray(List[]::new);
		for (int k = 0; k < edges.length; k++) {
			Edge edge = edges[k];
			neighbend[edge.u].add(2 * k + 1);
			neighbend[edge.v].add(2 * k);
		}

		mate = IntStream.generate(() -> -1).limit(nvertex).toArray();

		label = new int[2 * nvertex];

		labelend = IntStream.generate(() -> -1).limit(2 * nvertex).toArray();

		inblossom = IntStream.range(0, nvertex).toArray();

		blossomparent = IntStream.generate(() -> -1).limit(2 * nvertex).toArray();

		blossomchilds = new List[2 * nvertex];

		blossombase = Stream.concat(IntStream.range(0, nvertex).boxed(), Stream.generate(() -> -1).limit(nvertex)).mapToInt(x -> x).toArray();

		blossomendps = new List[2 * nvertex];

		bestedge = IntStream.generate(() -> -1).limit(2 * nvertex).toArray();

		blossombestedges = new List[2 * nvertex];

		unusedblossoms = IntStream.range(nvertex, 2 * nvertex).boxed().collect(Collectors.toList());

		dualvar = Stream.concat(Stream.generate(() -> maxweight).limit(nvertex), Stream.generate(() -> 0).limit(nvertex)).mapToInt(x -> x).toArray();

		allowedge = new boolean[nedge];

		queue = new ArrayList<>();

		for (int t = 0; t < nvertex; t++) {
			Arrays.fill(label, 0);

			Arrays.fill(bestedge, -1);
			Arrays.fill(blossombestedges, nvertex, 2 * nvertex, null);
			Arrays.fill(allowedge, false);
			queue.clear();

			for (int v = 0; v < nvertex; v++) {
				if (mate[v] == -1 && label[inblossom[v]] == 0)
					assignLabel(v, 1, -1);
			}

			int augmented = 0;
			while (true) {
				while (!queue.isEmpty() && augmented == 0) {
					int v = queue.remove(queue.size() - 1);

					for (int p : neighbend[v]) {
						int k = p / 2;
						int w = endpoint[p];
						if (inblossom[v] == inblossom[w]) {
							continue;
						}
						int kslack = 0;
						if (!allowedge[k]) {
							kslack = slack(k);
							if (kslack <= 0) {
								allowedge[k] = true;
							}
						}
						if (allowedge[k]) {
							if (label[inblossom[w]] == 0) {
								assignLabel(w, 2, p ^ 1);
							} else if (label[inblossom[w]] == 1) {
								int base = scanBlossom(v, w);
								if (base >= 0) {
									addBlossom(base, k);
								} else {
									augmentMatching(k);
									augmented = 1;
									break;
								}
							} else if (label[w] == 0) {
								label[w] = 2;
								labelend[w] = p ^ 1;
							}
						} else if (label[inblossom[w]] == 1) {
							int b = inblossom[v];
							if (bestedge[b] == -1 || kslack < slack(bestedge[b]))
								bestedge[b] = k;
						} else if (label[w] == 0) {
							if (bestedge[w] == -1 || kslack < slack(bestedge[w]))
								bestedge[w] = k;
						}
					}
				}

				if (augmented != 0)
					break;

				int deltatype = -1;
				Integer delta = null;
				Integer deltaedge = null;
				Integer deltablossom = null;

				if (!maxcardinality) {
					deltatype = 1;
					delta = Arrays.stream(dualvar).limit(nvertex).min().getAsInt();
				}

				for (int v = 0; v < nvertex; v++) {
					if (label[inblossom[v]] == 0 && bestedge[v] != -1) {
						int d = slack(bestedge[v]);
						if (deltatype == -1 || d < delta) {
							delta = d;
							deltatype = 2;
							deltaedge = bestedge[v];
						}
					}
				}

				for (int b = 0; b < 2 * nvertex; b++) {
					if (blossomparent[b] == -1 && label[b] == 1 && bestedge[b] != -1) {
						int kslack = slack(bestedge[b]);
						int d = kslack / 2;
						if (deltatype == -1 || d < delta) {
							delta = d;
							deltatype = 3;
							deltaedge = bestedge[b];
						}
					}
				}

				for (int b = nvertex; b < 2 * nvertex; b++) {
					if (blossombase[b] >= 0 && blossomparent[b] == -1 && label[b] == 2 && (deltatype == -1 || dualvar[b] < delta)) {
						delta = dualvar[b];
						deltatype = 4;
						deltablossom = b;
					}
				}

				if (deltatype == -1) {
					deltatype = 1;
					delta = Math.max(0, Arrays.stream(dualvar).limit(nvertex).min().getAsInt());
				}

				for (int v = 0; v < nvertex; v++) {
					if (label[inblossom[v]] == 1)
						dualvar[v] -= delta;
					else if (label[inblossom[v]] == 2)
						dualvar[v] += delta;
				}

				for (int b = nvertex; b < 2 * nvertex; b++) {
					if (blossombase[b] >= 0 && blossomparent[b] == -1) {
						if (label[b] == 1)
							dualvar[b] += delta;
						else if (label[b] == 2)
							dualvar[b] -= delta;
					}
				}

				if (deltatype == 1)
					break;
				else if (deltatype == 2) {
					allowedge[deltaedge] = true;
					int i = edges[deltaedge].u;
					int j = edges[deltaedge].v;
					if (label[inblossom[i]] == 0) {
						int tt = i;
						i = j;
						j = tt;
					}
					queue.add(i);
				} else if (deltatype == 3) {
					allowedge[deltaedge] = true;
					queue.add(edges[deltaedge].u);
				} else if (deltatype == 4) {
					expandBlossom(deltablossom, false);
				}
			}

			if (augmented == 0)
				break;

			for (int b = nvertex; b < 2 * nvertex; b++) {
				if (blossomparent[b] == -1 && blossombase[b] >= 0 && label[b] == 1 && dualvar[b] == 0)
					expandBlossom(b, true);
			}
		}
		for (int v = 0; v < nvertex; v++)
			if (mate[v] >= 0)
				mate[v] = endpoint[mate[v]];
		return mate;
	}

	public static void main(String[] args) {
		// System.out.println(Arrays.toString(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(0, 1, -1)}, false)));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(0, 1, 1)}, false), new int[]{1, 0}));
		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 10), new Edge(2, 3, 11)}, false), new int[]{-1, -1, 3, 2}));
		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 5), new Edge(2, 3, 11), new Edge(3, 4, 5)}, false), new int[]{-1, -1, 3, 2, -1}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 2), new Edge(1, 3, -2), new Edge(2, 3, 1), new Edge(2, 4, -1), new Edge(3, 4, -6)}, false), new int[]{-1, 2, 1, -1, -1}));
		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 2), new Edge(1, 3, -2), new Edge(2, 3, 1), new Edge(2, 4, -1), new Edge(3, 4, -6)}, true), new int[]{-1, 3, 4, 1, 2}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 8), new Edge(1, 3, 9), new Edge(2, 3, 10), new Edge(3, 4, 7)}, false), new int[]{-1, 2, 1, 4, 3}));
		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 8), new Edge(1, 3, 9), new Edge(2, 3, 10), new Edge(3, 4, 7), new Edge(1, 6, 5), new Edge(4, 5, 6)}, false), new int[]{-1, 6, 3, 2, 5, 4, 1}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 9), new Edge(1, 3, 8), new Edge(2, 3, 10), new Edge(1, 4, 5), new Edge(4, 5, 4), new Edge(1, 6, 3)}, false), new int[]{-1, 6, 3, 2, 5, 4, 1}));
		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 9), new Edge(1, 3, 8), new Edge(2, 3, 10), new Edge(1, 4, 5), new Edge(4, 5, 3), new Edge(1, 6, 4)}, false), new int[]{-1, 6, 3, 2, 5, 4, 1}));
		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 9), new Edge(1, 3, 8), new Edge(2, 3, 10), new Edge(1, 4, 5), new Edge(4, 5, 3), new Edge(3, 6, 4)}, false), new int[]{-1, 2, 1, 6, 5, 4, 3}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 9), new Edge(1, 3, 9), new Edge(2, 3, 10), new Edge(2, 4, 8), new Edge(3, 5, 8), new Edge(4, 5, 10), new Edge(5, 6, 6)}, false), new int[]{-1, 3, 4, 1, 2, 6, 5}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 10), new Edge(1, 7, 10), new Edge(2, 3, 12), new Edge(3, 4, 20), new Edge(3, 5, 20), new Edge(4, 5, 25), new Edge(5, 6, 10), new Edge(6, 7, 10), new Edge(7, 8, 8)}, false), new int[]{-1, 2, 1, 4, 3, 6, 5, 8, 7}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 8), new Edge(1, 3, 8), new Edge(2, 3, 10), new Edge(2, 4, 12), new Edge(3, 5, 12), new Edge(4, 5, 14), new Edge(4, 6, 12), new Edge(5, 7, 12), new Edge(6, 7, 14), new Edge(7, 8, 12)}, false), new int[]{-1, 2, 1, 5, 6, 3, 4, 8, 7}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 23), new Edge(1, 5, 22), new Edge(1, 6, 15), new Edge(2, 3, 25), new Edge(3, 4, 22), new Edge(4, 5, 25), new Edge(4, 8, 14), new Edge(5, 7, 13)}, false), new int[]{-1, 6, 3, 2, 8, 7, 1, 5, 4}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 19), new Edge(1, 3, 20), new Edge(1, 8, 8), new Edge(2, 3, 25), new Edge(2, 4, 18), new Edge(3, 5, 18), new Edge(4, 5, 13), new Edge(4, 7, 7), new Edge(5, 6, 7)}, false), new int[]{-1, 8, 3, 2, 7, 6, 5, 4, 1}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 45), new Edge(1, 5, 45), new Edge(2, 3, 50), new Edge(3, 4, 45), new Edge(4, 5, 50), new Edge(1, 6, 30), new Edge(3, 9, 35), new Edge(4, 8, 35), new Edge(5, 7, 26), new Edge(9, 10, 5)}, false), new int[]{-1, 6, 3, 2, 8, 7, 1, 5, 4, 10, 9}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 45), new Edge(1, 5, 45), new Edge(2, 3, 50), new Edge(3, 4, 45), new Edge(4, 5, 50), new Edge(1, 6, 30), new Edge(3, 9, 35), new Edge(4, 8, 26), new Edge(5, 7, 40), new Edge(9, 10, 5)}, false), new int[]{-1, 6, 3, 2, 8, 7, 1, 5, 4, 10, 9}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 45), new Edge(1, 5, 45), new Edge(2, 3, 50), new Edge(3, 4, 45), new Edge(4, 5, 50), new Edge(1, 6, 30), new Edge(3, 9, 35), new Edge(4, 8, 28), new Edge(5, 7, 26), new Edge(9, 10, 5)}, false), new int[]{-1, 6, 3, 2, 8, 7, 1, 5, 4, 10, 9}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 45), new Edge(1, 7, 45), new Edge(2, 3, 50), new Edge(3, 4, 45), new Edge(4, 5, 95), new Edge(4, 6, 94), new Edge(5, 6, 94), new Edge(6, 7, 50),
				new Edge(1, 8, 30), new Edge(3, 11, 35), new Edge(5, 9, 36), new Edge(7, 10, 26), new Edge(11, 12, 5)}, false), new int[]{-1, 8, 3, 2, 6, 9, 4, 10, 1, 5, 7, 12, 11}));

		System.out.println(Arrays.equals(new MaxWeightedMatching().maxWeightMatching(new Edge[]{new Edge(1, 2, 40), new Edge(1, 3, 40), new Edge(2, 3, 60), new Edge(2, 4, 55), new Edge(3, 5, 55), new Edge(4, 5, 5), new Edge(1, 8, 15),
				new Edge(5, 7, 30), new Edge(7, 6, 10), new Edge(8, 10, 10), new Edge(4, 9, 30)}, false), new int[]{-1, 2, 1, 5, 9, 3, 7, 6, 10, 4, 8}));

		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(15) + 1;
			int[][] a = new int[n][n];
			List<Edge> edges = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				for (int j = i + 1; j < n; j++) {
					if (rnd.nextBoolean()) {
						a[i][j] = a[j][i] = rnd.nextInt(100) + 1;
						if (a[i][j] != 0) {
							edges.add(new Edge(i, j, a[i][j]));
						}
					}
				}
			}
			int[] matching = new MaxWeightedMatching().maxWeightMatching(edges.toArray(new Edge[edges.size()]), false);
//			if (matching.length < n || Arrays.stream(matching).anyMatch(x -> x == -1)) continue;
			int res1 = 0;
			for (int i = 0; i < matching.length; i++) {
				if (matching[i] != -1)
					res1 += a[i][matching[i]];
			}
			res1 /= 2;
			int res2 = maximumWeightMatchingSlow(a);
//			int res2 = maximumWeightPerfectMatchingSlow(a);
			if (res1 != res2) {
				System.err.println(step + ": " + res1 + " " + res2);
				break;
			}
		}
	}

	static int maximumWeightMatchingSlow(int[][] a) {
		int n = a.length;
		int[] dp = new int[1 << n];
		for (int mask = 0; mask < dp.length; mask++) {
			for (int i = 0; i < n; i++) {
				if ((mask & (1 << i)) == 0) {
					for (int j = i + 1; j < n; j++) {
						if ((mask & (1 << j)) == 0) {
							dp[mask | (1 << i) | (1 << j)] = Math.max(dp[mask | (1 << i) | (1 << j)], dp[mask] + a[i][j]);
						}
					}
					break;
				}
			}
		}
		return dp[dp.length - 1];
	}

	static int maximumWeightPerfectMatchingSlow(int[][] a) {
		int n = a.length;
		int[] dp = new int[1 << n];
		Arrays.fill(dp, Integer.MIN_VALUE / 2);
		dp[0] = 0;
		for (int mask = 0; mask < dp.length; mask++) {
			if (dp[mask] != Integer.MIN_VALUE / 2) {
				for (int i = 0; i < n; i++) {
					if ((mask & (1 << i)) == 0) {
						for (int j = i + 1; j < n; j++) {
							if ((mask & (1 << j)) == 0 && a[i][j] != 0) {
								dp[mask | (1 << i) | (1 << j)] = Math.max(dp[mask | (1 << i) | (1 << j)], dp[mask] + a[i][j]);
							}
						}
						break;
					}
				}
			}
		}
		return dp[dp.length - 1];
	}
}
