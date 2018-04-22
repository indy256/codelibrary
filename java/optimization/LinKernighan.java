package optimization;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.stream.IntStream;

// https://en.wikipedia.org/wiki/Lin–Kernighan_heuristic
public class LinKernighan extends JFrame {
    Random rnd = new Random(1);
    int n = rnd.nextInt(300) + 250;

    double[] x = new double[n];
    double[] y = new double[n];
    int[] bestState;
    double bestDist = Double.POSITIVE_INFINITY;

    {
        for (int i = 0; i < n; i++) {
            x[i] = rnd.nextDouble();
            y[i] = rnd.nextDouble();
        }
    }

    public void linKernighan() {
        int[] curState = IntStream.range(0, n).toArray();
        double curDist = eval(curState);
        updateBest(curState, curDist);
        for (boolean improved = true; improved; ) {
            improved = false;
            for (int rev = -1; rev <= 1; rev += 2) {
                for (int i = 0; i < n; i++) {
                    int[] p = new int[n];
                    for (int j = 0; j < n; j++)
                        p[j] = curState[(i + rev * j + n) % n];
                    boolean[][] added = new boolean[n][n];
                    double cost = eval(p);
                    double delta = -dist(x[p[n - 1]], y[p[n - 1]], x[p[0]], y[p[0]]);
                    for (int k = 0; k < n; k++) {
                        double best = Double.POSITIVE_INFINITY;
                        int bestPos = -1;
                        for (int j = 1; j < n - 2; j++) {
                            if (added[p[j]][p[j + 1]])
                                continue;
                            double addedEdge = dist(x[p[n - 1]], y[p[n - 1]], x[p[j]], y[p[j]]);
                            if (delta + addedEdge > 0)
                                continue;
                            double removedEdge = dist(x[p[j]], y[p[j]], x[p[j + 1]], y[p[j + 1]]);
                            double cur = addedEdge - removedEdge;
                            if (best > cur) {
                                best = cur;
                                bestPos = j;
                            }
                        }
                        if (bestPos == -1)
                            break;
                        added[p[n - 1]][p[bestPos]] = true;
                        added[p[bestPos]][p[n - 1]] = true;
                        delta += best;
                        reverse(p, bestPos + 1, n - 1);
                        double closingEdge = dist(x[p[n - 1]], y[p[n - 1]], x[p[0]], y[p[0]]);
                        if (curDist > cost + delta + closingEdge) {
                            curDist = cost + delta + closingEdge;
                            curState = p.clone();
                            updateBest(curState, curDist);
                            improved = true;
                            break;
                        }
                    }
                }
            }
        }
        updateBest(curState, curDist);
    }

    void updateBest(int[] curState, double curDist) {
        if (bestDist > curDist) {
            bestDist = curDist;
            bestState = curState.clone();
            repaint();
        }
    }

    // reverse order from i to j
    static void reverse(int[] p, int i, int j) {
        int n = p.length;
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
    public LinKernighan() {
        setContentPane(new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bestState == null) return;
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).setStroke(new BasicStroke(3));
                int w = getWidth() - 5;
                int h = getHeight() - 30;
                for (int i = 0, j = n - 1; i < n; j = i++)
                    g.drawLine((int) (x[bestState[i]] * w), (int) ((1 - y[bestState[i]]) * h),
                            (int) (x[bestState[j]] * w), (int) ((1 - y[bestState[j]]) * h));
                g.drawString(String.format("length: %.3f", eval(bestState)), 5, h + 20);
            }
        });
        setSize(new Dimension(600, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        new Thread(this::linKernighan).start();
    }

    public static void main(String[] args) {
        new LinKernighan();
    }
}
