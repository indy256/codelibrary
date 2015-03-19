import java.util.*;

public class MaxRectangle {

	public static long maxRectangle(int[] heights) {
		long res = 0;
		Stack<Integer> spos = new Stack<>();
		Stack<Integer> sh = new Stack<>();
		sh.push(0);
		for (int i = 0; i <= heights.length; i++) {
			int h = i < heights.length ? heights[i] : 0;
			int pos = i;
			while (sh.peek() > h) {
				pos = spos.pop();
				res = Math.max(res, (long) sh.pop() * (i - pos));
			}
			spos.push(pos);
			sh.push(h);
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		System.out.println(maxRectangle(new int[]{1, 2, 3}));
	}
}
