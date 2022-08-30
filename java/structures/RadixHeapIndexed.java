package structures;

import java.util.Arrays;

public class RadixHeapIndexed {
    static final int BUCKETS = 32 + 1;
    int[][] values = new int[BUCKETS][1];
    int[][] ids = new int[BUCKETS][1];
    int[] len = new int[BUCKETS];
    int[] minValues;
    int last;
    int ptr;
    public int size;

    public RadixHeapIndexed(int n) {
        this.minValues = new int[n];
        Arrays.fill(minValues, Integer.MAX_VALUE);
    }

    static int lg(int x) {
        return 32 - Integer.numberOfLeadingZeros(x);
    }

    public void add(int id, int value) {
        addItem(id, value);
        ++size;
    }

    void addItem(int id, int value) {
        int bucket = lg(value ^ last);
        ensureCapacity(bucket);
        values[bucket][len[bucket]] = value;
        ids[bucket][len[bucket]] = id;
        ++len[bucket];
        minValues[id] = value;
    }

    void ensureCapacity(int bucket) {
        if (values[bucket].length == len[bucket]) {
            values[bucket] = Arrays.copyOf(values[bucket], len[bucket] * 2);
            ids[bucket] = Arrays.copyOf(ids[bucket], len[bucket] * 2);
        }
    }

    public void changeValue(int id, int value) {
        addItem(id, value);
    }

    public int removeMin() {
        pull();
        --size;
        return ids[0][ptr++];
    }

    void pull() {
        if (ptr < len[0])
            return;
        len[0] = 0;
        ptr = 0;
        int i = 1;
        last = Integer.MAX_VALUE;
        do {
            while (len[i] == 0) ++i;
            for (int j = 0; j < len[i]; j++) {
                if (values[i][j] == minValues[ids[i][j]]) {
                    last = Math.min(last, values[i][j]);
                }
            }
            for (int j = 0; j < len[i]; j++) {
                if (values[i][j] == minValues[ids[i][j]]) {
                    addItem(ids[i][j], values[i][j]);
                }
            }
            len[i] = 0;
        } while (last == Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        RadixHeapIndexed h = new RadixHeapIndexed(10);
        h.add(0, 10);
        h.add(1, 5);
        System.out.println(h.removeMin());
        h.add(2, 9);
        System.out.println(h.removeMin());
        h.add(3, 10);
        h.changeValue(0, 10);
        System.out.println(h.removeMin());
        System.out.println(h.removeMin());
    }
}
