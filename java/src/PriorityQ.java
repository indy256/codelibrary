import java.util.Comparator;
import java.util.PriorityQueue;

import mooc.EdxIO;

public class PriorityQ {

	//Comparator anonymous class implementation
	public static Comparator<Pair2<Integer,Integer>> pairComparator = 
			new Comparator<Pair2<Integer,Integer>>(){
		
		@Override
		public int compare(Pair2<Integer,Integer> p1, Pair2<Integer,Integer> p2) {
            return p2.compareTo(p1);
        }
	};
	
	public static void main(String[] args) {
		try (EdxIO io = EdxIO.create()) {
			int n = io.nextInt();
			
			PriorityQueue<Pair2<Integer,Integer>> q = 
					new PriorityQueue<Pair2<Integer,Integer>>(n, pairComparator);
			
			for (int i = 0; i < n; i++) {
				q.add(new Pair2<Integer,Integer>(io.nextInt(), 0));
			}
			
			int drying = io.nextInt();
			int increments = 0;

			Pair2<Integer,Integer> top = q.poll();
			
			while (top != null && top.u + top.v > increments) {
				int diff = top.u - drying;
				if (diff + top.v > 0) {
					q.add(new Pair2<Integer,Integer>(top.u - drying, top.v + 1));
				}
				increments++;
				
				top = q.poll();
			}
			
			io.println(increments);
		}
	}
}

class Pair2<U extends Comparable<U>, V extends Comparable<V>> implements Comparable<Pair2<U, V>> {
	final U u;
	final V v;

	public Pair2(U u, V v) {
		this.u = u;
		this.v = v;
	}

	public int hashCode() {
		return (u == null ? 0 : u.hashCode() * 31) + (v == null ? 0 : v.hashCode());
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Pair2<U, V> p = (Pair2<U, V>) o;
		return (u == null ? p.u == null : u.equals(p.u)) && (v == null ? p.v == null : v.equals(p.v));
	}

	public int compareTo(Pair2<U, V> b) {
		int cmpU = u.compareTo(b.u);
		return cmpU != 0 ? cmpU : v.compareTo(b.v);
	}
}
