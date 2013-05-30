package experimental.spoj;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

public class DYNACON1_Checker {

	static Random rnd = new Random(1);

	static void genCase(PrintWriter pw) {
/*		int n = rnd.nextInt(20) + 3;
		LinkCutTreeConnectivity.Node[] nodes = new LinkCutTreeConnectivity.Node[n];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new LinkCutTreeConnectivity.Node();
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
						LinkCutTreeConnectivity.link(nodes[x], nodes[y]);
						break;
					} catch (RuntimeException e) {
					}
				}
				++links;
				pw.println("add " + (x + 1) + " " + (y + 1));
				if (--q == 0)
					break m1;
				if (--cnt == 0)
					break;
			}

			cnt = rnd.nextInt(n / 2) + 1;
			while (links > 0) {
				int x;
				int y;
				while (true) {
					x = rnd.nextInt(n);
					y = rnd.nextInt(n);
					if (x == y)
						continue;
					try {
						LinkCutTreeConnectivity.cut(nodes[x], nodes[y]);
						break;
					} catch (RuntimeException e) {
					}
				}
				--links;
				pw.println("rem " + (x + 1) + " " + (y + 1));
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
					break;
				}
				pw.println("conn " + (x + 1) + " " + (y + 1));
				if (--q == 0)
					break m1;
			}
		}*/
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0;; i++) {
			String inputName = "D:\\dynacon1_input.txt";
			String outputName = "D:\\dynacon1_output.txt";
			PrintWriter pw = new PrintWriter(inputName);
			genCase(pw);
			pw.close();
			Process p1 = new ProcessBuilder("D:\\projects\\cpp\\spoj-stuff\\DYNACON1\\lc.exe")
					.redirectInput(new File(inputName)).redirectOutput(new File(outputName + "1")).start();
			p1.waitFor();
//			String name = "D:\\projects\\cpp\\spoj-stuff\\DYNACON1\\" + (i % 20) + ".exe";
			 String name = "D:\\projects\\cpp\\spoj-stuff\\DYNACON1\\a.bat";
			Process p2 = new ProcessBuilder(name, inputName, outputName + "2")/*.redirectInput(new File(inputName))
					.redirectOutput(new File(outputName + "2"))*/.start();
			p2.waitFor();

			Process p3 = Runtime.getRuntime().exec("fc " + outputName + "1" + " " + outputName + "2");
			p3.waitFor();
			System.out.println(name);
			if (p3.exitValue() != 0) {
				System.err.println("error: " + name);
				return;
			}
		}
	}
}
