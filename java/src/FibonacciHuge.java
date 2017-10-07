import java.math.BigInteger;

/* Optimized to work in O(log n) time complexity. */

public class FibonacciHuge {
	static final BigInteger TWO = new BigInteger("2");
	
	static int fib(BigInteger n)
	{
	    long F[][] = new long[][]{{1,1},{1,0}};
	    if (n.compareTo(BigInteger.ZERO) == 0)
	        return 0;
	    power(F,n.subtract(BigInteger.ONE));
	    
	    return (int)F[0][0];
	}
	      
	/* Helper function that multiplies 2 matrices F and M of size 2*2, and
	   puts the multiplication result back to F[][] */
	static void multiply(long F[][], long M[][])
	{
	    long x =  (F[0][0]*M[0][0] + F[0][1]*M[1][0]) % 1000000007;
	    long y =  (F[0][0]*M[0][1] + F[0][1]*M[1][1]) % 1000000007;
	    long z =  (F[1][0]*M[0][0] + F[1][1]*M[1][0]) % 1000000007;
	    long w =  (F[1][0]*M[0][1] + F[1][1]*M[1][1]) % 1000000007;
	      
	    F[0][0] = x;
	    F[0][1] = y;
	    F[1][0] = z;
	    F[1][1] = w;
	}
	 
	/* Helper function that calculates F[][] raised to the power n and puts the
	   result in F[][] */
	static void power(long F[][], BigInteger n)
	{
	    if (n.compareTo(BigInteger.ZERO) == 0 || n.compareTo(BigInteger.ONE) == 0) {
	        return;
	    }
	    long M[][] = new long[][]{{1,1},{1,0}};
	     
	    power(F, n.divide(TWO));
	    multiply(F, F);
	    
	    if (n.mod(TWO).compareTo(BigInteger.ZERO) != 0) {
	        multiply(F,M);
	    }
	}
	
	public static void main(String[] args) {
		String n = "face";

	    BigInteger N = new BigInteger(n, 16);
	    fib(N);
	}
}