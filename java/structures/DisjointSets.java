package structures;

// https://en.wikipedia.org/wiki/Disjoint-set_data_structure with path compression heuristic
// Complexity of operations is O(log(n)) on average
public class DisjointSets {

    public static int[] createSets(int size) {
        int[] p = new int[size];
        for (int i = 0; i < size; i++)
            p[i] = i;
        return p;
    }

    public static int root(int[] p, int x) {
        return x == p[x] ? x : (p[x] = root(p, p[x]));
    }

    public static boolean unite(int[] p, int a, int b) {
        a = root(p, a);
        b = root(p, b);
        if (a != b) {
            p[a] = b;
            return true;
        }
        return false;
    }

    // Usage example
    public static void main(String[] args) {
        int[] p = createSets(10);
        System.out.println(false == (root(p, 0) == root(p, 9)));
        unite(p, 0, 9);
        System.out.println(true == (root(p, 0) == root(p, 9)));
    }
}
