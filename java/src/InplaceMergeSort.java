import java.util.Random;

public class InplaceMergeSort {

	static Random rnd = new Random();


	static void swap(int[] a, int i, int j) {
		int t = a[j];
		a[j] = a[i];
		a[i] = t;
	}

	public static void mergeSort(int[] a, int low, int high) {
		if (low + 1 < high) {
			int mid = (low + high) >>> 1;
			mergeSort(a, low, mid);
			mergeSort(a, mid, high);

			int size = high - low;
			int[] b = new int[size];
			int i = low;
			int j = mid;
			for (int k = 0; k < size; k++) {
				if (j >= high || i < mid && a[i] < a[j]) {
					b[k] = a[i++];
				} else {
					b[k] = a[j++];
				}
			}
			System.arraycopy(b, 0, a, low, size);
		}
	}

	/**
	 * Transforms two consecutive sorted ranges into a single sorted range. The initial ranges are
	 * <code>[first..middle)</code> and <code>[middle..last)</code>, and the resulting range is
	 * <code>[first..last)</code>. Elements in the first input range will precede equal elements in
	 * the second.
	 */
	private static void inPlaceMerge( final int from, int mid, final int to, final IntComparator comp, final Swapper swapper ) {
		if ( from >= mid || mid >= to ) return;
		if ( to - from == 2 ) {
			if ( comp.compare( mid, from ) < 0 ) swapper.swap( from, mid );
			return;
		}

		int firstCut;
		int secondCut;

		if ( mid - from > to - mid ) {
			firstCut = from + ( mid - from ) / 2;
			secondCut = lowerBound( mid, to, firstCut, comp );
		}
		else {
			secondCut = mid + ( to - mid ) / 2;
			firstCut = upperBound( from, mid, secondCut, comp );
		}

		int first2 = firstCut;
		int middle2 = mid;
		int last2 = secondCut;
		if ( middle2 != first2 && middle2 != last2 ) {
			int first1 = first2;
			int last1 = middle2;
			while ( first1 < --last1 )
				swapper.swap( first1++, last1 );
			first1 = middle2;
			last1 = last2;
			while ( first1 < --last1 )
				swapper.swap( first1++, last1 );
			first1 = first2;
			last1 = last2;
			while ( first1 < --last1 )
				swapper.swap( first1++, last1 );
		}

		mid = firstCut + ( secondCut - mid );
		inPlaceMerge( from, firstCut, mid, comp, swapper );
		inPlaceMerge( mid, secondCut, to, comp, swapper );
	}

	/**
	 * Performs a binary search on an already-sorted range: finds the first position where an
	 * element can be inserted without violating the ordering. Sorting is by a user-supplied
	 * comparison function.
	 *
	 * @param from the index of the first element (inclusive) to be included in the binary search.
	 * @param to the index of the last element (exclusive) to be included in the binary search.
	 * @param pos the position of the element to be searched for.
	 * @param comp the comparison function.
	 * @return the largest index i such that, for every j in the range <code>[first..i)</code>,
	 * <code>comp.compare(j, pos)</code> is <code>true</code>.
	 */
	private static int lowerBound( int from, final int to, final int pos, final IntComparator comp ) {
		// if (comp==null) throw new NullPointerException();
		int len = to - from;
		while ( len > 0 ) {
			int half = len / 2;
			int middle = from + half;
			if ( comp.compare( middle, pos ) < 0 ) {
				from = middle + 1;
				len -= half + 1;
			}
			else {
				len = half;
			}
		}
		return from;
	}


	/**
	 * Performs a binary search on an already sorted range: finds the last position where an element
	 * can be inserted without violating the ordering. Sorting is by a user-supplied comparison
	 * function.
	 *
	 * @param from the index of the first element (inclusive) to be included in the binary search.
	 * @param to the index of the last element (exclusive) to be included in the binary search.
	 * @param pos the position of the element to be searched for.
	 * @param comp the comparison function.
	 * @return The largest index i such that, for every j in the range <code>[first..i)</code>,
	 * <code>comp.compare(pos, j)</code> is <code>false</code>.
	 */
	private static int upperBound( int from, final int mid, final int pos, final IntComparator comp ) {
		// if (comp==null) throw new NullPointerException();
		int len = mid - from;
		while ( len > 0 ) {
			int half = len / 2;
			int middle = from + half;
			if ( comp.compare( pos, middle ) < 0 ) {
				len = half;
			}
			else {
				from = middle + 1;
				len -= half + 1;
			}
		}
		return from;
	}

	private static class IntComparator {
		public int compare(int mid, int from) {
			return 0;
		}
	}

	private static class Swapper {
		public void swap(int i, int last1) {

		}
	}
}
