package experimental;

import java.util.Arrays;

public class RadixSort {
	public static void radixSort(int[] a, int from, int to) {
		int n = to - from;
		int[] temp = new int[n];
		int[] cnt = new int[1 << 16];
		for (int i = to - 1; i >= from; --i) {
			++cnt[low(a[i])];
		}
		for (int i = 0; i < cnt.length - 1; ++i) {
			cnt[i + 1] += cnt[i];
		}
		for (int i = to - 1; i >= from; --i) {
			temp[--cnt[low(a[i])]] = a[i];
		}

		Arrays.fill(cnt, 0);
		for (int i = n - 1; i >= 0; --i) {
			++cnt[high(temp[i])];
		}
		cnt[0] += from;
		for (int i = 0; i < cnt.length - 1; ++i) {
			cnt[i + 1] += cnt[i];
		}
		for (int i = n - 1; i >= 0; --i) {
			a[--cnt[high(temp[i])]] = temp[i];
		}
	}

	static int high(int a) {
		return (a ^ Integer.MIN_VALUE) >>> 16;
	}

	static int low(int a) {
		return a & 0xFFFF;
	}
}
