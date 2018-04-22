package structures;

public class DisjointSetsRank {

    int[] p;
    int[] rank;

    public DisjointSetsRank(int size) {
        p = new int[size];
        for (int i = 0; i < size; i++)
            p[i] = i;
        rank = new int[size];
    }

    public int root(int x) {
        return x == p[x] ? x : (p[x] = root(p[x]));
    }

    public void unite(int a, int b) {
        a = root(a);
        b = root(b);
        if (a == b)
            return;

        if (rank[a] < rank[b]) {
            p[a] = b;
        } else {
            p[b] = a;
            if (rank[a] == rank[b])
                ++rank[a];
        }
    }

    public static void main(String[] args) {
        DisjointSetsRank ds = new DisjointSetsRank(10);
        System.out.println(false == (ds.root(0) == ds.root(9)));
        ds.unite(0, 9);
        System.out.println(true == (ds.root(0) == ds.root(9)));
    }
}
