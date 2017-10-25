import java.util.Scanner;

/**
 * Program to find any of the Longest Common Subsequences between 2 strings using Dynamic Programming.
 * <br />Time Complexity : O(n * m), where n and m are the lengths of the 2 strings.
 */
public class LCS
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		Solver solver = new Solver(in);

		solver.solve();
		in.close();
	}

	static class Solver
	{
		String a, b;
		Scanner in;

		void solve()
		{
			System.out.print("Enter the 1st string : ");
			a = in.next();
			System.out.print("\nEnter the 2nd string : ");
			b = in.next();

			int aLength, bLength;

			aLength = a.length();
			bLength = b.length();

			int[][] dp = new int[aLength + 1][bLength + 1];

			for (int i = 1; i <= aLength; i++)
			{
				for (int j = 1; j <= bLength; j++)
				{
					if (a.charAt(i - 1) == b.charAt(j - 1))
						dp[i][j] = 1 + dp[i - 1][j - 1];
					else
						dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}

			StringBuilder lcs = new StringBuilder();

			for (int i = aLength; i > 0;)
			{
				for (int j = bLength; j > 0;)
				{
					if (i == 0)
						break;

					if (dp[i][j] != dp[i - 1][j] && dp[i][j] != dp[i][j - 1])
					{
						i--;
						j--;
						lcs.append(a.charAt(i));
					}
					else if (dp[i][j] == dp[i - 1][j])
						i--;
					else
						j--;
				}
			}

			lcs = lcs.reverse();
			System.out.println("The LCS is : " + lcs.toString());
		}

		public Solver(Scanner in)
		{
			this.in = in;
		}

	}

}

/*

asbecdf
adbcf

aaazzz
zzzaaa

zzzaaa
aaazzz

*/
