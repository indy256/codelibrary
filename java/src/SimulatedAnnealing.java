import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SimulatedAnnealing extends JFrame {
	Random rnd = new Random(1);
	int n = rnd.nextInt(300) + 250;

	int[] bestState;
	double[] x = new double[n];
	double[] y = new double[n];

	{
		for (int i = 0; i < n; i++) {
			x[i] = rnd.nextDouble();
			y[i] = rnd.nextDouble();
		}
	}

	public void anneal() {
		double coolingFactor = 0.999998;
		int[] curState = new int[n];
		for (int i = 0; i < n; i++)
			curState[i] = i;
		double curEnergy = eval(curState);
		bestState = curState;
		double bestEnergy = curEnergy;
		for (double temperature = 1; temperature > 1e-4; temperature *= coolingFactor) {
			int[] newState = neighbour(curState);
			double newEnergy = eval(newState);

			double delta = newEnergy - curEnergy;
			if (delta < 0 || Math.exp(-delta / temperature) > rnd.nextDouble()) {
				curState = newState;
				curEnergy = newEnergy;
			}

			if (bestEnergy > newEnergy) {
				bestState = newState;
				bestEnergy = newEnergy;
				repaint();
			}
		}
	}

	int[] neighbour(int[] state) {
		int n = state.length;
		int i = rnd.nextInt(n);
		int j = (i + rnd.nextInt(n - 1) + 1) % n;
		int[] newState = state.clone();
		int sign = Integer.compare(i, j);
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
	public SimulatedAnnealing() {
		setContentPane(new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D) g).setStroke(new BasicStroke(3));
				g.setColor(Color.BLUE);
				int w = getWidth() - 5;
				int h = getHeight() - 30;
				for (int i = 0, j = n - 1; i < n; j = i++)
					g.drawLine((int) (x[bestState[i]] * w), (int) ((1 - y[bestState[i]]) * h),
							(int) (x[bestState[j]] * w), (int) ((1 - y[bestState[j]]) * h));
				g.setColor(Color.RED);
				for (int i = 0; i < n; i++)
					g.drawOval((int) (x[i] * w) - 1, (int) ((1 - y[i]) * h) - 1, 3, 3);
				g.setColor(Color.BLACK);
				g.drawString(String.format("length = %.3f", eval(bestState)), 5, h + 20);
			}
		});
		setSize(new Dimension(600, 600));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		new Thread(this::anneal).start();
	}

	public static void main(String[] args) {
		new SimulatedAnnealing();
	}
}
