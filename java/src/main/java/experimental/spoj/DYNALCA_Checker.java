package experimental.spoj;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;


public class DYNALCA_Checker {

	static Random rnd = new Random(1);
 /*
	static void genCase(PrintWriter pw) {
		int n = rnd.nextInt(20) + 3;
		LinkCutTreeLca.Node[] nodes = new LinkCutTreeLca.Node[n];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new LinkCutTreeLca.Node();
		}
		int q = 100000;
		pw.println(n + " " + q);

		int links = 0;

		m1: while (true) {
			int cnt = rnd.nextInt(n / 2) + 1;
			while (links == 0 || links < n - 1) {
				int x;
				int y;
				while (true) {
					x = rnd.nextInt(n);
					y = rnd.nextInt(n);
					if (x == y)
						continue;
					try {
						LinkCutTreeLca.link(nodes[x], nodes[y]);
						break;
					} catch (RuntimeException e) {
					}
				}
				++links;
				pw.println("link " + (x + 1) + " " + (y + 1));
				if (--q == 0)
					break m1;
				if (--cnt == 0)
					break;
			}

			cnt = rnd.nextInt(n / 2) + 1;
			while (links > 0) {
				int x;
				while (true) {
					x = rnd.nextInt(n);
					try {
						LinkCutTreeLca.cut(nodes[x]);
						break;
					} catch (RuntimeException e) {
					}
				}
				--links;
				pw.println("cut " + (x + 1));
				if (--q == 0)
					break m1;
				if (--cnt == 0)
					break;
			}
			if (links > 1) {
				int x;
				int y;
				while (true) {
					x = rnd.nextInt(n);
					y = rnd.nextInt(n);
					if (x == y)
						continue;
					try {
						LinkCutTreeLca.lca(nodes[x], nodes[y]);
						break;
					} catch (RuntimeException e) {

					}
				}
				pw.println("lca " + (x + 1) + " " + (y + 1));
				if (--q == 0)
					break m1;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0;; i++) {
			String inputName = "D:\\dynalca_input.txt";
			String outputName = "D:\\dynalca_output.txt";
			PrintWriter pw = new PrintWriter(inputName);
			genCase(pw);
			pw.close();
			Process p1 = new ProcessBuilder("D:\\projects\\cpp\\spoj-stuff\\DYNALCA\\lc.exe")
					.redirectInput(new File(inputName)).redirectOutput(new File(outputName + "1")).start();
			p1.waitFor();
			String name = "D:\\projects\\cpp\\spoj-stuff\\DYNALCA\\" + (i % 39) + ".exe";
			Process p2 = new ProcessBuilder(name).redirectInput(new File(inputName))
					.redirectOutput(new File(outputName + "2")).start();
			p2.waitFor();

			Process p3 = Runtime.getRuntime().exec("fc " + outputName + "1" + " " + outputName + "2");
			p3.waitFor();
			if (p3.exitValue() != 0) {
				System.err.println("error: " + name);
				return;
			}
		}
	}
	*/
}
