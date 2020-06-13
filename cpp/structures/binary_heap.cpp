#include <bits/stdc++.h>

using namespace std;

template <class T>
struct binary_heap {
    vector<T> heap;
    vector<int> pos2Id;
    vector<int> id2Pos;
    int size;

    binary_heap() : size(0) {}

    binary_heap(int n) : heap(n), pos2Id(n), id2Pos(n), size(0) {}

    void add(int id, T value) {
        heap[size] = value;
        pos2Id[size] = id;
        id2Pos[id] = size;
        up(size++);
    }

    int remove_min() {
        int removedId = pos2Id[0];
        heap[0] = heap[--size];
        pos2Id[0] = pos2Id[size];
        id2Pos[pos2Id[0]] = 0;
        down(0);
        return removedId;
    }

    void remove(int id) {
        int pos = id2Pos[id];
        pos2Id[pos] = pos2Id[--size];
        id2Pos[pos2Id[pos]] = pos;
        changePriority(pos2Id[pos], heap[size]);
    }

    void changePriority(int id, T value) {
        int pos = id2Pos[id];
        if (heap[pos] < value) {
            heap[pos] = value;
            down(pos);
        } else if (heap[pos] > value) {
            heap[pos] = value;
            up(pos);
        }
    }

    void up(int pos) {
        while (pos > 0) {
            int parent = (pos - 1) / 2;
            if (heap[pos] >= heap[parent])
                break;
            exchange(pos, parent);
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
            exchange(pos, child);
            pos = child;
        }
    }

    void exchange(int i, int j) {
        swap(heap[i], heap[j]);
        swap(pos2Id[i], pos2Id[j]);
        id2Pos[pos2Id[i]] = i;
        id2Pos[pos2Id[j]] = j;
    }
};

// usage example
int main() {
    binary_heap<int> h(5);
    h.add(0, 50);
    h.add(1, 30);
    h.add(2, 40);
    h.changePriority(0, 20);
    h.remove(1);
    while (h.size) {
        cout << h.remove_min() << endl;
    }
}
