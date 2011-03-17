import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;

public class BigIntGen {
	static Random rnd = new Random(1);

	static BigInteger getNum(boolean zero) {
		int n = rnd.nextInt(100) + 1;
		while (true) {
			byte[] a = new byte[n];
			rnd.nextBytes(a);
			BigInteger x = new BigInteger(a);
			if (zero || x.signum() != 0)
				return x;
		}
	}

	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter("D:/projects/cpp/wikialgo/bigint.txt");
		for (int i = 0; i < 100000; i++) {
			BigInteger a = getNum(true);
			BigInteger b = getNum(false);
			pw.println(a + " " + b);
		}
		pw.close();
	}
}
