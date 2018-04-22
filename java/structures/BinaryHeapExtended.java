package structures;

// https://en.wikipedia.org/wiki/Binary_heap
// invariant: heap[parent] <= heap[child]
public class BinaryHeapExtended {
    long[] heap;
    int[] pos2Id;
    int[] id2Pos;
    int size;

    public BinaryHeapExtended(int n) {
        heap = new long[n];
        pos2Id = new int[n];
        id2Pos = new int[n];
    }

    public int remove() {
        int removedId = pos2Id[0];
        heap[0] = heap[--size];
        pos2Id[0] = pos2Id[size];
        id2Pos[pos2Id[0]] = 0;
        down(0);
        return removedId;
    }

    public void add(int id, long value) {
        heap[size] = value;
        pos2Id[size] = id;
        id2Pos[id] = size;
        up(size++);
    }

    public void changePriority(int id, long value) {
        int pos = id2Pos[id];
        if (heap[pos] < value) {
            heap[pos] = value;
            down(pos);
        } else if (heap[pos] > value) {
            heap[pos] = value;
            up(pos);
        }
    }

    public void remove(int id) {
        int pos = id2Pos[id];
        pos2Id[pos] = pos2Id[--size];
        id2Pos[pos2Id[pos]] = pos;
        changePriority(pos2Id[pos], heap[size]);
    }

    void up(int pos) {
        while (pos > 0) {
            int parent = (pos - 1) / 2;
            if (heap[pos] >= heap[parent])
                break;
            swap(pos, parent);
            pos = parent;
        }
    }

    void down(int pos) {
        while (true) {
            int child = 2 * pos + 1;
            if (child >= size)
                break;
            if (child + 1 < size && heap[child + 1] < heap[child])
                ++child;
            if (heap[pos] <= heap[child])
                break;
            swap(pos, child);
            pos = child;
        }
    }

    void swap(int i, int j) {
        long tt = heap[i];
        heap[i] = heap[j];
        heap[j] = tt;
        int t = pos2Id[i];
        pos2Id[i] = pos2Id[j];
        pos2Id[j] = t;
        id2Pos[pos2Id[i]] = i;
        id2Pos[pos2Id[j]] = j;
    }

    // Usage example
    public static void main(String[] args) {
        BinaryHeapExtended heap = new BinaryHeapExtended(10);
        heap.add(0, 4);
        heap.add(1, 5);
        heap.add(2, 2);

        heap.changePriority(1, 3);
        heap.changePriority(2, 6);
        heap.remove(0);

        // print elements in sorted order
        while (heap.size != 0) {
            System.out.println(heap.heap[0] + " " + heap.remove());
        }
    }
}