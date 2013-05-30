package experimental.spoj;
import java.io.PrintStream;
import java.util.Random;

public class LinkCutTreeTest2 {

	public static void main(String[] args) throws Exception {
		PrintStream ps = new PrintStream("dynalca2.in");
		int n = 50000;
		ps.println(n);
		for (int i = 0; i + 1 < n; i++) {
			int a = i + 1;
			int b = i + 2;
			ps.println("link " + a + " " + b);
		}
		Random rnd = new Random(1);
		for (int i = 0; i < 100000 - n; i++) {
			int a = rnd.nextInt(n) + 1;
			int b = n - i % 10;
			ps.println("lca " + a + " " + b);
		}
	}
}
