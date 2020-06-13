package structures;

import java.util.*;

// https://cp-algorithms.com/data_structures/stack_queue_modification.html
public class QueueMin {
    List<Integer> s1 = new ArrayList<>();
    List<Integer> s2 = new ArrayList<>();
    int min1 = Integer.MAX_VALUE;

    public int min() {
        return Math.min(s2.isEmpty() ? Integer.MAX_VALUE : s2.get(s2.size() - 1), min1);
    }

    public void addLast(int x) {
        s1.add(x);
        min1 = Math.min(min1, x);
    }

    public void removeFirst() {
        while (!s1.isEmpty()) {
            int x = s1.remove(s1.size() - 1);
            s2.add(s2.isEmpty() ? x : Math.min(x, s2.get(s2.size() - 1)));
        }
        min1 = Integer.MAX_VALUE;
        s2.remove(s2.size() - 1);
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
