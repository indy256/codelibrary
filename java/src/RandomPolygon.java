import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RandomPolygon extends JFrame {

	public static int[][] getRandomPolygon(int n, int maxWidth, int maxHeight) {
		Random rnd = new Random();
		int[] x = new int[n];
		int[] y = new int[n];
		int[] p = new int[n];
		while (true) {
			for (int i = 0; i < n; i++) {
				x[i] = rnd.nextInt(maxWidth);
				y[i] = rnd.nextInt(maxHeight);
				p[i] = i;
			}
			for (boolean improved = true; improved; ) {
				improved = false;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						int[] p1 = p.clone();
						reverse(p1, i, j);
						if (len(x, y, p) > len(x, y, p1)) {
							p = p1;
							improved = true;
						}
					}
				}
			}
			int[] tx = x.clone();
			int[] ty = y.clone();
			for (int i = 0; i < n; i++) {
				x[i] = tx[p[i]];
				y[i] = ty[p[i]];
			}
			boolean ok = true;
			for (int i = 0; i < n; i++) {
				int i1 = (i - 1 + n) % n;
				int i2 = (i + 1) % n;
				ok &= x[i1] * y[i2] - x[i2] * y[i1] != 0 || x[i1] * x[i2] + y[i1] * y[i2] <= 0;
			}
			for (int i2 = 0, i1 = p.length - 1; i2 < p.length; i1 = i2++)
				for (int j2 = 0, j1 = p.length - 1; j2 < p.length; j1 = j2++)
					ok &= i1 == j1 || i1 == j2 || i2 == j1
							|| !isCrossOrTouchIntersect(x[i1], y[i1], x[i2], y[i2], x[j1], y[j1], x[j2], y[j2]);
			if (ok)
				return new int[][]{x, y};
		}
	}

	static int[] reverse(int[] p, int i, int j) {
		int n = p.length;
		int sign = Integer.compare(i, j);
		// reverse order from i to j
		while (sign * (i - j) > 0) {
			int t = p[j];
			p[j] = p[i];
			p[i] = t;
			i = (i + 1) % n;
			j = (j - 1 + n) % n;
		}
		return p;
	}

	static double len(int[] x, int[] y, int[] p) {
		double res = 0;
		for (int i = 0, j = p.length - 1; i < p.length; j = i++) {
			double dx = x[p[i]] - x[p[j]];
			double dy = y[p[i]] - y[p[j]];
			res += Math.sqrt(dx * dx + dy * dy);
		}
		return res;
	}

	static boolean isCrossOrTouchIntersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
		if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2)
				|| Math.max(y1, y2) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2))
			return false;
		long z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
		long z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
		long z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
		long z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
		return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0);
	}

	// visualization code
	final JPanel panel;
	int[] x = new int[0];
	int[] y = new int[0];
	final int MAX_WIDTH = 500;
	final int MAX_HEIGHT = 500;

	public RandomPolygon() {
		panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D) g).setStroke(new BasicStroke(3));
				g.setColor(Color.BLUE);
				int w = getWidth() - 5;
				int h = getHeight() - 30;
				synchronized (panel) {
					int n = x.length;
					for (int i = 0, j = n - 1; i < n; j = i++) {
						g.drawLine((int) (x[i] / (double) MAX_WIDTH * w), (int) ((1 - y[i] / (double) MAX_HEIGHT) * h),
								(int) (x[j] / (double) MAX_WIDTH * w), (int) ((1 - y[j] / (double) MAX_HEIGHT) * h));
					}
					g.setColor(Color.RED);
					for (int i = 0; i < n; i++) {
						g.drawOval((int) (x[i] / (double) MAX_WIDTH * w) - 1, (int) ((1 - y[i] / (double) MAX_HEIGHT) * h) - 1, 3, 3);
					}
				}
			}
		};
		setContentPane(panel);
		new Thread() {
			public void run() {
				Random rnd = new Random();
				while (true) {
					long time = System.currentTimeMillis();
					synchronized (panel) {
						int[][] poly = getRandomPolygon(rnd.nextInt(100) + 3, MAX_WIDTH, MAX_HEIGHT);
						x = poly[0];
						y = poly[1];
					}
					panel.repaint();
					try {
						Thread.sleep(Math.max(0, 200 - (System.currentTimeMillis() - time)));
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

	public static void main(String[] args) {
		JFrame frame = new RandomPolygon();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
