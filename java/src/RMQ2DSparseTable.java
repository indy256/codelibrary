import java.util.Arrays;
import java.util.Scanner;

public class RMQ2DSparseTable
{
	public static void main(String[] args)
	{
		Solver solver = new Solver();

		solver.solve();
	}

	static class Solver
	{
		int[] log;
		int[][] arr;
		int[][][][] rmq;

		void solve()
		{
			arr = new int[][]{{1, 0, 3, 2, 6}, {10, 100, 12, 3, 0}, {12, 16, 15, 2, 4}, {8, 10, 50, 20, 1}, {1, 2, 3, 4,
					5}};

			buildSparseMatrix(arr.length, arr[0].length);
			System.out.println("The matrix looks like : ");

			for (int i = 0; i < arr.length; i++)
				System.out.println("i : " + i + " => " + Arrays.toString(arr[i]));

			Scanner in = new Scanner(System.in);

			while (true)
			{
				System.out.println("Enter the top-left & bottom-right points of the submatrix : ");

				int x1, y1, x2, y2;

				x1 = in.nextInt();
				y1 = in.nextInt();
				x2 = in.nextInt();
				y2 = in.nextInt();

				System.out.println(query(x1, y1, x2, y2));
				System.out.println("Do you want to ask any more queries? (yes/no) : ");

				if (!in.next().equals("yes"))
					break;
			}
		}

		void buildSparseMatrix(int n, int m)
		{
			log = new int[n + 1];

			for (int i = 2; i <= n; i++)
				log[i] = log[i >> 1] + 1;

			rmq = new int[n + 1][m + 1][log[n] + 1][log[m] + 1];

			for (int i = 0; (1 << i) <= n; i++)
			{
				for (int j = 0; (1 << j) <= m; j++)
				{
					for (int x = 0; x + (1 << i) - 1 < n; x++)
					{
						for (int y = 0; y + (1 << j) - 1 < m; y++)
						{
							if (i == 0 && j == 0)
								rmq[x][y][i][j] = arr[x][y];
							else if (i == 0)
								rmq[x][y][i][j] = max(rmq[x][y][i][j - 1], rmq[x][y + (1 << (j - 1))][i][j - 1]);
							else if (j == 0)
								rmq[x][y][i][j] = max(rmq[x][y][i - 1][j], rmq[x + (1 << (i - 1))][y][i - 1][j]);
							else
								rmq[x][y][i][j] = max(rmq[x][y][i - 1][j - 1], rmq[x + (1 << (i - 1))][y][i - 1][j - 1],
										rmq[x][y + (1 << (j - 1))][i - 1][j - 1], rmq[x + (1 << (i - 1))][y + (1 << (j - 1))
												][i - 1][j - 1]);
						}
					}
				}
			}
		}

		int query(int x, int y, int x1, int y1)
		{
			int k, l, ans;

			k = log[x1 - x + 1];
			l = log[y1 - y + 1];
			ans = max(rmq[x][y][k][l], rmq[x1 - (1 << k) + 1][y][k][l], rmq[x][y1 - (1 << l) + 1][k][l], rmq[x1 - (1 << k)
					+ 1][y1 - (1 << l) + 1][k][l]);

			return ans;
		}

		int max(int... integers)
		{
			int maxValue = -1;

			for (int i : integers)
				maxValue = Math.max(maxValue, i);

			return maxValue;
		}

	}

}
