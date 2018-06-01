package misc;

import java.util.*;
import java.util.function.IntFunction;

public class FunctionCycleDetection {

    public static int[] floyd(IntFunction<Integer> f, int x0) {
        int tortoise = f.apply(x0);
        int hare = f.apply(f.apply(x0));

        while (tortoise != hare) {
            tortoise = f.apply(tortoise);
            hare = f.apply(f.apply(hare));
        }

        int startPos = 0;
        tortoise = x0;
        while (tortoise != hare) {
            tortoise = f.apply(tortoise);
            hare = f.apply(hare);
            ++startPos;
        }

        int length = 1;
        hare = f.apply(tortoise);
        while (tortoise != hare) {
            hare = f.apply(hare);
            ++length;
        }

        return new int[]{startPos, length};
    }

    public static int[] brent(IntFunction<Integer> f, int x0) {
        int power = 1;
        int length = 1;

        int tortoise = x0;
        int hare = f.apply(x0);

        while (tortoise != hare) {
            if (power == length) {
                tortoise = hare;
                power *= 2;
                length = 0;
            }
            hare = f.apply(hare);
            ++length;
        }

        int startPos = 0;
        tortoise = x0;
        hare = x0;
        for (int i = 0; i < length; i++)
            hare = f.apply(hare);

        while (tortoise != hare) {
            tortoise = f.apply(tortoise);
            hare = f.apply(hare);
            ++startPos;
        }

        return new int[]{startPos, length};
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100_000; step++) {
            int a = rnd.nextInt(100);
            int b = rnd.nextInt(100);
            int c = rnd.nextInt(100) + 1;
            IntFunction<Integer> f = x -> (a * x * x + b) % c;
            int[] floyd = floyd(f, 0);
            int[] brent = brent(f, 0);
            if (!Arrays.equals(floyd, brent))
                throw new RuntimeException();
        }
        System.out.println(floyd(x -> (41 * x + 1) % 1000_000, 0)[1]);
    }
}
