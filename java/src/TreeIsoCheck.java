import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeIsoCheck {
	static Random rnd = new Random(1);

	public static void main(String[] args) throws Exception {
		List<Integer> list = new ArrayList<Integer>();
		list.add(10);
		while (true) {
			PrintStream ps = new PrintStream("D:\\treeiso_input.txt");
			genCase(1, list, ps);
			ps.close();
			Process p1 = Runtime.getRuntime().exec(
					new String[] { "D:\\projects\\cpp\\spoj\\TREEISO\\treeiso1.bat", "D:\\treeiso_input.txt",
							"D:\\treeiso_output1.txt" });
			p1.waitFor();
			Process p2 = Runtime.getRuntime()
					.exec(new String[] { "D:\\projects\\cpp\\spoj\\a.bat", "D:\\treeiso_input.txt",
							"D:\\treeiso_output2.txt" });
			p2.waitFor();

			Process p3 = Runtime.getRuntime().exec("fc D:\\treeiso_output1.txt D:\\treeiso_output2.txt");
			p3.waitFor();
			if (p3.exitValue() != 0) {
				System.err.println("error");
				return;
			}
		}
	}

	static void genCase(int id, List<Integer> list, PrintStream ps) throws FileNotFoundException {
		ps.println(list.size());
		for (int n : list) {
			n = rnd.nextInt(30) + 2;
//			 n=5;
			ps.println(n);
			List[][] t = { RandomGraph.getRandomConnectedGraph(n, n-1, rnd), RandomGraph.getRandomConnectedGraph(n, n-1, rnd) };
			for (int i = 0; i < t.length; i++) {
				for (int u = 0; u < n; u++) {
					for (int v : (List<Integer>) t[i][u]) {
						if (u < v) {
							ps.println((u + 1) + " " + (v + 1));
						}
					}
				}
			}
		}
	}
}
