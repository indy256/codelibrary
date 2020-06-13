package combinatorics;

import java.util.*;
import java.util.stream.Stream;

public class SetPartitions {
    public static boolean nextRestrictedGrowthString(int[] a) {
        int n = a.length;
        int[] b = new int[n];
        for (int i = 0; i + 1 < n; i++) {
            b[i + 1] = Math.max(a[i] + 1, b[i]);
        }
        int i = n - 1;
        while (a[i] == b[i]) {
            if (--i < 0)
                return false;
        }
        ++a[i];
        Arrays.fill(a, i + 1, n, 0);
        return true;
    }

    public static int[][] toSets(int[] a) {
        List<Integer>[] sets = Stream.generate(ArrayList::new).limit(a.length).toArray(List[] ::new);
        for (int i = 0; i < a.length; i++) {
            sets[a[i]].add(i);
        }
        return Arrays.stream(sets)
            .filter(s -> !s.isEmpty())
            .map(s -> s.stream().mapToInt(Integer::intValue).toArray())
            .toArray(int[][] ::new);
    }

    // Usage example
    public static void main(String[] args) {
        int[] a = new int[3];
        do {
            System.out.println(Arrays.deepToString(toSets(a)));
        } while (nextRestrictedGrowthString(a));
    }
}
