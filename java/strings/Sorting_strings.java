package codelibrary;

public class Sorting_strings {
	
		static int MAX = 26;

		public static String getSortedString(StringBuilder s, int n)
		{
			int[] lower = new int[MAX];
			int[] upper = new int[MAX];

			for (int i = 0; i < n; i++)
			{
				if (Character.isLowerCase(s.charAt(i)))
					lower[s.charAt(i) - 'a']++;

				else if (Character.isUpperCase(s.charAt(i)))
					upper[s.charAt(i) - 'A']++;
			}

			int i = 0, j = 0;
			while (i < MAX && lower[i] == 0)
				i++;

			while (j < MAX && upper[j] == 0)
				j++;

			for (int k = 0; k < n; k++)
			{
				if (Character.isLowerCase(s.charAt(k)))
				{
					while (lower[i] == 0)
						i++;
					s.setCharAt(k, (char) (i + 'a'));
					lower[i]--;
				}
				else if (Character.isUpperCase(s.charAt(k)))
				{
					while (upper[j] == 0)
						j++;
					s.setCharAt(k, (char) (j + 'A'));

					upper[j]--;
				}
			}

			return s.toString();
		}
		public static void main(String[] args)
		{
			StringBuilder s = new StringBuilder("baskad");
			int n = s.length();
			System.out.println(getSortedString(s, n));
		}
	}
