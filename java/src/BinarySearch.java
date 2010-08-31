public class BinarySearch {

	/**
	 * Returns min(p|a[p]==true)
	 * 
	 * invariant: a[lo]==false, a[hi]==true, hence can't set lo = mid+1 or hi = mid-1
	 * 
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

	/**
	 * Returns max(p|a[p]==false)
	 * 
	 * invariant: a[lo]==false, a[hi]==true, hence can't set lo=mid+1 or hi=mid-1
	 * 
	 * boundary case: lo1=p,hi1=p+1,lo2=(lo1+hi1)/2=p,hi2=p+1
	 */
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
	 * Returns min(p|a[p]==true)
	 * 
	 * invariant: lo<=min(p|a[p]=true), a[hi]==true, hence can set lo = mid+1
	 * 
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
	 * Returns max(p|a[p]==false)
	 * 
	 * invariant: a[lo]=false, hi>=max(p|a[p]=false), hence can set hi=mid-1
	 * 
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
	 * 
	 * invariant: lo<=min(p|a[p]>=key), hi>=max(p|a[p]<=key), hence can set lo=mid+1 and hi=mid-1
	 * 
	 * boundary case: lo1=p+1,hi1=p,lo2=(lo1+hi1)/2+1=p+1,hi2=p
	 */
	public static int binarySearch(long[] a, long key) {
		int lo = 0;
		int hi = a.length - 1;
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			long midVal = a[mid];
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
	 * 
	 * invariant: a[lo]<key, a[hi]>=key, hence can't set lo=mid+1 or hi=mid-1
	 * 
	 * boundary case: lo1=p,hi1=p+1,lo2=(lo1+hi1)/2=p,hi2=p+1
	 */
	public static int stl_lower_bound(long[] a, long key) {
		int lo = -1;
		int hi = a.length;
		while (hi - lo > 1) {
			int mid = (lo + hi) / 2;
			long midVal = a[mid];
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
	 * 
	 * invariant: a[lo]<=key, a[hi]>key, hence can't set lo=mid+1 or hi=mid-1
	 * 
	 * boundary case: lo1=p,hi1=p+1,lo2=(lo1+hi1)/2=p,hi2=p+1
	 */
	public static int stl_upper_bound(long[] a, long key) {
		int lo = -1;
		int hi = a.length;
		while (hi - lo > 1) {
			int mid = (lo + hi) / 2;
			long midVal = a[mid];
			if (midVal <= key) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	// Usage example
	public static void main(String[] args) {
		boolean[] b = { false, false, true };
		System.out.println(2 == binarySearchFirst(b));
		System.out.println(1 == binarySearchLast(b));

		long[] a = { 1, 3, 7, 10, 15 };
		System.out.println(4 == stl_upper_bound(a, 11));
	}
}
