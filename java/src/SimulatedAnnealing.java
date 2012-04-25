import java.awt.*;
import java.io.PrintWriter;
import java.util.*;
import javax.swing.*;

public class SimulatedAnnealing extends JFrame {
	Random rnd = new Random(5);
	int n = rnd.nextInt(300) + 150;

	double[] x = new double[n];
	double[] y = new double[n];
	int[] p = new int[n];
	{
		for (int i = 0; i < n; i++) {
			x[i] = rnd.nextDouble();
			y[i] = rnd.nextDouble();
			p[i] = i;
		}
		try {
			PrintWriter pw = new PrintWriter("D:/Program Files/Concorde/tsp1.txt");
			pw.println(n);
			for (int i = 0; i < n; i++) {
				pw.println((int)(x[i]*1000) + " " + (int)(y[i]*1000));
			}
			pw.close();
		} catch (Exception e) {
		}

	}

	int[] neighbour(int[] state, double curTemp) {
		int n = state.length;
		int i = 0;
		int j = 0;
		do {
			i = rnd.nextInt(n);
			j = rnd.nextInt(n);
		} while (Math.abs(i - j) > n * curTemp);

		int[] newState = state.clone();
		int sign = i - j;
		// reverse order from i to j
		while (sign * (i - j) > 0) {
			int t = newState[i];
			newState[i] = newState[j];
			newState[j] = t;
			i = (i + 1) % n;
			j = (j - 1 + n) % n;
		}
		return newState;
	}

	double Prob(double curEnergy, double newEnergy, double curTemp) {
		double diff = newEnergy - curEnergy;
		if (diff < 0) {
			return 1;
		} else {
			int magicValue = 100000;
			double probability = Math.exp(-diff * magicValue / n / curTemp);
			return probability;
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
		double temperatureStep = 1e-7;
		for (double curTemp = 1; curTemp > 0; curTemp -= temperatureStep) {
			int[] newState = neighbour(curState, curTemp);
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

	double eval(int[] state) {
		double res = 0;
		for (int i = 0, j = state.length - 1; i < state.length; j = i++) {
			double dx = x[state[i]] - x[state[j]];
			double dy = y[state[i]] - y[state[j]];
			res += Math.sqrt(dx * dx + dy * dy);
		}
		return res;
	}

	// visualization code
	JPanel panel;

	public SimulatedAnnealing() {
		panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D) g).setStroke(new BasicStroke(3));
				g.setColor(Color.BLUE);
				int maxx = getWidth() - 5;
				int maxy = getHeight() - 30;
				for (int i = 0, j = n - 1; i < n; j = i++) {
					g.drawLine((int) (x[p[i]] * maxx), (int) ((1 - y[p[i]]) * maxy), (int) (x[p[j]] * maxx),
							(int) ((1 - y[p[j]]) * maxy));
				}
				g.setColor(Color.RED);
				for (int i = 0; i < n; i++) {
					g.drawOval((int) (x[i] * maxx) - 1, (int) ((1 - y[i]) * maxy) - 1, 3, 3);
				}
				g.setColor(Color.BLACK);
				g.drawString(String.format("length = %6.3f", eval(p)), 2, maxy + 10);
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
		JFrame frame = new SimulatedAnnealing();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}