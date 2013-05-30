package test;

import java.math.BigInteger;

public class BigIntReadTest {

	public static void main(String[] args) {
		StringBuilder sa = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10000; i++) {
			sa.append(i % 10);
		}
		for (int i = 0; i < 20000; i++) {
			sb.append(i % 10);
		}
		BigInteger a;
		long time = System.currentTimeMillis();
		a = new BigInteger(sa.toString());
		System.err.println(System.currentTimeMillis() - time);

		BigInteger b;
		time = System.currentTimeMillis();
		b = new BigInteger(sb.toString());
		System.err.println(System.currentTimeMillis() - time);

		time = System.currentTimeMillis();
		a = a.divide(b);
		System.err.println(System.currentTimeMillis() - time);

		// System.out.println(a);

	}

}
