package experimental;

import java.util.Arrays;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;

public class BinarySearchFull {

	public static int binarySearchFirstTrueInteger(IntPredicate p, int from, int to) {
		int lo = from - 1;
		int hi = to + 1;
		while (hi - lo > 1) {
			int mid = (lo + hi) / 2;
			if (!p.test(mid)) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	public static double binarySearchFirstTrueDouble(DoublePredicate p, double lo, double hi) {
		for (int step = 0; step < 1000; step++) {
			double mid = (lo + hi) / 2;
			if (!p.test(mid)) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	/**
	 * Returns min(p|a[p]==true) 000[1]11
	 * <p/>
	 * invariant: a[lo]==false, a[hi]==true, hence can't set lo=mid+1 or hi=mid-1
	 * <p/>
	 * boundary case: lo1=p,hi1=p+1,lo2=(lo1+hi1)/2=p,hi2=p+1
	 */
	public static int binarySearchFirst(boolean[] a) {
		int lo = -1;
		int hi = a.length;
		while (hi - lo > 1) {
			int mid = (lo + hi) / 2;
			if (!a[mid]) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	// Returns max(p|a[p]==false) 00[0]111
	public static int binarySearchLast(boolean[] a) {
		int lo = -1;
		int hi = a.length;
		while (hi - lo > 1) {
			int mid = (lo + hi) / 2;
			if (!a[mid]) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return lo;
	}

	/**
	 * Returns min(p|a[p]==true) 000[1]11
	 * <p/>
	 * invariant: lo<=min(p|a[p]=true), a[hi]==true, hence can set lo = mid+1
	 * <p/>
	 * boundary case: lo1=p,hi1=p,lo2=p,hi2=(lo1+hi1)/2=p
	 */
	public static int binarySearchFirst2(boolean[] a) {
		int lo = 0;
		int hi = a.length;
		while (lo < hi) {
			int mid = (lo + hi) / 2;
			if (!a[mid]) {
				lo = mid + 1;
			} else {
				hi = mid;
			}
		}
		return lo;
	}

	/**
	 * Returns max(p|a[p]==false) 00[0]111
	 * <p/>
	 * invariant: a[lo]=false, hi>=max(p|a[p]=false), hence can set hi=mid-1
	 * <p/>
	 * boundary case: lo1=p,hi1=p,lo2=p,hi2=(lo1+hi1+1)/2=p
	 */
	public static int binarySearchLast2(boolean[] a) {
		int lo = -1;
		int hi = a.length - 1;
		while (lo < hi) {
			int mid = (lo + hi + 1) / 2;
			if (!a[mid]) {
				lo = mid;
			} else {
				hi = mid - 1;
			}
		}
		return lo;
	}

	/**
	 * Returns (p|a[p]==key), if it is contained in the array; otherwise, -(insertion point+1).
	 * <p/>
	 * invariant: lo<=min(p|a[p]>=key), hi>=max(p|a[p]<=key), hence can set lo=mid+1 and hi=mid-1
	 * <p/>
	 * boundary case: lo1=p+1,hi1=p,lo2=(lo1+hi1)/2+1=p+1,hi2=p
	 */
	public static int binarySearch(int[] a, int key) {
		int lo = 0;
		int hi = a.length - 1;
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			int midVal = a[mid];
			if (midVal < key) {
				lo = mid + 1;
			} else if (midVal > key) {
				hi = mid - 1;
			} else {
				return mid;
			}
		}
		return -(lo + 1);
	}

	/**
	 * Returns min(p|a[p]>=key)
	 * <p/>
	 * invariant: a[lo]<key, a[hi]>=key, hence can't set lo=mid+1 or hi=mid-1
	 * <p/>
	 * boundary case: lo1=p,hi1=p+1,lo2=(lo1+hi1)/2=p,hi2=p+1
	 */
	public static int stl_lower_bound(int[] a, int key) {
		// return binarySearchFirstTrueInteger(i -> a[i] >= key, 0, a.length - 1);
		int lo = -1;
		int hi = a.length;
		while (hi - lo > 1) {
			int mid = (lo + hi) / 2;
			int midVal = a[mid];
			if (midVal < key) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	/**
	 * Returns min(p|a[p]>key)
	 * <p/>
	 * invariant: a[lo]<=key, a[hi]>key, hence can't set lo=mid+1 or hi=mid-1
	 * <p/>
	 * boundary case: lo1=p,hi1=p+1,lo2=(lo1+hi1)/2=p,hi2=p+1
	 */
	public static int stl_upper_bound(int[] a, int key) {
		// return binarySearchFirstTrueInteger(i -> a[i] > key, 0, a.length - 1);
		int lo = -1;
		int hi = a.length;
		while (hi - lo > 1) {
			int mid = (lo + hi) / 2;
			int midVal = a[mid];
			if (midVal <= key) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	/**
	 * Returns min(p|a[p]>=key)
	 */
	public static int stl_lower_bound2(int[] a, int key) {
		int len = a.length;
		int from = 0;
		while (len > 0) {
			int half = len / 2;
			int mid = from + half;
			if (a[mid] < key) {
				from = mid + 1;
				len -= half + 1;
			} else {
				len = half;
			}
		}
		return from;
	}

	/**
	 * Returns min(p|a[p]>key)
	 */
	public static int stl_upper_bound2(int[] a, int key) {
		int len = a.length;
		int from = 0;
		while (len > 0) {
			int half = len / 2;
			int mid = from + half;
			if (a[mid] <= key) {
				from = mid + 1;
				len -= half + 1;
			} else {
				len = half;
			}
		}
		return from;
	}

	// Usage example
	public static void main(String[] args) {
		boolean[] b = {false, false, true};
		System.out.println(2 == binarySearchFirst(b));
		System.out.println(1 == binarySearchLast(b));

		int[] a = {1, 3, 7, 10, 15};
		System.out.println(4 == stl_upper_bound(a, 11));
		System.out.println(4 == stl_upper_bound2(a, 11));

		System.out.println(binarySearchFirstTrueInteger(i -> b[i], 0, b.length - 1));
		System.out.println(binarySearchFirstTrueInteger(i -> a[i] >= 6, 0, a.length - 1));
		System.out.println(Arrays.binarySearch(new int[5], 0));
	}
}
