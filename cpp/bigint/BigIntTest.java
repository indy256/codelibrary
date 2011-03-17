import java.io.File;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BigIntTest {
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
		Scanner sc = new Scanner(new File("D:/projects/cpp/wikialgo/bigint.txt"));
		List<BigInteger> a = new ArrayList<BigInteger>();
		List<BigInteger> b = new ArrayList<BigInteger>();
		while (sc.hasNext()) {
			a.add(sc.nextBigInteger());
			b.add(sc.nextBigInteger());
		}

		long time = System.currentTimeMillis();
		PrintWriter pw = new PrintWriter("D:/projects/cpp/wikialgo/bigint1.txt");
		List<BigInteger> c = new ArrayList<BigInteger>();
		for (int i = 0; i < a.size(); i++) {
			c.add(a.get(i).divide(b.get(i)));
		}
		System.err.println(System.currentTimeMillis() - time);
		for (BigInteger x : c) {			
			pw.println(x);
		}
		pw.close();
	}
}
