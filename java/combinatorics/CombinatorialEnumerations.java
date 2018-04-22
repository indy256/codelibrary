package combinatorics;

import java.util.Arrays;

public class CombinatorialEnumerations {

    // subclass and implement count() method
    public static abstract class AbstractEnumeration {

        // range of definition of sequence elements
        protected final int range;

        // length of generated sequences
        protected final int length;

        protected AbstractEnumeration(int range, int length) {
            this.range = range;
            this.length = length;
        }

        // returns number of combinatorial sequences starting with prefix
        // by contract only the last element of prefix can be invalid and in this case 0 must be returned
        protected abstract long count(int[] prefix);

        public int[] next(int[] sequence) {
            return fromNumber(toNumber(sequence) + 1);
        }

        public long totalCount() {
            return count(new int[0]);
        }

        public long toNumber(int[] sequence) {
            long res = 0;
            for (int i = 0; i < sequence.length; i++) {
                int[] prefix = Arrays.copyOf(sequence, i + 1);
                for (prefix[i] = 0; prefix[i] < sequence[i]; ++prefix[i]) {
                    res += count(prefix);
                }
            }
            return res;
        }

        public int[] fromNumber(long number) {
            int[] sequence = new int[length];
            for (int i = 0; i < sequence.length; i++) {
                int[] prefix = Arrays.copyOf(sequence, i + 1);
                for (prefix[i] = 0; prefix[i] < range; ++prefix[i]) {
                    long cur = count(prefix);
                    if (number < cur) {
                        break;
                    }
                    number -= cur;
                }
                sequence[i] = prefix[i];
            }
            return sequence;
        }

        public void enumerate() {
            System.out.println(getClass().getName());
            long total = totalCount();
            for (long i = 0; i < total; i++) {
                int[] p = fromNumber(i);
                System.out.println(i + " " + Arrays.toString(p));
                long j = toNumber(p);
                if (i != j)
                    throw new RuntimeException();
            }
        }
    }

    public static class Arrangements extends AbstractEnumeration {

        public Arrangements(int n, int k) {
            super(n, k);
        }

        @Override
        protected long count(int[] prefix) {
            int size = prefix.length;

            // if the last element appears twice, then prefix is invalid and 0 must be returned
            for (int i = 0; i < size - 1; i++)
                if (prefix[size - 1] == prefix[i])
                    return 0;

            long res = 1;
            for (int i = 0; i < length - size; i++)
                res *= range - size - i;
            return res;
        }
    }

    public static class Permutations extends Arrangements {
        public Permutations(int n) {
            super(n, n);
        }
    }

    public static class Combinations extends AbstractEnumeration {

        final long[][] binomial;

        public Combinations(int n, int k) {
            super(n, k);
            binomial = new long[n + 1][n + 1];
            // calculate binomial coefficients in advance
            for (int i = 0; i <= n; i++)
                for (int j = 0; j <= i; j++)
                    binomial[i][j] = (j == 0) ? 1 : binomial[i - 1][j - 1] + binomial[i - 1][j];
        }

        @Override
        protected long count(int[] prefix) {
            int size = prefix.length;

            // if there is no combination with given prefix, 0 must be returned.
            // by contract only the last element can make prefix invalid.
            if (size >= 2 && prefix[size - 1] <= prefix[size - 2])
                return 0;

            // prefix is valid. return the number of combinations starting with prefix
            int last = size > 0 ? prefix[size - 1] : -1;
            return binomial[range - 1 - last][length - size];
        }
    }

    public static class CorrectBracketSequences extends AbstractEnumeration {

        final long[][] d;

        // sequenceLength must be a multiple of 2
        public CorrectBracketSequences(int sequenceLength) {
            super(2, sequenceLength);
            d = new long[sequenceLength + 1][sequenceLength / 2 + 1];
            // d[i][j] - number of bracket sequences of length i with balance j
            d[0][0] = 1;
            for (int i = 1; i <= sequenceLength; i++) {
                for (int balance = 0; balance <= sequenceLength / 2; balance++) {
                    if (balance - 1 >= 0)
                        d[i][balance] += d[i - 1][balance - 1];
                    if (balance + 1 <= sequenceLength / 2)
                        d[i][balance] += d[i - 1][balance + 1];
                }
            }
        }

        @Override
        protected long count(int[] prefix) {
            int size = prefix.length;

            int balance = 0;
            for (int cur : prefix)
                // 0 designates '('
                // 1 designates ')'
                balance += cur == 0 ? 1 : -1;

            if (balance < 0 || balance > length - size)
                return 0;

            return d[length - size][balance];
        }
    }

    public static class Partitions extends AbstractEnumeration {

        final long[][] pp;

        public Partitions(int value) {
            super(value + 1, value);
            long[][] p = new long[value + 1][value + 1];
            // p[i][j] - number of partitions of i with largest summand equal to j
            p[0][0] = 1;
            for (int i = 1; i <= value; i++)
                for (int j = 1; j <= i; j++)
                    p[i][j] = p[i - 1][j - 1] + p[i - j][j];
            pp = new long[value + 1][value + 1];
            for (int i = 1; i <= value; i++)
                for (int j = 1; j <= value; j++)
                    pp[i][j] = p[i][j] + pp[i][j - 1];
        }

        @Override
        protected long count(int[] prefix) {
            int size = prefix.length;
            int sum = 0;
            for (int e : prefix)
                sum += e;

            if (sum == range - 1)
                return 1;

            if (sum > range - 1 || size > 0 && prefix[size - 1] == 0 || size >= 2 && prefix[size - 1] > prefix[size - 2])
                return 0;

            int last = size > 0 ? prefix[size - 1] : range - 1;
            return pp[range - 1 - sum][last];
        }
    }

    public static void main(String[] args) {
        Permutations permutations = new Permutations(3);
        permutations.enumerate();

        Combinations combinations = new Combinations(4, 3);
        combinations.enumerate();

        Arrangements arrangements = new Arrangements(3, 2);
        arrangements.enumerate();

        CorrectBracketSequences correctBracketSequences = new CorrectBracketSequences(6);
        correctBracketSequences.enumerate();

        Partitions partitions = new Partitions(4);
        partitions.enumerate();
    }
}
