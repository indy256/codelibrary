package experimental;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TabuSearch extends JFrame {
	Random rnd = new Random(1);
	int n = rnd.nextInt(300)*0 + 100;

	int[] bestState;
	double[] x = new double[n];
	double[] y = new double[n];

	{
		for (int i = 0; i < n; i++) {
			x[i] = rnd.nextDouble();
			y[i] = rnd.nextDouble();
		}
	}

	public void tabuSearch() {
		Set<Solution> tabu = new HashSet<>();
		Deque<Solution> deque = new ArrayDeque<>();
		bestState = new int[n];
		for (int i = 0; i < n; i++)
			bestState[i] = i;
		Solution curSolution = new Solution(bestState);
		tabu.add(curSolution);
		deque.addLast(curSolution);
		bestState = curSolution.state;
		double bestEnergy = eval(curSolution.state);
		for (int iteration = 0; iteration < 100_000; iteration++) {
			double curEnergy = Double.POSITIVE_INFINITY;
			int[] curState = curSolution.state;
			curSolution = null;
			m1:
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (i == j) continue;
					Solution newSolution = new Solution(reverse(curState.clone(), i, j));
					if (tabu.contains(newSolution)) continue;
					double newEnergy = eval(newSolution.state);
					if (curEnergy > newEnergy) {
						curEnergy = newEnergy;
						curSolution = newSolution;
						if (bestEnergy > curEnergy)
							break m1;
					}
				}
			}
			System.out.println(iteration + " " + tabu.size() + " " + curEnergy);

			if (curSolution == null)
				break;

			if (tabu.size() == 1000)
				tabu.remove(deque.removeFirst());
			tabu.add(curSolution);
			deque.addLast(curSolution);

			if (bestEnergy > curEnergy) {
				bestState = curSolution.state;
				bestEnergy = curEnergy;
				repaint();
			}
		}
	}

	static class Solution {
		final int[] state;
		final int hash;

		Solution(int[] state) {
			this.state = state;
			this.hash = Arrays.hashCode(state);
		}

		@Override
		public boolean equals(Object o) {
			return Arrays.equals(state, ((Solution) o).state);
		}

		@Override
		public int hashCode() {
			return hash;
		}
	}

	// http://en.wikipedia.org/wiki/2-opt
	static int[] reverse(int[] p, int i, int j) {
		int n = p.length;
		// reverse order from i to j
		while (i != j) {
			int t = p[j];
			p[j] = p[i];
			p[i] = t;
			i = (i + 1) % n;
			if (i == j) break;
			j = (j - 1 + n) % n;
		}
		return p;
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
	public TabuSearch() {
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
				g.drawString(String.format("length: %.3f", eval(bestState)), 5, h + 20);
			}
		});
		setSize(new Dimension(600, 600));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		new Thread(this::tabuSearch).start();
	}

	public static void main(String[] args) {
		new TabuSearch();
	}
}
