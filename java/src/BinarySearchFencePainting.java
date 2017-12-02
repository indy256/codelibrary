import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

// ITMOx: I2CPx -> Fence Painting
public class BinarySearchFencePainting {
    public static void main(String[] args) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader("input.txt"));
             PrintWriter out = new PrintWriter("output.txt")) {
            StringTokenizer first = new StringTokenizer(in.readLine());
            StringTokenizer second = new StringTokenizer(in.readLine());
            int n = Integer.parseInt(first.nextToken());
            int k = Integer.parseInt(first.nextToken());
            int[] a = new int[n];
            for (int i = 0; i < n; ++i) {
                a[i] = Integer.parseInt(second.nextToken());
            }
            Arrays.sort(a);
            int xGood = 0, xBad = a[0] + 1;
            while (xBad - xGood > 1) {
                int xMid = (xBad + xGood) >>> 1;
                int lastFree = 0;
                for (int t : a) {
                    lastFree = Math.max(t, lastFree + xMid);
                    if (lastFree > k) {
                        break;
                    }
                }
                if (lastFree > k) {
                    xBad = xMid;
                } else {
                    xGood = xMid;
                }
            }
            out.println(xGood);
        }
    }
}