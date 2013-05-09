package experimental;

import java.util.Random;

public class JumpPrediction {

	public static void main(String[] args) {
		Random rnd = new Random(1);

		int n = 10000000;
		int[] a = new int[n];
		int[] b = new int[n];
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = rnd.nextInt(1);
			b[i] = rnd.nextInt(1);
			c[i] = rnd.nextInt(1);
		}

		long time = System.currentTimeMillis();
		int x = 0;
		for (int i = 0; i < n; i++) {
			// if(a[i]==0 && b[i]==0 && c[i]==0){
			// x++;
			// }
			if ((a[i] | b[i] | c[i]) == 0) {
				x++;
			}

		}
		System.out.println(System.currentTimeMillis() - time);
		System.out.println(x);
	}

}
