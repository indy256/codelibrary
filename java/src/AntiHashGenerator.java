public class AntiHashGenerator {
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		int n = 100_000;
		for (int i = 0; i < n; i++) {
			sb.append((char) ('A' + Integer.bitCount(i) % 2));
		}
		System.out.println(sb);
	}
}
