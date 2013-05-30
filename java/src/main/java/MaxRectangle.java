import java.util.*;

public class MaxRectangle {

	public static long maxRectangle(int[] heights) {
		long res = 0;
		Stack<Integer> sx = new Stack<Integer>();
		Stack<Integer> sh = new Stack<Integer>();
		sh.push(0);
		for (int i = 0; i < heights.length; i++) {
			int x = i;
			while (sh.peek() > heights[i]) {
				x = sx.pop();
				res = Math.max(res, (long) sh.pop() * (i - x));
			}
			sx.push(x);
			sh.push(heights[i]);
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {

	}
}
