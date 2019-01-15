package structures;

import java.util.*;

// https://cp-algorithms.com/data_structures/stack_queue_modification.html
public class QueueMin {
    List<int[]> s1 = new ArrayList<>();
    List<int[]> s2 = new ArrayList<>();

    public int min() {
        if (s1.isEmpty())
            return s2.get(s2.size() - 1)[1];
        if (s2.isEmpty())
            return s1.get(s1.size() - 1)[1];
        return Math.min(s1.get(s1.size() - 1)[1], s2.get(s2.size() - 1)[1]);
    }

    public void addLast(int x) {
        int minima = s1.isEmpty() ? x : Math.min(x, s1.get(s1.size() - 1)[1]);
        s1.add(new int[]{x, minima});
    }

    public int removeFirst() {
        if (s2.isEmpty()) {
            while (!s1.isEmpty()) {
                int x = s1.remove(s1.size() - 1)[0];
                int min = s2.isEmpty() ? x : Math.min(x, s2.get(s2.size() - 1)[1]);
                s2.add(new int[]{x, min});
            }
        }
        return s2.remove(s2.size() - 1)[0];
    }

    // Usage example
    public static void main(String[] args) {
        QueueMin q = new QueueMin();
        q.addLast(2);
        q.addLast(3);
        System.out.println(2 == q.min());
        q.removeFirst();
        System.out.println(3 == q.min());
        q.addLast(1);
        System.out.println(1 == q.min());
    }
}
