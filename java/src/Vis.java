import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Vis extends JFrame {
	static final int WIDTH = 500;
	static final int HEIGHT = 500;
	BufferedImage img;
	boolean stop;
	public Graphics2D g;

	public Vis() {
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		clear();
		JComponent c = new JComponent() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		};
		c.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		add(c);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				stop = false;
			}
		});
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void vis() {
		repaint();
		stop = true;
		try {
			while (stop)
				Thread.sleep(10);
		} catch (Exception e) {
		}
	}

	void clear() {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
	}

	// Usage example
	public static void main(String[] args) {
		Vis vis = new Vis();
		vis.g.drawLine(10, 10, 100, 100);
		vis.vis();
	}
}
