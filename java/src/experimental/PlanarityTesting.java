package experimental;

public class PlanarityTesting {

	public static boolean isPlanar(int n, int m, int nodei[], int nodej[]) {
		if (m <= 1)
			return true;

		int i, j, k, n1, n2, m2, nm2, n2m, nmp2, m7n5, m22, m33, mtotal;
		int node1, node2, qnode, tnode, tnum, aux1, aux2, aux3, aux4;
		int level[] = new int[1];
		int initp[] = new int[1];
		int snode[] = new int[1];
		int pnum[] = new int[1];
		int snum[] = new int[1];
		int nexte[] = new int[1];
		int store1[] = new int[1];
		int store2[] = new int[1];
		int store3[] = new int[1];
		int store4[] = new int[1];
		int store5[] = new int[1];
		int mark[] = new int[n + 1];
		int trail[] = new int[n + 1];
		int descendant[] = new int[n + 1];
		int firstlow[] = new int[n + 1];
		int secondlow[] = new int[n + 1];
		int nodebegin[] = new int[n + n + 1];
		int wkpathfind5[] = new int[m + 1];
		int wkpathfind6[] = new int[m + 1];
		int stackarc[] = new int[m + m + 1];
		int stackcolor1[] = new int[m + m + 3];
		int stackcolor2[] = new int[m + m + 3];
		int stackcolor3[] = new int[m + m + 3];
		int stackcolor4[] = new int[m + m + 3];
		int wkpathfind1[] = new int[m + m + 3];
		int wkpathfind2[] = new int[m + m + 3];
		int wkpathfind3[] = new int[m + m + m + 4];
		int wkpathfind4[] = new int[m + m + m + 4];
		int first[] = new int[n + m + m + 1];
		int second[] = new int[n + m + m + 1];
		int sortn[] = new int[n + n + m + 1];
		int sortptr1[] = new int[n + n + m + 1];
		int sortptr2[] = new int[n + n + m + 1];
		int start[] = new int[m - n + 3];
		int finish[] = new int[m - n + 3];
		int paint[] = new int[m - n + 3];
		int nextarc[] = new int[7 * m - 5 * n + 3];
		int arctop[] = new int[7 * m - 5 * n + 3];
		boolean middle[] = new boolean[1];
		boolean fail[] = new boolean[1];
		boolean examin[] = new boolean[m - n + 3];
		boolean arctype[] = new boolean[7 * m - 5 * n + 3];

		// check for the necessary condition
		if (m > 3 * n - 6)
			return false;

		n2 = n + n;
		m2 = m + m;
		nm2 = n + m + m;
		n2m = n + n + m;
		m22 = m + m + 2;
		m33 = m + m + m + 3;
		nmp2 = m - n + 2;
		m7n5 = 7 * m - 5 * n + 2;

		// set up graph representation
		for (i = 1; i <= n; i++)
			second[i] = 0;
		mtotal = n;
		for (i = 1; i <= m; i++) {
			node1 = nodei[i];
			node2 = nodej[i];
			mtotal++;
			second[mtotal] = second[node1];
			second[node1] = mtotal;
			first[mtotal] = node2;
			mtotal++;
			second[mtotal] = second[node2];
			second[node2] = mtotal;
			first[mtotal] = node1;
		}

		// initial depth-first search, compute low point functions
		for (i = 1; i <= n; i++) {
			mark[i] = 0;
			firstlow[i] = n + 1;
			secondlow[i] = n + 1;
		}
		snum[0] = 1;
		store1[0] = 0;
		mark[1] = 1;
		wkpathfind5[1] = 1;
		wkpathfind6[1] = 0;
		level[0] = 1;
		middle[0] = false;
		do {
			planarityDFS1(n, m, m2, nm2, level, middle, snum, store1, mark, firstlow,
					secondlow, wkpathfind5, wkpathfind6, stackarc, first, second);
		} while (level[0] > 1);
		for (i = 1; i <= n; i++)
			if (secondlow[i] >= mark[i]) secondlow[i] = firstlow[i];

		// radix sort
		mtotal = n2;
		k = n2;
		for (i = 1; i <= n2; i++)
			sortn[i] = 0;

		for (i = 2; i <= m2; i += 2) {
			k++;
			sortptr1[k] = stackarc[i - 1];
			tnode = stackarc[i];
			sortptr2[k] = tnode;
			if (mark[tnode] < mark[sortptr1[k]]) {
				j = 2 * mark[tnode] - 1;
				sortn[k] = sortn[j];
				sortn[j] = k;
			} else {
				if (secondlow[tnode] >= mark[sortptr1[k]]) {
					j = 2 * firstlow[tnode] - 1;
					sortn[k] = sortn[j];
					sortn[j] = k;
				} else {
					j = 2 * firstlow[tnode];
					sortn[k] = sortn[j];
					sortn[j] = k;
				}
			}
		}
		for (i = 1; i <= n2; i++) {
			j = sortn[i];
			while (j != 0) {
				node1 = sortptr1[j];
				node2 = sortptr2[j];
				mtotal++;
				second[mtotal] = second[node1];
				second[node1] = mtotal;
				first[mtotal] = node2;
				j = sortn[j];
			}
		}
		// second depth-first search
		for (i = 2; i <= n; i++)
			mark[i] = 0;
		store1[0] = 0;
		snum[0] = 1;
		trail[1] = 1;
		wkpathfind5[1] = 1;
		start[1] = 0;
		finish[1] = 0;
		level[0] = 1;
		middle[0] = false;
		do {
			planarityDFS2(n, m, m2, nm2, level, middle, snum, store1, mark,
					wkpathfind5, stackarc, first, second);
		} while (level[0] > 1);

		mtotal = n;
		for (i = 1; i <= m; i++) {
			j = i + i;
			node1 = stackarc[j - 1];
			node2 = stackarc[j];
			mtotal++;
			second[mtotal] = second[node1];
			second[node1] = mtotal;
			first[mtotal] = node2;
		}
		// path decomposition, construction of the dependency graph
		store2[0] = 0;
		store3[0] = 0;
		store4[0] = 0;
		store5[0] = 0;
		initp[0] = 0;
		pnum[0] = 1;
		wkpathfind1[1] = 0;
		wkpathfind1[2] = 0;
		wkpathfind2[1] = 0;
		wkpathfind2[2] = 0;
		wkpathfind3[1] = 0;
		wkpathfind3[2] = n + 1;
		wkpathfind3[3] = 0;
		wkpathfind4[1] = 0;
		wkpathfind4[2] = n + 1;
		wkpathfind4[3] = 0;
		for (i = 1; i <= n2; i++)
			nodebegin[i] = 0;
		nexte[0] = m - n + 1;
		for (i = 1; i <= m7n5; i++)
			nextarc[i] = 0;
		snode[0] = n;
		descendant[1] = n;
		wkpathfind5[1] = 1;
		level[0] = 1;
		middle[0] = false;
		do {
			planarityDecompose(n, m, n2, m22, m33, nm2, nmp2, m7n5, level, middle, initp,
					snode, pnum, nexte, store2, store3, store4, store5, trail, descendant,
					nodebegin, wkpathfind5, start, finish, first, second, wkpathfind1,
					wkpathfind2, wkpathfind3, wkpathfind4, nextarc, arctop, arctype);
		} while (level[0] > 1);
		// perform two-coloring
		pnum[0]--;
		for (i = 1; i <= nmp2; i++)
			paint[i] = 0;
		j = pnum[0] + 1;
		for (i = 2; i <= j; i++)
			examin[i] = true;

		tnum = 1;
		while (tnum <= pnum[0]) {
			wkpathfind5[1] = tnum;
			paint[tnum] = 1;
			examin[tnum] = false;
			level[0] = 1;
			middle[0] = false;
			do {
				planarityTwoColoring(m, nmp2, m7n5, level, middle, fail, wkpathfind5,
						paint, nextarc, arctop, examin, arctype);
				if (fail[0])
					return false;
			} while (level[0] > 1);
			while (!examin[tnum])
				tnum++;
		}
		aux1 = 0;
		aux2 = 0;
		aux3 = 0;
		aux4 = 0;
		stackcolor1[1] = 0;
		stackcolor1[2] = 0;
		stackcolor2[1] = 0;
		stackcolor2[2] = 0;
		stackcolor3[1] = 0;
		stackcolor3[2] = 0;
		stackcolor4[1] = 0;
		stackcolor4[2] = 0;
		for (i = 1; i <= pnum[0]; i++) {
			qnode = start[i + 1];
			tnode = finish[i + 1];
			while (qnode <= stackcolor1[aux1 + 2])
				aux1 -= 2;
			while (qnode <= stackcolor2[aux2 + 2])
				aux2 -= 2;
			while (qnode <= stackcolor3[aux3 + 2])
				aux3 -= 2;
			while (qnode <= stackcolor4[aux4 + 2])
				aux4 -= 2;
			if (paint[i] == 1) {
				if (finish[trail[qnode] + 1] != tnode) {
					if (tnode < stackcolor2[aux2 + 2])
						return false;
					if (tnode < stackcolor3[aux3 + 2])
						return false;
					aux3 += 2;
					stackcolor3[aux3 + 1] = i;
					stackcolor3[aux3 + 2] = tnode;
				} else {

					if ((tnode < stackcolor3[aux3 + 2]) &&
							(start[stackcolor3[aux3 + 1] + 1] <= descendant[qnode]))
						return false;
					aux1 += 2;
					stackcolor1[aux1 + 1] = i;
					stackcolor1[aux1 + 2] = qnode;
				}
			} else {
				if (finish[trail[qnode] + 1] != tnode) {
					if (tnode < stackcolor1[aux1 + 2])
						return false;
					if (tnode < stackcolor4[aux4 + 2])
						return false;
					aux4 += 2;
					stackcolor4[aux4 + 1] = i;
					stackcolor4[aux4 + 2] = tnode;
				} else {
					if ((tnode < stackcolor4[aux4 + 2]) &&
							(start[stackcolor4[aux4 + 1] + 1] <= descendant[qnode]))
						return false;
					aux2 += 2;
					stackcolor2[aux2 + 1] = i;
					stackcolor2[aux2 + 2] = qnode;
				}
			}
		}
		return true;
	}

	static void planarityDFS1(int n, int m, int m2, int nm2, int level[],
							  boolean middle[], int snum[], int store1[], int mark[],
							  int firstlow[], int secondlow[], int wkpathfind5[], int wkpathfind6[],
							  int stackarc[], int first[], int second[]) {
		/* this method is used internally by planarityTesting */
		int pnode = 0, qnode = 0, tnode = 0, tmp1, tmp2;
		boolean skip;
		skip = false;
		if (middle[0]) skip = true;
		if (!skip) {
			qnode = wkpathfind5[level[0]];
			pnode = wkpathfind6[level[0]];
		}
		while (second[qnode] > 0 || skip) {
			if (!skip) {
				tnode = first[second[qnode]];
				second[qnode] = second[second[qnode]];
			}
			if (((mark[tnode] < mark[qnode]) && (tnode != pnode)) || skip) {
				if (!skip) {
					store1[0] += 2;
					stackarc[store1[0] - 1] = qnode;
					stackarc[store1[0]] = tnode;
				}
				if ((mark[tnode] == 0) || skip) {
					if (!skip) {
						snum[0]++;
						mark[tnode] = snum[0];
						level[0]++;
						wkpathfind5[level[0]] = tnode;
						wkpathfind6[level[0]] = qnode;
						middle[0] = false;
						return;
					}
					skip = false;
					tnode = wkpathfind5[level[0]];
					qnode = wkpathfind6[level[0]];
					level[0]--;
					pnode = wkpathfind6[level[0]];
					if (firstlow[tnode] < firstlow[qnode]) {
						tmp1 = secondlow[tnode];
						tmp2 = firstlow[qnode];
						secondlow[qnode] = (tmp1 < tmp2 ? tmp1 : tmp2);
						firstlow[qnode] = firstlow[tnode];
					} else {
						if (firstlow[tnode] == firstlow[qnode]) {
							tmp1 = secondlow[tnode];
							tmp2 = secondlow[qnode];
							secondlow[qnode] = (tmp1 < tmp2 ? tmp1 : tmp2);
						} else {
							tmp1 = firstlow[tnode];
							tmp2 = secondlow[qnode];
							secondlow[qnode] = (tmp1 < tmp2 ? tmp1 : tmp2);
						}
					}
				} else {
					if (mark[tnode] < firstlow[qnode]) {
						secondlow[qnode] = firstlow[qnode];
						firstlow[qnode] = mark[tnode];
					} else {
						if (mark[tnode] > firstlow[qnode]) {
							tmp1 = mark[tnode];
							tmp2 = secondlow[qnode];
							secondlow[qnode] = (tmp1 < tmp2 ? tmp1 : tmp2);
						}
					}
				}
			}
		}
		middle[0] = true;
	}

	static void planarityDFS2(int n, int m, int m2, int nm2, int level[],
							  boolean middle[], int snum[], int store1[], int mark[],
							  int wkpathfind5[], int stackarc[], int first[], int second[]) {
		/* this method is used internally by planarityTesting */
		int qnode, tnode;
		if (middle[0]) {
			tnode = wkpathfind5[level[0]];
			level[0]--;
			qnode = wkpathfind5[level[0]];
			store1[0] += 2;
			stackarc[store1[0] - 1] = mark[qnode];
			stackarc[store1[0]] = mark[tnode];
		} else
			qnode = wkpathfind5[level[0]];
		while (second[qnode] > 0) {
			tnode = first[second[qnode]];
			second[qnode] = second[second[qnode]];
			if (mark[tnode] == 0) {
				snum[0]++;
				mark[tnode] = snum[0];
				level[0]++;
				wkpathfind5[level[0]] = tnode;
				middle[0] = false;
				return;
			}
			store1[0] += 2;
			stackarc[store1[0] - 1] = mark[qnode];
			stackarc[store1[0]] = mark[tnode];
		}
		middle[0] = true;
	}

	static void planarityDecompose(int n, int m, int n2, int m22, int m33,
								   int nm2, int nmp2, int m7n5, int level[], boolean middle[],
								   int initp[], int snode[], int pnum[], int nexte[], int store2[],
								   int store3[], int store4[], int store5[], int trail[],
								   int descendant[], int nodebegin[], int wkpathfind5[], int start[],
								   int finish[], int first[], int second[], int wkpathfind1[],
								   int wkpathfind2[], int wkpathfind3[], int wkpathfind4[], int nextarc[],
								   int arctop[], boolean arctype[]) {
		/* this method is used internally by planarityTesting */
		int node1, node2, qnode = 0, qnode2, tnode = 0, tnode2;
		boolean ind, skip;
		skip = false;
		if (middle[0]) skip = true;
		if (!skip) qnode = wkpathfind5[level[0]];
		while ((second[qnode] != 0) || skip) {
			if (!skip) {
				tnode = first[second[qnode]];
				second[qnode] = second[second[qnode]];
				if (initp[0] == 0) initp[0] = qnode;
			}
			if ((tnode > qnode) || skip) {
				if (!skip) {
					descendant[tnode] = snode[0];
					trail[tnode] = pnum[0];
					level[0]++;
					wkpathfind5[level[0]] = tnode;
					middle[0] = false;
					return;
				}
				skip = false;
				tnode = wkpathfind5[level[0]];
				level[0]--;
				qnode = wkpathfind5[level[0]];
				snode[0] = tnode - 1;
				initp[0] = 0;
				while (qnode <= wkpathfind2[store3[0] + 2])
					store3[0] -= 2;
				while (qnode <= wkpathfind1[store2[0] + 2])
					store2[0] -= 2;
				while (qnode <= wkpathfind3[store4[0] + 3])
					store4[0] -= 3;
				while (qnode <= wkpathfind4[store5[0] + 3])
					store5[0] -= 3;
				ind = false;
				qnode2 = qnode + qnode;
				while ((nodebegin[qnode2 - 1] > wkpathfind3[store4[0] + 2]) &&
						(qnode < wkpathfind3[store4[0] + 2]) &&
						(nodebegin[qnode2] < wkpathfind3[store4[0] + 1])) {
					ind = true;
					node1 = nodebegin[qnode2];
					node2 = wkpathfind3[store4[0] + 1];
					nexte[0]++;
					nextarc[nexte[0]] = nextarc[node1];
					nextarc[node1] = nexte[0];
					arctop[nexte[0]] = node2;
					node1 = wkpathfind3[store4[0] + 1];
					node2 = nodebegin[qnode2];
					nexte[0]++;
					nextarc[nexte[0]] = nextarc[node1];
					nextarc[node1] = nexte[0];
					arctop[nexte[0]] = node2;
					arctype[nexte[0] - 1] = false;
					arctype[nexte[0]] = false;
					store4[0] -= 3;
				}
				if (ind) store4[0] += 3;
				nodebegin[qnode2 - 1] = 0;
				nodebegin[qnode2] = 0;
			} else {
				start[pnum[0] + 1] = initp[0];
				finish[pnum[0] + 1] = tnode;
				ind = false;
				if (wkpathfind1[store2[0] + 2] != 0) {
					store3[0] += 2;
					wkpathfind2[store3[0] + 1] = wkpathfind1[store2[0] + 1];
					wkpathfind2[store3[0] + 2] = wkpathfind1[store2[0] + 2];
				}
				if (finish[wkpathfind1[store2[0] + 1] + 1] != tnode) {
					while (tnode < wkpathfind2[store3[0] + 2]) {
						node1 = pnum[0];
						node2 = wkpathfind2[store3[0] + 1];
						nexte[0]++;
						nextarc[nexte[0]] = nextarc[node1];
						nextarc[node1] = nexte[0];
						arctop[nexte[0]] = node2;
						node1 = wkpathfind2[store3[0] + 1];
						node2 = pnum[0];
						nexte[0]++;
						nextarc[nexte[0]] = nextarc[node1];
						nextarc[node1] = nexte[0];
						arctop[nexte[0]] = node2;
						arctype[nexte[0] - 1] = true;
						arctype[nexte[0]] = true;
						ind = true;
						store3[0] -= 2;
					}

					if (ind) store3[0] += 2;
					ind = false;
					while ((tnode < wkpathfind3[store4[0] + 3]) &&
							(initp[0] < wkpathfind3[store4[0] + 2])) {
						node1 = pnum[0];
						node2 = wkpathfind3[store4[0] + 1];
						nexte[0]++;
						nextarc[nexte[0]] = nextarc[node1];
						nextarc[node1] = nexte[0];
						arctop[nexte[0]] = node2;
						node1 = wkpathfind3[store4[0] + 1];
						node2 = pnum[0];
						nexte[0]++;
						nextarc[nexte[0]] = nextarc[node1];
						nextarc[node1] = nexte[0];
						arctop[nexte[0]] = node2;
						arctype[nexte[0] - 1] = false;
						arctype[nexte[0]] = false;
						store4[0] -= 3;
					}
					while ((tnode < wkpathfind4[store5[0] + 3]) &&
							(initp[0] < wkpathfind4[store5[0] + 2]))
						store5[0] -= 3;
					tnode2 = tnode + tnode;
					if (initp[0] > nodebegin[tnode2 - 1]) {
						nodebegin[tnode2 - 1] = initp[0];
						nodebegin[tnode2] = pnum[0];
					}
					store4[0] += 3;
					wkpathfind3[store4[0] + 1] = pnum[0];
					wkpathfind3[store4[0] + 2] = initp[0];
					wkpathfind3[store4[0] + 3] = tnode;
					store5[0] += 3;
					wkpathfind4[store5[0] + 1] = pnum[0];
					wkpathfind4[store5[0] + 2] = initp[0];
					wkpathfind4[store5[0] + 3] = tnode;
				} else {
					while ((tnode < wkpathfind4[store5[0] + 3]) &&
							(initp[0] < wkpathfind4[store5[0] + 2]) &&
							(wkpathfind4[store5[0] + 2] <= descendant[initp[0]])) {
						ind = true;
						node1 = pnum[0];
						node2 = wkpathfind4[store5[0] + 1];
						nexte[0]++;
						nextarc[nexte[0]] = nextarc[node1];
						nextarc[node1] = nexte[0];
						arctop[nexte[0]] = node2;
						node1 = wkpathfind4[store5[0] + 1];
						node2 = pnum[0];


						nexte[0]++;
						nextarc[nexte[0]] = nextarc[node1];
						nextarc[node1] = nexte[0];
						arctop[nexte[0]] = node2;
						arctype[nexte[0] - 1] = false;
						arctype[nexte[0]] = false;
						store5[0] -= 3;
					}
					if (ind) store5[0] += 3;
				}
				if (qnode != initp[0]) {
					store2[0] += 2;
					wkpathfind1[store2[0] + 1] = pnum[0];
					wkpathfind1[store2[0] + 2] = initp[0];
				}
				pnum[0]++;
				initp[0] = 0;
			}
		}
		middle[0] = true;
	}

	static void planarityTwoColoring(int m, int nmp2, int m7n5, int level[],
									 boolean middle[], boolean fail[], int wkpathfind5[], int paint[],
									 int nextarc[], int arctop[], boolean examin[], boolean arctype[]) {
		/* this method is used internally by planarityTesting */
		int link, qnode, tnode;
		boolean dum1, dum2;
		fail[0] = false;
		if (middle[0]) {
			level[0]--;
			qnode = wkpathfind5[level[0]];
		} else
			qnode = wkpathfind5[level[0]];
		while (nextarc[qnode] != 0) {
			link = nextarc[qnode];
			tnode = arctop[link];
			nextarc[qnode] = nextarc[link];
			if (paint[tnode] == 0)
				paint[tnode] = (arctype[link] ? paint[qnode] : 3 - paint[qnode]);
			else {
				dum1 = (paint[tnode] == paint[qnode]);
				dum2 = !arctype[link];
				if ((dum1 && dum2) || (!dum1 && !dum2)) {
					fail[0] = true;
					return;
				}
			}
			if (examin[tnode]) {
				examin[tnode] = false;
				level[0]++;
				wkpathfind5[level[0]] = tnode;
				middle[0] = false;
				return;
			}
		}
		middle[0] = true;
	}

	public static void main(String args[]) {
		int n = 7;
		int m = 11;
		int nodei[] = {0, 1, 5, 2, 7, 1, 3, 5, 7, 4, 1, 1};
		int nodej[] = {0, 5, 2, 4, 4, 7, 5, 6, 3, 6, 4, 2};
		if (isPlanar(n, m, nodei, nodej))
			System.out.println("The input graph is planar.");
		else
			System.out.println("The input graph is nonplanar.");
	}
}