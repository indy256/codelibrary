import java.awt.*;
import java.util.*;
import javax.swing.*;

public class ForceBasedGraphDrawer extends JFrame {
  static Random rnd = new Random();
  JPanel panel;
  double[] x, y;

  public void drawGraph(int[][] d) {
    int n = d.length;
    x = new double[n];
    y = new double[n];
    Set<Long> set = new HashSet<>();
    for (int i = 0; i < n; i++) {
      while (true) {
        int px = rnd.nextInt(100);
        int py = rnd.nextInt(100);
        x[i] = px;
        y[i] = py;
        long key = px * 1000000000L + py;
        if (!set.contains(key)) {
          set.add(key);
          break;
        }
      }
    }
    double k1 = 1;
    double k2 = 100000;
    for (int step = 0; step < 500000; step++) {
      double[] fx = new double[n];
      double[] fy = new double[n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          if (i == j) {
            continue;
          }
          double dx = x[j] - x[i];
          double dy = y[j] - y[i];
          double dist = Math.sqrt(dx * dx + dy * dy);
          dx /= dist;
          dy /= dist;

          double force = k2 / (dist * dist);

          if (d[i][j] > 0) {
            force += -k1 * (dist - 1);
          }
          fx[j] += force * dx;
          fy[j] += force * dy;
        }
      }

      for (int i = 0; i < n; i++) {
        x[i] += fx[i] / 1000;
        y[i] += fy[i] / 1000;
      }

      if (step % 100 == 0) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
        }
      }
      panel.repaint();
    }
  }

  public ForceBasedGraphDrawer() {
    int n = rnd.nextInt(40) + 50;
    final int[][] d = new int[n][n];
    for (int i = 0; i + 1 < n; i++) {
      d[i][i + 1] = d[i + 1][i] = 1;
    }
    for (int i = 0; i < 2 * n; i++) {
      int u = rnd.nextInt(n - 1);
      int v = u + 1 + rnd.nextInt(n - 1 - u);
      d[u][v] = d[v][u] = 1;
    }
    // "hard" example
    // int t = 10;
    // n = 2 * t + 1;
    // final int[][] d = new int[n][n];
    // for (int i = 0; i < t; i++) {
    // d[i][(i + 1) % t] = d[(i + 1) % t][i] = 1;
    // d[i][n - 1] = d[n - 1][i] = 1;
    // d[i + t][n - 1] = d[n - 1][i + t] = 1;
    // d[i][i + t] = d[i + t][i] = 1;
    // d[i][(i + 1) % t + t] = d[(i + 1) % t + t][i] = 1;
    // }

    panel = new JPanel() {
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (x == null) {
          return;
        }
        Double minx = Double.POSITIVE_INFINITY;
        Double miny = Double.POSITIVE_INFINITY;
        int n = d.length;
        for (int i = 0; i < n; i++) {
          minx = Math.min(minx, x[i]);
          miny = Math.min(miny, y[i]);
        }
        int[] x1 = new int[n];
        int[] y1 = new int[n];
        for (int i = 0; i < n; i++) {
          x1[i] = (int) (x[i] - minx) + 10;
          y1[i] = (int) (y[i] - miny) + 10;
        }
        Graphics2D g2 = ((Graphics2D) g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        g.setColor(Color.BLUE);
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
            if (d[i][j] > 0) {
              g.drawLine(x1[i], y1[i], x1[j], y1[j]);
            }
          }
        }
        g.setColor(Color.RED);
        for (int i = 0; i < n; i++) {
          g.drawOval(x1[i] - 1, y1[i] - 1, 3, 3);
        }
      }
    };
    setContentPane(panel);
    new Thread() {
      public void run() {
        drawGraph(d);
      }
    }.start();
  }

  public static void main(String[] args) {
    JFrame frame = new ForceBasedGraphDrawer();
    frame.setSize(new Dimension(800, 600));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

}