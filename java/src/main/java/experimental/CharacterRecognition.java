package experimental;

import java.util.*;

public class CharacterRecognition {

	static int d2(int a, int b) {
		return (a - b) * (a - b);
	}

	static int[][] slowRec(int[][] picture, boolean[][] symbol, int bgColor, int textColor) {
		int pR = picture.length;
		int pC = picture[0].length;
		int sR = symbol.length;
		int sC = symbol[0].length;

		int R = pR - sR + 1;
		int C = pC - sC + 1;
		int[][] res = new int[R][C];

		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				for (int lr = 0; lr < sR; lr++) {
					for (int lc = 0; lc < sC; lc++) {
						int color = symbol[lr][lc] ? textColor : bgColor;
						res[r][c] += d2(color, picture[r + lr][c + lc]);
					}
				}
			}
		}

		return res;
	}

	static class Point {
		int r, c;

		public Point(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}

	public static int[][] fastRec(int[][] picture, boolean[][] symbol, int bgColor, int textColor) {
		int sR = symbol.length;
		int sC = symbol[0].length;

		List<Point> bt = new ArrayList<>();
		List<Point> tb = new ArrayList<>();

		for (int r = 0; r < sR; r++) {
			if (symbol[r][0]) {
				bt.add(new Point(r, 0));
			}
			if (symbol[r][sC - 1]) {
				tb.add(new Point(r, sC));
			}
			for (int c = 1; c < sC; c++) {
				if (!symbol[r][c - 1] && symbol[r][c]) {
					bt.add(new Point(r, c));
				} else if (symbol[r][c - 1] && !symbol[r][c]) {
					tb.add(new Point(r, c));
				}
			}
		}

		int pR = picture.length;
		int pC = picture[0].length;
		int R = pR - sR + 1;
		int C = pC - sC + 1;
		int[][] res = new int[R][C];

		for (int r = 0; r < R; r++) {
			int[] empty = new int[pC + 1];
			for (int c = 0; c < pC; c++) {
				empty[c + 1] = empty[c];
				for (int dr = 0; dr < sR; dr++) {
					empty[c + 1] += d2(bgColor, picture[r + dr][c]);
				}
			}

			for (int lr = 0; lr < sR; lr++) {
				for (int lc = 0; lc < sC; lc++) {
					int color = symbol[lr][lc] ? textColor : bgColor;
					res[r][0] += d2(color, picture[r + lr][lc]);
				}
			}

			for (int c = 1; c < C; c++) {
				int s = 0;
				for (int i = 0; i < bt.size(); i++) {
					s += picture[r + tb.get(i).r][c - 1 + tb.get(i).c] - picture[r + bt.get(i).r][c - 1 + bt.get(i).c];
				}
				res[r][c] = res[r][c - 1];
				res[r][c] += s * 2 * (bgColor - textColor);
				res[r][c] += (empty[c + sC] - empty[c + sC - 1]) - (empty[c] - empty[c - 1]);
			}
		}

		return res;
	}

	public static void main(String[] args) {
		final int bgColor = 0;
		final int textColor = 255;
		Random rnd = new Random(1);

		for (int step = 0; step < 1000; step++) {
			int sR = rnd.nextInt(10) + 1;
			int sC = rnd.nextInt(10) + 1;
			boolean[][] s = new boolean[sR][sC];
			for (int r = 0; r < sR; r++) {
				for (int c = 0; c < sC; c++) {
					s[r][c] = rnd.nextBoolean();
				}
			}

			int pR = rnd.nextInt(100) + sR;
			int pC = rnd.nextInt(100) + sC;
			int[][] p = new int[pR][pC];
			for (int r = 0; r < sR; r++) {
				for (int c = 0; c < sC; c++) {
					p[r][c] = rnd.nextInt(256);
				}
			}

			int[][] res1 = slowRec(p, s, bgColor, textColor);
			int[][] res2 = fastRec(p, s, bgColor, textColor);

			for (int r = 0; r < res1.length; r++) {
				if (!Arrays.equals(res1[r], res2[r])) {
					for (int[] row : res1) {
						System.err.println(Arrays.toString(row));
					}
					System.err.println();
					for (int[] row : res2) {
						System.err.println(Arrays.toString(row));
					}
				}
			}
		}
	}
}
