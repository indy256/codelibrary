import java.awt.*;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class GeneticProgramming extends JFrame {
	static Random rnd = new Random();
	JPanel panel;
	int[] x;
	int[] y;
	int[] p;
	int generationsEvolved = 0;

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

	double eval(int[] p) {
		double res = 0;
		for (int i = 0, j = p.length - 1; i < p.length; j = i++) {
			int dx = x[p[i]] - x[p[j]];
			int dy = y[p[i]] - y[p[j]];
			res += Math.sqrt(dx * dx + dy * dy);
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
			if (Double.isNaN(cost)) {
				cost = eval(p);
			}
			return cost;
		}

		@Override
		public int compareTo(Chromosome other) {
			return Double.compare(getCost(), other.getCost());
		}
	}

	static class Population {
		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		final int populationLimit;

		public Population(int populationLimit) {
			this.populationLimit = populationLimit;
		}

		public void nextGeneration() {
			Collections.sort(chromosomes);
			List<Chromosome> nextChromosomes = new ArrayList<Chromosome>();
			for (int i = 0; i < (chromosomes.size() + 1) / 2; i++) {
				nextChromosomes.add(chromosomes.get(i));
			}
			chromosomes = nextChromosomes;
		}
	}

	int[][] crossOver(int[] p1, int[] p2) {
		int n = p1.length;
		int i1 = rnd.nextInt(n);
		int i2;
		do {
			i2 = rnd.nextInt(n);
		} while (i2 == i1);

		int[] n1 = p1.clone();
		int[] n2 = p2.clone();

		boolean[] used1 = new boolean[n];
		boolean[] used2 = new boolean[n];

		for (int i = i1;; i = (i + 1) % n) {
			n1[i] = p2[i];
			used1[n1[i]] = true;
			n2[i] = p1[i];
			used2[n2[i]] = true;
			if (i == i2) {
				break;
			}
		}

		for (int i = (i2 + 1) % n; i != i1; i = (i + 1) % n) {
			if (used1[n1[i]]) {
				n1[i] = -1;
			} else {
				used1[n1[i]] = true;
			}
			if (used2[n2[i]]) {
				n2[i] = -1;
			} else {
				used2[n2[i]] = true;
			}
		}

		List<Integer> free1 = new ArrayList<Integer>();
		List<Integer> free2 = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			if (!used1[i]) {
				free1.add(i);
			}
			if (!used2[i]) {
				free2.add(i);
			}
		}

		int pos1 = 0;
		int pos2 = 0;
		for (int i = 0; i < n; i++) {
			if (n1[i] == -1) {
				n1[i] = free1.get(pos1++);
			}
			if (n2[i] == -1) {
				n2[i] = free2.get(pos2++);
			}
		}
		return new int[][] { n1, n2 };
	}

	void mutate(int[] p) {
		int n = p.length;
		int i1 = rnd.nextInt(n);
		int i2;
		do {
			i2 = rnd.nextInt(n);
		} while (i2 == i1);
		if (rnd.nextBoolean()) {
			// swap
			int t = p[i1];
			p[i1] = p[i2];
			p[i2] = t;
		} else {
			// rotate
			while (true) {
				int t = p[i1];
				p[i1] = p[i2];
				p[i2] = t;
				i1 = (i1 + 1) % n;
				if (i1 == i2) {
					break;
				}
				i2 = (i2 + n - 1) % n;
				if (i1 == i2) {
					break;
				}
			}
		}
	}

	public void geneticAlgorithm() {
		final int populationLimit = 1000;
		final Population population = new Population(populationLimit);
		final int n = x.length;
		for (int i = 0; i < populationLimit; i++) {
			population.chromosomes.add(new Chromosome(getRandomPermutation(n)));
		}

		final double mutationRate = 0.3;
		final int generations = 10000;

		for (int g = 0; g < generations; g++) {
			while (population.chromosomes.size() < population.populationLimit) {
				int i1 = rnd.nextInt(population.chromosomes.size());
				int i2;
				do {
					i2 = rnd.nextInt(population.chromosomes.size());
				} while (i2 == i1);

				Chromosome ch1 = population.chromosomes.get(i1);
				Chromosome ch2 = population.chromosomes.get(i2);

				int[][] pair = crossOver(ch1.p, ch2.p);
				ch1 = new Chromosome(pair[0]);
				ch2 = new Chromosome(pair[1]);

				if (rnd.nextDouble() < mutationRate) {
					mutate(ch1.p);
					mutate(ch2.p);
				}

				population.chromosomes.add(ch1);
				if (population.chromosomes.size() < population.populationLimit) {
					population.chromosomes.add(ch2);
				}
			}
			population.nextGeneration();
			p = population.chromosomes.get(0).p;
			generationsEvolved = g + 1;
			panel.repaint();
		}
	}

	public GeneticProgramming() {
		final int n = rnd.nextInt(20) + 200;
		// final int n = 5;
		x = new int[n];
		y = new int[n];
		p = new int[n];
		final int max = 400;

		for (int i = 0; i < n; i++) {
			x[i] = rnd.nextInt(max);
			y[i] = rnd.nextInt(max);
			p[i] = i;
		}

//		try {
//			PrintWriter pw = new PrintWriter("tsp.txt");
//			pw.println(n);
//			for (int i = 0; i < n; i++) {
//				pw.println(x[i] + " " + y[i]);
//			}
//			pw.close();
//		} catch (Exception e) {
//		}

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
				g.drawString(String.format("%6.1f", eval(p)), 2, 410);
				g.drawString(String.format("Generation %d", generationsEvolved), 2, 425);
			}
		};
		setContentPane(panel);
		new Thread() {
			public void run() {
				geneticAlgorithm();
			}
		}.start();
	}

	public static void main(String[] args) {
		JFrame frame = new GeneticProgramming();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
