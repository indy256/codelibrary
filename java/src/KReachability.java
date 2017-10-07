import java.util.Scanner;

/*
 * An integer is said to be k-reachable from another integer if the latter can
 * be converted to the former by replacing at most k digits. Note that leading
 * 0's are not considered the part of the number and hence are not allowed to
 * replaced with non-zero integers.
 * 
 * For example,
 * 
 * 1123 is 15-reachable from 2223 but not 1-reachable.
 * 
 * 6 is 2-reachable from 10, but 10 is not 2-reachable from 6
 * 
 * We are provided the initial integer N and the value of k. We have to
 * answer Q queries. Each query is represented by a pair (L, R). The answer
 * for a query is the number of integers in the range L L to R R (inclusive)
 * which are k-reachable from N N.
 */

public class KReachability {

	static final int N = 10000007;
	static final int siz = 8;
	
	static int[] arr = new int[N];
	static int k;
	
	static int reachable(int a, int b) {
		int i, cnt = 0;
		for(i = 0; i < siz; i++){
			if(b == 0 && a != 0)
				return 0;
			if(a % 10 != b % 10)
				cnt++;
			a /= 10;
			b /= 10;
		}
		return cnt <= k ? 1 : 0;
	}
	public static void main(String[] args){
		int i,q,n,l,r;
		
		Scanner in = new Scanner(System.in);

		n = in.nextInt();
		k = in.nextInt();
		q = in.nextInt();
		
		for(i = 1; i < N; i++)
			arr[i] = reachable(i, n);
		for(i = 1; i < N; i++)
			arr[i] += arr[i - 1];
		while(q-- > 0){
			l = in.nextInt();
			r = in.nextInt();
			
			System.out.printf("%d\n", arr[r] - arr[l - 1]);
		}
		in.close();
	}
}