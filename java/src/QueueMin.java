import java.util.*;

public class QueueMin<E extends Comparable<E>> {
	List<Comparable[]> s1 = new ArrayList<Comparable[]>();
	List<Comparable[]> s2 = new ArrayList<Comparable[]>();

	static Comparable min(Comparable a, Comparable b) {
		return a.compareTo(b) < 0 ? a : b;
	}

	public E min() {
		if (s1.isEmpty() || s2.isEmpty()) {
			return s1.isEmpty() ? (E) s2.get(s2.size() - 1)[1] : (E) s1.get(s1.size() - 1)[1];
		}
		return (E) min(s1.get(s1.size() - 1)[1], s2.get(s2.size() - 1)[1]);
	}

	public void push_back(E x) {
		Comparable minima = s1.isEmpty() ? x : min(x, s1.get(s1.size() - 1)[1]);
		s1.add(new Comparable[] { x, minima });
	}

	public void pop_front() {
		if (s2.isEmpty()) {
			while (!s1.isEmpty()) {
				Comparable x = s1.remove(s1.size() - 1)[0];
				Comparable minima = s2.isEmpty() ? x : min(x, s2.get(s2.size() - 1)[1]);
				s2.add(new Comparable[] { x, minima });
			}
		}
		s2.remove(s2.size() - 1);
	}

	// Usage example
	public static void main(String[] args) {
		QueueMin<Integer> q = new QueueMin<Integer>();
		q.push_back(2);
		q.push_back(3);
		System.out.println(2 == q.min());
		q.pop_front();
		System.out.println(3 == q.min());
		q.push_back(1);
		System.out.println(1 == q.min());
	}
}
