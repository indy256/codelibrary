package optimization;

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
        int[] curState = new int[n];
        for (int i = 0; i < n; i++)
            curState[i] = i;
        double curEnergy = eval(curState);
        bestState = curState.clone();
        double bestEnergy = curEnergy;
        for (double temperature = 0.1, coolingFactor = 0.999999; temperature > 1e-4; temperature *= coolingFactor) {
            int i = rnd.nextInt(n);
            int j = (i + 1 + rnd.nextInt(n - 2)) % n;
            int i1 = (i - 1 + n) % n;
            int j1 = (j + 1) % n;
            double delta = dist(x[curState[i1]], y[curState[i1]], x[curState[j]], y[curState[j]])
                    + dist(x[curState[i]], y[curState[i]], x[curState[j1]], y[curState[j1]])
                    - dist(x[curState[i1]], y[curState[i1]], x[curState[i]], y[curState[i]])
                    - dist(x[curState[j]], y[curState[j]], x[curState[j1]], y[curState[j1]]);
            if (delta < 0 || Math.exp(-delta / temperature) > rnd.nextDouble()) {
                reverse(curState, i, j);
                curEnergy += delta;

                if (bestEnergy > curEnergy) {
                    bestEnergy = curEnergy;
                    System.arraycopy(curState, 0, bestState, 0, n);
                    repaint();
                }
            }
        }
    }

    // http://en.wikipedia.org/wiki/2-opt
    static void reverse(int[] p, int i, int j) {
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
    }

    double eval(int[] state) {
        double res = 0;
        for (int i = 0, j = state.length - 1; i < state.length; j = i++)
            res += dist(x[state[i]], y[state[i]], x[state[j]], y[state[j]]);
        return res;
    }

    static double dist(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
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
                g.drawString(String.format("length: %.3f", eval(bestState)), 5, h + 20);
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
