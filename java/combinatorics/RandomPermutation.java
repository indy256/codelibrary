package combinatorics;

import java.util.*;

public class RandomPermutation {

    public static void shuffle(int[] a) {
        Random rnd = new Random();
        for (int i = a.length - 1; i >= 1; i--) {
            int j = rnd.nextInt(i + 1);
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }

    public static int[] getRandomPermutation(int n) {
        Random rnd = new Random();
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int j = rnd.nextInt(i + 1);
            res[i] = res[j];
            res[j] = i;
        }
        return res;
    }

    public static int[] getRandomArrangement(int n, int m) {
        Random rnd = new Random();
        int[] res = new int[n];
        for (int i = 0; i < n; i++)
            res[i] = i;
        for (int i = 0; i < m; i++) {
            int j = i + rnd.nextInt(n - i);
            int t = res[i];
            res[i] = res[j];
            res[j] = t;
        }
        return Arrays.copyOf(res, m);
    }

    public static int[] getRandomCombination(int n, int m) {
        int[] res = getRandomArrangement(n, m);
        Arrays.sort(res);
        return res;
    }

    // for small m
    public static int[] getRandomArrangement2(int n, int m) {
        Random rnd = new Random();
        Set<Integer> set = new HashSet<>();
        int[] res = new int[m];
        while (set.size() < m) {
            int x = rnd.nextInt(n);
            if (set.add(x))
                res[set.size() - 1] = x;
        }
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        System.out.println(Arrays.toString(getRandomPermutation(5)));
    }
}
