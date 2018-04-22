package combinatorics;

import java.util.*;

// https://en.wikipedia.org/wiki/Partition_(number_theory)
public class Partitions {

    public static boolean nextPartition(List<Integer> p) {
        int n = p.size();
        if (n <= 1)
            return false;
        int s = p.remove(n - 1) - 1;
        int i = n - 2;
        while (i > 0 && p.get(i).equals(p.get(i - 1))) {
            s += p.remove(i);
            --i;
        }
        p.set(i, p.get(i) + 1);
        while (s-- > 0) {
            p.add(1);
        }
        return true;
    }

    public static List<Integer> partitionByNumber(int n, long number) {
        List<Integer> p = new ArrayList<>();
        for (int x = n; x > 0; ) {
            int j = 1;
            while (true) {
                long cnt = partitionFunction(x)[x][j];
                if (number < cnt)
                    break;
                number -= cnt;
                ++j;
            }
            p.add(j);
            x -= j;
        }
        return p;
    }

    public static long numberByPartition(List<Integer> p) {
        long res = 0;
        int sum = 0;
        for (int x : p) {
            sum += x;
        }
        for (int cur : p) {
            for (int j = 0; j < cur; j++) {
                res += partitionFunction(sum)[sum][j];
            }
            sum -= cur;
        }
        return res;
    }

    public static void generateIncreasingPartitions(int[] p, int left, int last, int pos) {
        if (left == 0) {
            for (int i = 0; i < pos; i++)
                System.out.print(p[i] + " ");
            System.out.println();
            return;
        }
        for (p[pos] = last + 1; p[pos] <= left; p[pos]++)
            generateIncreasingPartitions(p, left - p[pos], p[pos], pos + 1);
    }

    public static long countPartitions(int n) {
        long[] p = new long[n + 1];
        p[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                p[j] += p[j - i];
            }
        }
        return p[n];
    }

    public static long[][] partitionFunction(int n) {
        long[][] p = new long[n + 1][n + 1];
        p[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                p[i][j] = p[i - 1][j - 1] + p[i - j][j];
            }
        }
        return p;
    }

    public static long[][] partitionFunction2(int n) {
        long[][] p = new long[n + 1][n + 1];
        p[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                for (int k = 0; k <= j; k++) {
                    p[i][j] += p[i - j][k];
                }
            }
        }
        return p;
    }

    // Usage example
    public static void main(String[] args) {
        System.out.println(7 == countPartitions(5));
        System.out.println(627 == countPartitions(20));
        System.out.println(5604 == countPartitions(30));
        System.out.println(204226 == countPartitions(50));
        System.out.println(190569292 == countPartitions(100));

        List<Integer> p = new ArrayList<>();
        Collections.addAll(p, 1, 1, 1, 1, 1);
        do {
            System.out.println(p);
        } while (nextPartition(p));

        int[] p1 = new int[8];
        generateIncreasingPartitions(p1, p1.length, 0, 0);

        List<Integer> list = partitionByNumber(5, 6);
        System.out.println(list);

        System.out.println(numberByPartition(list));
    }
}
