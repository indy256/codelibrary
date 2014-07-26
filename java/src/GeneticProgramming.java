import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GeneticProgramming extends JFrame {
	Random rnd = new Random(1);
	int n = rnd.nextInt(300) + 250;

	int generation;
	double[] x = new double[n];
	double[] y = new double[n];
	int[] bestState;

	{
		for (int i = 0; i < n; i++) {
			x[i] = rnd.nextDouble();
			y[i] = rnd.nextDouble();
		}
	}

	public void geneticAlgorithm() {
		bestState = new int[n];
		for (int i = 0; i < n; i++)
			bestState[i] = i;
		final int populationLimit = 100;
		final Population population = new Population(populationLimit);
		final int n = x.length;
		for (int i = 0; i < populationLimit; i++) {
			population.chromosomes.add(new Chromosome(getRandomPermutation(n)));
		}

		final double mutationRate = 0.3;
		final int generations = 100_000;

		for (generation = 0; generation < generations; generation++) {
			while (population.chromosomes.size() < population.populationLimit) {
				int i1 = rnd.nextInt(population.chromosomes.size());
				int i2 = (i1 + 1 + rnd.nextInt(population.chromosomes.size() - 1)) % population.chromosomes.size();

				Chromosome parent1 = population.chromosomes.get(i1);
				Chromosome parent2 = population.chromosomes.get(i2);

				int[] child = crossOver(parent1.p, parent2.p);

				if (rnd.nextDouble() < mutationRate) {
					mutate(child);
				}

				population.chromosomes.add(new Chromosome(child));
			}
			population.nextGeneration();
			bestState = population.chromosomes.get(0).p;
			repaint();
		}
	}

	// http://en.wikipedia.org/wiki/Edge_recombination_operator
	int[] crossOver(int[] p1, int[] p2) {
		int n = p1.length;
		int[] p = new int[4 * n];
		Arrays.fill(p, -1);
		for (int i = 0; i < n; i++) {
			int pos = p1[i] * 4;
			while (p[pos] != -1) ++pos;
			p[pos] = p1[(i + 1) % n];
			p[pos] = p1[(i - 1 + n) % n];
			pos = p2[i] * 4;
			while (p[pos] != -1) ++pos;
			p[pos] = p2[(i + 1) % n];
			p[pos] = p2[(i - 1 + n) % n];
		}
		boolean[] used = new boolean[n];
		int[] child = new int[n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			if (used[i]) continue;
			for (int cur = i; cur != -1; ) {
				child[k++] = cur;
				used[cur] = true;
				int best = Integer.MAX_VALUE;
				int next = -1;
				for (int d1 = 0; d1 < 4; d1++) {
					int v = p[cur * 4 + d1];
					if (v == -1 || used[v]) continue;
					int cnt = 0;
					for (int d2 = 0; d2 < 4; d2++) {
						if (p[v * 4 + d2] == cur) {
							p[v * 4 + d2] = -1;
						} else if (p[v * 4 + d2] != -1) {
							++cnt;
						}
					}
					if (best > cnt) {
						best = cnt;
						next = v;
					}
				}
				cur = next;
			}
		}
		return child;
	}

	// http://en.wikipedia.org/wiki/2-opt
	void mutate(int[] p) {
		int n = p.length;
		int i = rnd.nextInt(n);
		int j = (i + 1 + rnd.nextInt(n - 1)) % n;
		// reverse order from i to j
		while (true) {
			int t = p[i];
			p[i] = p[j];
			p[j] = t;
			i = (i + 1) % n;
			if (i == j) break;
			j = (j - 1 + n) % n;
			if (i == j) break;
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

	int[] getRandomPermutation(int n) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			int j = rnd.nextInt(i + 1);
			res[i] = res[j];
			res[j] = i;
		}
		return res;
	}

	class Chromosome implements Comparable<Chromosome> {
		final int[] p;
		private double cost = Double.NaN;

		public Chromosome(int[] p) {
			this.p = p;
		}

		public double getCost() {
			if (Double.isNaN(cost))
				cost = eval(p);
			return cost;
		}

		@Override
		public int compareTo(Chromosome o) {
			return Double.compare(getCost(), o.getCost());
		}
	}

	static class Population {
		List<Chromosome> chromosomes = new ArrayList<>();
		final int populationLimit;

		public Population(int populationLimit) {
			this.populationLimit = populationLimit;
		}

		public void nextGeneration() {
			Collections.sort(chromosomes);
			chromosomes = new ArrayList<>(chromosomes.subList(0, (chromosomes.size() + 1) / 2));
		}
	}

	// visualization code
	public GeneticProgramming() {
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
				g.drawString(String.format("generation: %d", generation), 150, h + 20);
			}
		});
		setSize(new Dimension(600, 600));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		new Thread(this::geneticAlgorithm).start();
	}

	public static void main(String[] args) {
		new GeneticProgramming();
	}
}
