package structures;

import java.util.*;

public class QueueMin<E extends Comparable<? super E>> {
    List<E[]> s1 = new ArrayList<>();
    List<E[]> s2 = new ArrayList<>();

    E min(E a, E b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    public E min() {
        if (s1.isEmpty())
            return s2.get(s2.size() - 1)[1];
        if (s2.isEmpty())
            return s1.get(s1.size() - 1)[1];
        return min(s1.get(s1.size() - 1)[1], s2.get(s2.size() - 1)[1]);
    }

    public void addLast(E x) {
        E minima = x;
        if (!s1.isEmpty()) {
            minima = min(minima, s1.get(s1.size() - 1)[1]);
        }
        s1.add((E[]) new Comparable[]{x, minima});
    }

    public E removeFirst() {
        if (s2.isEmpty()) {
            E minima = null;
            while (!s1.isEmpty()) {
                E x = s1.remove(s1.size() - 1)[0];
                minima = minima == null ? x : min(minima, x);
                s2.add((E[]) new Comparable[]{x, minima});
            }
        }
        return s2.remove(s2.size() - 1)[0];
    }

    // Usage example
    public static void main(String[] args) {
        QueueMin<Integer> q = new QueueMin<>();
        q.addLast(2);
        q.addLast(3);
        System.out.println(2 == q.min());
        q.removeFirst();
        System.out.println(3 == q.min());
        q.addLast(1);
        System.out.println(1 == q.min());
    }
}
