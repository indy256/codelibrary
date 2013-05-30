import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class Delaunay extends JFrame {

	public static Triangle[] triangulateDelaunay(long[] x, long[] y) {
		int n = x.length;
		long[] z = new long[n];
		for (int i = 0; i < n; i++)
			z[i] = x[i] * x[i] + y[i] * y[i];
		List<Triangle> res = new ArrayList<>();
		for (int i = 0; i < n - 2; i++) {
			for (int j = i + 1; j < n; j++) {
				m1: for (int k = i + 1; k < n; k++) {
					if (j == k)
						continue;
					long nx = (y[j] - y[i]) * (z[k] - z[i]) - (y[k] - y[i]) * (z[j] - z[i]);
					long ny = (x[k] - x[i]) * (z[j] - z[i]) - (x[j] - x[i]) * (z[k] - z[i]);
					long nz = (x[j] - x[i]) * (y[k] - y[i]) - (x[k] - x[i]) * (y[j] - y[i]);
					if (nz >= 0)
						continue;
					for (int m = 0; m < n; m++) {
						long dot = (x[m] - x[i]) * nx + (y[m] - y[i]) * ny + (z[m] - z[i]) * nz;
						if (dot > 0)
							continue m1;
					}
					// to deal with 4 points on a circle
					int[] s1 = { i, j, k, i };
					for (Triangle t : res) {
						int[] s2 = { t.a, t.b, t.c, t.a };
						for (int u = 0; u < 3; u++)
							for (int v = 0; v < 3; v++)
								if (isCrossIntersect(x[s1[u]], y[s1[u]], x[s1[u + 1]], y[s1[u + 1]], x[s2[v]],
										y[s2[v]], x[s2[v + 1]], y[s2[v + 1]]))
									continue m1;
					}
					res.add(new Triangle(i, j, k));
				}
			}
		}
		return res.toArray(new Triangle[0]);
	}

	static class Triangle {
		int a, b, c;

		public Triangle(int a, int b, int c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}

	static boolean isCrossIntersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
		long z1 = (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1);
		long z2 = (x4 - x1) * (y2 - y1) - (y4 - y1) * (x2 - x1);
		if (z1 < 0 && z2 < 0 || z1 > 0 && z2 > 0 || z1 == 0 || z2 == 0)
			return false;
		long z3 = (x1 - x3) * (y4 - y3) - (y1 - y3) * (x4 - x3);
		long z4 = (x2 - x3) * (y4 - y3) - (y2 - y3) * (x4 - x3);
		if (z3 < 0 && z4 < 0 || z3 > 0 && z4 > 0 || z3 == 0 || z4 == 0)
			return false;
		return true;
	}

	public Delaunay() {
		// 4 points on a circle
		// final long[] x = { 0, 0, 100, 100 };
		// final long[] y = { 0, 100, 100, 0 };
		int n = 100;
		final long[] x = new long[n];
		final long[] y = new long[n];
		Random rnd = new Random();
		for (int i = 0; i < n; i++) {
			x[i] = rnd.nextInt(500) + 1;
			y[i] = rnd.nextInt(500) + 1;
		}
		final Triangle[] ts = triangulateDelaunay(x, y);
		JPanel panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = ((Graphics2D) g);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setStroke(new BasicStroke(3));
				g.setColor(Color.BLUE);
				for (Triangle t : ts) {
					g.drawLine((int) x[t.a], (int) y[t.a], (int) x[t.b], (int) y[t.b]);
					g.drawLine((int) x[t.a], (int) y[t.a], (int) x[t.c], (int) y[t.c]);
					g.drawLine((int) x[t.c], (int) y[t.c], (int) x[t.b], (int) y[t.b]);
				}
				g.setColor(Color.RED);
				for (int i = 0; i < x.length; i++) {
					g.drawOval((int) x[i] - 1, (int) y[i] - 1, 3, 3);
				}
			}
		};
		setContentPane(panel);
	}

	public static void main(String[] args) {
		JFrame frame = new Delaunay();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
