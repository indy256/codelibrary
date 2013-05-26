package test;/*
 * Problem, java?
 */

import java.io.PrintStream;
import java.util.Arrays;

public class AntiJava7Sort {
  public static void main(String[] args) throws Exception {
    long starttime = System.currentTimeMillis();
    int[] scrambled = Trollface.killQuicksort(200000);
    System.out
        .println("Scrambling ended in " + (System.currentTimeMillis() - starttime) + "ms");
    PrintStream out = new PrintStream(scrambled.length + ".txt");
    for (int i = 0; i < scrambled.length; i++) {
      out.println(scrambled[i]);
    }
    out.close();
    starttime = System.currentTimeMillis();
    Arrays.sort(scrambled);
    System.out.println("Sorting ended in " + (System.currentTimeMillis() - starttime) + "ms");
  }
}

class Trollface {
  static int max;
  static int[] positions;
  static final int MAX_RUN_LENGTH = 33, MAX_RUN_COUNT = 67;
  static int set;

  public static int[] killQuicksort(int size) throws Exception {
    set = 0;
    max = size;
    positions = new int[size];
    int[] sorted = new int[size];
    for (int i = 0; i < size; i++) {
      positions[i] = i;
      sorted[i] = Integer.MIN_VALUE;
    }
    for (int i = 0; i < MAX_RUN_COUNT; i++) {
      sorted[2 * i + 1] = max--;
      sorted[2 * i] = max--;
    }
    sort(sorted, 0, sorted.length - 1, true);
    int[] result = new int[size];
    for (int i = 0; i < size; i++) {
      result[positions[i]] = sorted[i];
      if (result[positions[i]] == Integer.MIN_VALUE) {
        result[positions[i]] = max--;
      }
    }
    return result;
  }

  private static final int INSERTION_SORT_THRESHOLD = 47;

  /**
   * Sorts the specified range of the array by Dual-Pivot Quicksort.
   * 
   * @param a
   *            the array to be sorted
   * @param left
   *            the index of the first element, inclusive, to be sorted
   * @param right
   *            the index of the last element, inclusive, to be sorted
   * @param leftmost
   *            indicates if this part is the leftmost in the range
   */
  private static void sort(int[] a, int left, int right, boolean leftmost) {
    int length = right - left + 1;

    // Use insertion sort on tiny arrays
    if (length < INSERTION_SORT_THRESHOLD) {
      for (int i = left; i <= right; i++)
        for (int j = i; j > left && a[j - 1] > a[j]; j--)
          swap(a, j, j - 1);
      return;
    }

    // Inexpensive approximation of length / 7
    int seventh = (length >> 3) + (length >> 6) + 1;

    /*
     * Sort five evenly spaced elements around (and including) the center
     * element in the range. These elements will be used for pivot selection
     * as described below. The choice for spacing these elements was
     * empirically determined to work well on a wide variety of inputs.
     */

    int e3 = (left + right) >>> 1; // The midpoint
    int e2 = e3 - seventh;
    int e1 = e2 - seventh;
    int e4 = e3 + seventh;
    int e5 = e4 + seventh;

    for (int i : new int[] { e1, e2, e3, e4, e5 }) {
      if (a[i] == Integer.MIN_VALUE) {
        set(a, i, max--);
      }
    }
    // Sort these elements using insertion sort
    if (a[e2] < a[e1]) {
      swap(a, e1, e2);
    }

    if (a[e3] < a[e2]) {
      swap(a, e3, e2);
      if (a[e2] < a[e1]) {
        swap(a, e2, e1);
      }
    }
    if (a[e4] < a[e3]) {
      swap(a, e4, e3);
      if (a[e3] < a[e2]) {
        swap(a, e3, e2);
        if (a[e2] < a[e1]) {
          swap(a, e2, e1);
        }
      }
    }
    if (a[e5] < a[e4]) {
      swap(a, e5, e4);
      if (a[e4] < a[e3]) {
        swap(a, e4, e3);
        if (a[e3] < a[e2]) {
          swap(a, e3, e2);
          if (a[e2] < a[e1]) {
            swap(a, e2, e1);
          }
        }
      }
    }

    // Pointers
    int less = left; // The index of the first element of center part
    int great = right; // The index before the first element of right part

    if (!(a[e1] != a[e2] && a[e2] != a[e3] && a[e3] != a[e4] && a[e4] != a[e5])) {
      throw new AssertionError();
    }
    /*
     * Use the second and fourth of the five sorted elements as pivots.
     * These values are inexpensive approximations of the first and second
     * terciles of the array. Note that pivot1 <= pivot2.
     */
    int pivot1 = a[e2];
    int pivot2 = a[e4];

    /*
     * The first and the last elements to be sorted are moved to the
     * locations formerly occupied by the pivots. When partitioning is
     * complete, the pivots are swapped back into their final positions, and
     * excluded from subsequent sorting.
     */
    swap(a, e2, left);
    swap(a, e4, right);

    /*
     * Skip elements, which are less or greater than pivot values.
     */
    while (a[++less] < pivot1)
      ;
    while (a[--great] > pivot2)
      ;

    /*
     * Partitioning:
     * 
     * left part center part right part
     * +--------------------------------------------------------------+ | <
     * pivot1 | pivot1 <= && <= pivot2 | ? | > pivot2 |
     * +--------------------------------------------------------------+ ^ ^
     * ^ | | | less k great
     * 
     * Invariants:
     * 
     * all in (left, less) < pivot1 pivot1 <= all in [less, k) <= pivot2 all
     * in (great, right) > pivot2
     * 
     * Pointer k is the first index of ?-part.
     */
    outer: for (int k = less - 1; ++k <= great;) {
      int ak = a[k];
      if (ak < pivot1) { // Move a[k] to left part
        swap(a, k, less);
        ++less;
      } else if (ak > pivot2) { // Move a[k] to right part
        while (a[great] > pivot2) {
          if (great-- == k) {
            break outer;
          }
        }
        if (a[great] < pivot1) { // a[great] <= pivot2
          swap(a, k, less);
          swap(a, less, great);
          ++less;
        } else { // pivot1 <= a[great] <= pivot2
          swap(a, k, great);
        }
        --great;
      }
    }

    // Swap pivots into their final positions
    swap(a, left, less - 1);
    swap(a, right, great + 1);

    // Sort left and right parts recursively, excluding known pivots
    sort(a, left, less - 2, leftmost);
    sort(a, great + 2, right, false);

    /*
     * If center part is too large (comprises > 4/7 of the array), swap
     * internal pivot values to ends.
     */
    if (less < e1 && e5 < great) {
      /*
       * Skip elements, which are equal to pivot values.
       */
      while (a[less] == pivot1) {
        ++less;
      }

      while (a[great] == pivot2) {
        --great;
      }

      /*
       * Partitioning:
       * 
       * left part center part right part
       * +----------------------------------------------------------+ | ==
       * pivot1 | pivot1 < && < pivot2 | ? | == pivot2 |
       * +----------------------------------------------------------+ ^ ^
       * ^ | | | less k great
       * 
       * Invariants:
       * 
       * all in (*, less) == pivot1 pivot1 < all in [less, k) < pivot2 all
       * in (great, *) == pivot2
       * 
       * Pointer k is the first index of ?-part.
       */
      outer: for (int k = less - 1; ++k <= great;) {
        int ak = a[k];
        if (ak == pivot1) { // Move a[k] to left part
          swap(a, k, less);
          ++less;
        } else if (ak == pivot2) { // Move a[k] to right part
          while (a[great] == pivot2) {
            if (great-- == k) {
              break outer;
            }
          }
          if (a[great] == pivot1) { // a[great] < pivot2
            swap(a, k, less);
            swap(a, less, great);
            ++less;
          } else { // pivot1 < a[great] < pivot2
            swap(a, k, great);
          }
          --great;
        }
      }
    }

    // Sort center part recursively
    sort(a, less, great, false);
  }

  /**
   * Swaps x[a] with x[b].
   */
  private static void swap(int x[], int a, int b) {
    int t = x[a];
    x[a] = x[b];
    x[b] = t;
    int tpos = positions[a];
    positions[a] = positions[b];
    positions[b] = tpos;
  }

  private static void set(int x[], int pos, int value) {
    if (++set % 10000 == 0) {
      System.out.println(set + "/" + x.length);
    }
    if (x[pos] == Integer.MIN_VALUE)
      x[pos] = value;
    else
      throw new AssertionError();
  }
}