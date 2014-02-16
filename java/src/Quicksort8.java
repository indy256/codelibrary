import java.util.*;
import java.util.stream.Collectors;

public class Quicksort8 {

	public static List<Integer> quickSort(List<Integer> a) {
		if (a.size() <= 1)
			return a;

		int pivot = a.get(a.size() / 2);

		List<Integer> l1 = quickSort(a.stream().filter(x -> x < pivot).collect(Collectors.toList()));
		List<Integer> l2 = a.stream().filter(x -> x == pivot).collect(Collectors.toList());
		List<Integer> l3 = quickSort(a.stream().filter(x -> x > pivot).collect(Collectors.toList()));

		l1.addAll(l2);
		l1.addAll(l3);

		return l1;
	}

	// test
	public static void main(String[] args) {
		int n = 10_000_000;
		List<Integer> a1 = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			a1.add(i);
		}
		Collections.shuffle(a1);

		List<Integer> a2 = new ArrayList<>(a1);
		Collections.sort(a2);

		long time = System.currentTimeMillis();
		a1 = quickSort(a1);
		System.out.println(System.currentTimeMillis() - time);

		if (!a1.equals(a2))
			throw new RuntimeException();
	}
}
