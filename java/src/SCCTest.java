import java.util.*;
import java.util.stream.Collectors;

public class SCCTest {

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 10_000; step++) {
			int n = rnd.nextInt(30);
			List<Integer>[] g = getRandomGraph(n, rnd);

			Set<Set> s1 = SCCTransitiveClosure.scc(g).stream().map(HashSet::new).collect(Collectors.toSet());
			Set<Set> s2 = SCCKosaraju.scc(g).stream().map(HashSet::new).collect(Collectors.toSet());
			Set<Set> s3 = new SCCTarjan().scc(g).stream().map(HashSet::new).collect(Collectors.toSet());
			Set<Set> s4 = SCCTarjanNoRecursion.scc(g).stream().map(HashSet::new).collect(Collectors.toSet());

			if (!s1.equals(s2) || !s1.equals(s3) || !s1.equals(s4))
				throw new RuntimeException();
		}
	}

	static List<Integer>[] getRandomGraph(int n, Random rnd) {
		List<Integer>[] res = new List[n];
		for (int i = 0; i < n; i++) {
			res[i] = new ArrayList<>();
			for (int j = 0; j < n; j++)
				if (j != i && rnd.nextBoolean())
					res[i].add(j);
		}
		return res;
	}
}
