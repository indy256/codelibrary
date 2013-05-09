package experimental;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class Code2Html {

	static String[] KEYWORDS = { "int", "char", "long", "void", "bool", "for", "while", "do", "static", "const", "if",
			"return", "#include", "using", "namespace", "true", "false", "typedef", "operator", "struct", "class",
			"friend", "this", "public", "private", "protected", "break", "continue" };
	static String[] MACROS = { "rep", "fr" };
	static String separators = " ;,.<>()=/+-*\t";

	public static void main(String[] args) throws Exception {
		Set<String> keywords = new HashSet<>();
		for (String s : KEYWORDS) {
			keywords.add(s);
		}
		Set<String> macros = new HashSet<>();
		for (String s : MACROS) {
			macros.add(s);
		}

		BufferedReader br = new BufferedReader(new FileReader(
				new File("D://projects//olymp//wikialgo//cpp//input.txt")));
		PrintWriter pw = new PrintWriter(new File("D://projects//olymp//wikialgo//cpp//output.txt"));

		pw.println("<code>");

		while (true) {
			String s = br.readLine();
			if (s == null)
				break;

			boolean semicolon = s.endsWith(";");
			boolean comment = s.trim().startsWith("//");
			if (comment) {
				print(pw, s, "3F5FBF");
				pw.println("<br/>");
				continue;
			}
			char[] a = s.toCharArray();
			int prev = -1;
			for (int i = 0; i < a.length; i++) {
				boolean last = i == a.length - 1;
				if (separators.indexOf(a[i]) != -1 || last) {
					String w = new String(a, prev + 1, i - prev - 1);
					if (!w.isEmpty()) {
						boolean isNumber = true;
						for (char x : w.toCharArray()) {
							isNumber &= Character.isDigit(x);
						}

						if (a[i] == '(' && !macros.contains(w) && !keywords.contains(w) && !semicolon) {
							print(pw, w, "000000");
						} else if (isNumber) {
							print(pw, w, "990000");
						} else if (keywords.contains(w)) {
							print(pw, w, "7f0055");
						} else {
							pw.print(w);
						}
					}
					if (a[i] == 9)
						pw.print("&nbsp;&nbsp;");
					else if (a[i] == ' ')
						pw.print("&nbsp;");
					else if (a[i] == '<')
						pw.print("&lt;");
					else if (a[i] == '>')
						pw.print("&gt;");
					else
						pw.print(a[i]);
					prev = i;
				}
			}
			pw.println("<br/>");
		}

		pw.println("</code>");
		pw.close();
	}

	static void print(PrintWriter pw, String w, String color) {
		pw.print("<font color=\"#" + color + "\"><b>");
		pw.print(w);
		pw.print("</b></font>");
	}
}
