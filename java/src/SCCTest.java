import java.util.*;

public class SCCTest {

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 10_000; step++) {
			int n = rnd.nextInt(30);
			List<Integer>[] g = getRandomGraph(n, rnd);

			List<List<Integer>> scc1 = SCCTransitiveClosure.scc(g);
			List<List<Integer>> scc2 = SCCKosaraju.scc(g);
			List<List<Integer>> scc3 = new SCCTarjan().scc(g);
			List<List<Integer>> scc4 = SCCTarjanNoRecursion.scc(g);

			Set<Set<Integer>> s1 = new HashSet<>();
			for (List<Integer> c : scc1)
				s1.add(new HashSet<>(c));
			Set<Set<Integer>> s2 = new HashSet<>();
			for (List<Integer> c : scc2)
				s2.add(new HashSet<>(c));
			Set<Set<Integer>> s3 = new HashSet<>();
			for (List<Integer> c : scc3)
				s3.add(new HashSet<>(c));
			Set<Set<Integer>> s4 = new HashSet<>();
			for (List<Integer> c : scc4)
				s4.add(new HashSet<>(c));

			if (!s1.equals(s2) || !s1.equals(s3) || !s1.equals(s4))
				throw new RuntimeException();
		}
	}

	static List<Integer>[] getRandomGraph(int n, Random rnd) {
		List<Integer>[] res = new List[n];
		for (int i = 0; i < n; i++) {
			res[i] = new ArrayList<>();
			for (int j = 0; j < n; j++)
				if (rnd.nextBoolean())
					res[i].add(j);
		}
		return res;
	}
}
