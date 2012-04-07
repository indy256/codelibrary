import java.awt.*;
import java.util.*;
import javax.swing.*;

public class SimulatedAnnealing1 extends JFrame {
	static Random rnd = new Random();
	JPanel panel;
	int[] x;
	int[] y;
	int[] p;

	static int[] getRandomCombination(int n, int m) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			res[i] = i;
		}
		for (int i = 0; i < m; i++) {
			int j = n - 1 - rnd.nextInt(n - i);
			int t = res[i];
			res[i] = res[j];
			res[j] = t;
		}
		return Arrays.copyOf(res, m);
	}

	static int[] getRandomPermutation(int n) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			res[i] = i;
		}
		for (int i = res.length - 1; i > 0; i--) {
			int j = rnd.nextInt(i + 1);
			int t = res[i];
			res[i] = res[j];
			res[j] = t;
		}
		return res;
	}

	int[] neighbour(int[] state, int changes) {
		int[] p = getRandomCombination(state.length, changes);
		int[] r = getRandomPermutation(changes);
		int[] res = state.clone();
		for (int i = 0; i < changes; i++) {
			res[p[i]] = state[p[r[i]]];
		}
		return res;
	}

	double eval(int[] state) {
		double res = 0;
		for (int i = 0, j = state.length - 1; i < state.length; j = i++) {
			int dx = x[state[i]] - x[state[j]];
			int dy = y[state[i]] - y[state[j]];
			res += Math.sqrt(dx * dx + dy * dy);
		}
		return res;
	}

	static double Prob(double curEnergy, double newEnergy, double curTemp) {
		double diff = newEnergy - curEnergy;
		if (diff < 0) {
			return 1;
		} else {
			return Math.exp(-diff / curTemp);
		}
	}

	public void anneal() {
		int[] curState = new int[p.length];
		for (int i = 0; i < p.length; i++) {
			curState[i] = i;
		}
		double curEnergy = eval(curState);
		int[] bestState = curState;
		double bestEnergy = curEnergy;
		double maxTemp = 10000;
		for (double curTemp = maxTemp; curTemp > 0; curTemp -= 0.01) {
			int changes = Math.max(2, (int) (curTemp / maxTemp * p.length));
			int[] newState = neighbour(curState, changes);
			double newEnergy = eval(newState);

			if (Prob(curEnergy, newEnergy, curTemp) > rnd.nextDouble()) {
				curState = newState;
				curEnergy = newEnergy;
			}

			if (bestEnergy > newEnergy) {
				bestEnergy = newEnergy;
				bestState = newState;

				p = bestState;
				panel.repaint();
			}
		}
	}

	public SimulatedAnnealing1() {
		final int n = rnd.nextInt(20) + 50;
		x = new int[n];
		y = new int[n];
		p = new int[n];
		final int max = 400;

		for (int i = 0; i < n; i++) {
			x[i] = rnd.nextInt(max);
			y[i] = rnd.nextInt(max);
			p[i] = i;
		}

		panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = ((Graphics2D) g);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setStroke(new BasicStroke(3));
				g.setColor(Color.BLUE);
				for (int i = 0, j = n - 1; i < n; j = i++) {
					g.drawLine(x[p[i]], max - y[p[i]], x[p[j]], max - y[p[j]]);
				}
				g.setColor(Color.RED);
				for (int i = 0; i < n; i++) {
					g.drawOval(x[i], max - y[i], 3, 3);
				}
				g.setColor(Color.BLACK);
				String s = String.format("%6.1f", eval(p));
				g.drawString(s, 2, 410);
			}
		};
		setContentPane(panel);
		new Thread() {
			public void run() {
				anneal();
			}
		}.start();
	}

	public static void main(String[] args) {
		JFrame frame = new SimulatedAnnealing1();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
