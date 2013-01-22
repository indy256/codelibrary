import java.util.*;

public class QueueMin<E extends Comparable<E>> {
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

	public void push_back(E x) {
		E minima = s1.isEmpty() ? x : min(x, s1.get(s1.size() - 1)[1]);
		s1.add((E[]) new Comparable[] { x, minima });		
	}

	public void pop_front() {
		if (s2.isEmpty()) {
			while (!s1.isEmpty()) {
				E x = s1.remove(s1.size() - 1)[0];
				E minima = s2.isEmpty() ? x : min(x, s2.get(s2.size() - 1)[1]);
				s2.add((E[]) new Comparable[] { x, minima });
			}
		}
		s2.remove(s2.size() - 1);
	}

	// Usage example
	public static void main(String[] args) {
		QueueMin<Integer> q = new QueueMin<>();
		q.push_back(2);
		q.push_back(3);
		System.out.println(2 == q.min());
		q.pop_front();
		System.out.println(3 == q.min());
		q.push_back(1);
		System.out.println(1 == q.min());
	}
}
