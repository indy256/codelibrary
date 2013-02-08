import java.util.*;

/**
 * @author Andrey Naumenko
 */
public class ContractionHierarchies {

    final int NODES = 100000;
    final int EDGES = 100000;

    int[] levels = new int[NODES];

    int[] len = new int[EDGES];
    int[] u = new int[EDGES];
    int[] v = new int[EDGES];
    int[] degree = new int[NODES];

    int[][] tail = {new int[NODES], new int[NODES]};
    int[][] prev = {new int[EDGES], new int[EDGES]};

    PriorityQueue<Long> priorities = new PriorityQueue<>();

    {
        Arrays.fill(prev[0], -1);
        Arrays.fill(prev[1], -1);
        Arrays.fill(tail[0], -1);
        Arrays.fill(tail[1], -1);
    }

    int edges = 0;
    int nodes = 0;

    public void addEdge(int s, int t, int len) {
        nodes = Math.max(nodes, s + 1);
        nodes = Math.max(nodes, t + 1);

        ++degree[s];
        ++degree[t];

        for (int edge = tail[0][s]; edge != -1; edge = prev[0][edge]) {
            if (v[edge] == t) {
                this.len[edge] = Math.min(this.len[edge], len);
                return;
            }
        }

        this.len[edges] = len;
        u[edges] = s;
        v[edges] = t;

        // outgoing arc
        prev[0][edges] = tail[0][s];
        tail[0][s] = edges;

        // incoming arc
        prev[1][edges] = tail[1][t];
        tail[1][t] = edges;

        ++edges;
    }

    private Map<Integer, Integer> findWitness(int s, boolean[] targets) {
        int targetCount = 0;
        for (boolean target : targets) if (target) ++targetCount;
        Map<Integer, Integer> prio = new HashMap<>();
        prio.put(s, 0);
        PriorityQueue<Long> q = new PriorityQueue<>();
        q.add((long) s);
        while (!q.isEmpty()) {
            long cur = q.remove();
            int u = (int) cur;
            int priou = prio.get(u);
            if (cur >>> 32 != priou)
                continue;

            if (targets[u]) {
                targets[u] = false;
                if (--targetCount == 0)
                    break;
            }

            for (int edge = tail[0][u]; edge != -1; edge = prev[0][edge]) {
                int v = this.v[edge];
                if (levels[v] < levels[s])
                    continue;
                int nprio = priou + len[edge];
                Integer priov = prio.get(v);
                if (priov == null || priov > nprio) {
                    prio.put(v, nprio);
                    q.add(((long) nprio << 32) + v);
                }
            }
        }
        return prio;
    }

    private int addShortcuts(int v, boolean nonSimulate) {
        boolean[] targets = new boolean[nodes];
        int shortcuts = 0;
        for (int uv = tail[1][v]; uv != -1; uv = prev[1][uv]) {
            int u = this.u[uv];
            if (levels[u] < levels[v])
                continue;

            for (int vw = tail[0][v]; vw != -1; vw = prev[0][vw]) {
                int w = this.v[vw];
                if (levels[w] < levels[v] || u == w)
                    continue;
                targets[w] = true;
            }

            Map<Integer, Integer> prio = findWitness(u, targets);

            for (int vw = tail[0][v]; vw != -1; vw = prev[0][vw]) {
                int w = this.v[vw];
                if (levels[w] < levels[v] || u == w)
                    continue;
                targets[w] = false;

                final Integer priow = prio.get(w);
                if (priow == null || priow > len[uv] + len[vw]) {
                    if (nonSimulate) {
                        addEdge(u, w, len[uv] + len[vw]);
                        ++shortcuts;
//						System.out.println("(" + u + "," + w + ") -> " + (len[uv] + len[vw]));
                    }
                }
            }
        }
        return shortcuts;
    }

    private int calculatePriority(int v) {
        int shortcuts = addShortcuts(v, false);
        int degree = this.degree[v];
        int edgeDifference = shortcuts - degree;
        return edgeDifference;
    }

    private void preprocess() {
        for (int v = 0; v < nodes; v++) {
            int prio = calculatePriority(v);
            priorities.add(((long) prio << 32) + v);
        }
        List<Long> list = new ArrayList<>();
        int level = 0;
        while (!priorities.isEmpty()) {
            long cur = priorities.remove();
            levels[(int) cur] = level++;
            list.add(cur);
        }
        Arrays.fill(levels, Integer.MAX_VALUE);
        priorities.addAll(list);
        for (int i = 0; i < nodes - 2; i++) {
            long cur = priorities.remove();
            int v = (int) cur;
            int prio = calculatePriority(v);
            if (prio > priorities.peek() >>> 32) {
                priorities.add(((long) prio << 32) + v);
                --i;
                continue;
            }
            levels[v] = i;
            addShortcuts(v, true);
        }
    }

    public int shortestPath(int s, int t) {
        int iterations = 0;
        int[][] prio = {new int[nodes], new int[nodes]};
        Arrays.fill(prio[0], Integer.MAX_VALUE / 2);
        Arrays.fill(prio[1], Integer.MAX_VALUE / 2);
        prio[0][s] = 0;
        prio[1][t] = 0;
        PriorityQueue<Long>[] q = new PriorityQueue[]{new PriorityQueue<Long>(), new PriorityQueue<Long>()};
        q[0].add((long) s);
        q[1].add((long) t);
        int res = Integer.MAX_VALUE;
        for (int dir = 0; ; dir = !q[1 - dir].isEmpty() ? 1 - dir : dir) {
            ++iterations;
            if (res <= Math.min(q[0].isEmpty() ? Integer.MAX_VALUE : (int) (q[0].peek() >>> 32), q[1].isEmpty() ? Integer.MAX_VALUE : (int) (q[1].peek() >>> 32)))
                break;
            long cur = q[dir].remove();
            int u = (int) cur;
            if (cur >>> 32 != prio[dir][u])
                continue;
            res = Math.min(res, prio[dir][u] + prio[1 - dir][u]);

            for (int edge = tail[dir][u]; edge != -1; edge = prev[dir][edge]) {
                int v = dir == 0 ? this.v[edge] : this.u[edge];
                if (levels[v] < levels[u])
                    continue;
                int nprio = prio[dir][u] + len[edge];
                if (prio[dir][v] > nprio) {
                    prio[dir][v] = nprio;
                    q[dir].add(((long) nprio << 32) + v);
                }
            }
        }

//		System.out.println(iterations);
        return res;
    }

    public static void main(String[] args) {
        Random rnd = new Random(1);

        for (int step = 0; step < 1000; step++) {
            ContractionHierarchies ch = new ContractionHierarchies();
            int V = rnd.nextInt(50) + 2;
            int E = V + rnd.nextInt(V * (V - 1) - V + 1);
            int[][] d = generateStronglyConnectedDigraph(V, E, rnd);
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (i != j && d[i][j] != Integer.MAX_VALUE / 2) {
                        ch.addEdge(i, j, d[i][j]);
                    }
                }
            }

            int checkE = 0;
            for (int i = 0; i < V; i++) for (int j = 0; j < V; j++) if (d[i][j] != Integer.MAX_VALUE / 2 && i != j) ++checkE;
            if (checkE != E) throw new RuntimeException(E + " " + checkE+" "+ch.edges);

            for (int k = 0; k < V; k++)
                for (int i = 0; i < V; i++)
                    for (int j = 0; j < V; j++)
                        d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);

            for (int i = 0; i < V; i++)
                for (int j = 0; j < V; j++)
                    if (d[i][j] == Integer.MAX_VALUE / 2) throw new RuntimeException();

            int shortcuts = ch.edges;
            ch.preprocess();
            shortcuts = ch.edges - shortcuts;
            System.out.println("edges = " + (ch.edges - shortcuts) + " shortcuts = " + shortcuts + " nodes = " + ch.nodes);

//			ch.debug();
            for (int step1 = 0; step1 < 100; step1++) {
                int a = rnd.nextInt(V);
                int b = rnd.nextInt(V);

                int res1 = ch.shortestPath(a, b);
                int res2 = d[a][b];
                if (res1 != res2) throw new RuntimeException(res1 + " " + res2);
            }
        }
    }

    void debug() {
        for (int edge = 0; edge < edges; edge++) {
            System.out.println("(" + u[edge] + "," + v[edge] + ") = " + len[edge]);
        }
    }

    static int[][] generateStronglyConnectedDigraph(int V, int E, Random rnd) {
        List<Integer> p = new ArrayList<>();
        for (int i = 0; i < V; i++)
            p.add(i);
        while (p.size() < E)
            p.add(rnd.nextInt(V));
        Collections.shuffle(p, rnd);
        for (int i = 0; i < p.size(); i++) {
            int a = p.get((i + p.size() - 1) % p.size());
            int b = p.get(i);
            int c = p.get((i + 1) % p.size());
            while (b == a || b == c)
                b = (b + 1) % V;
            p.set(i, b);
        }
        int[][] d = new int[V][V];
        for (int i = 0; i < V; i++) {
            Arrays.fill(d[i], Integer.MAX_VALUE / 2);
            d[i][i] = 0;
        }
        for (int i = 0; i < p.size(); i++) {
            int a = p.get(i);
            int b = p.get((i + 1) % p.size());
            d[a][b] = rnd.nextInt(10);
        }
        return d;
    }
}
