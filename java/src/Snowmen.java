import mooc.EdxIO;
import java.util.*;

// ITMOx: I2CPx Snowmen
public class Snowmen {
    public static void main(String[] args) {
        try (EdxIO io = EdxIO.create()) {
            int n = io.nextInt();
            long sum = 0;
            int[] h = new int[n + 1];
            int[] p = new int[n + 1];
            p[0] = -1;
            for (int i = 1; i <= n; ++i) {
                int prev = io.nextInt();
                int chng = io.nextInt();
                if (chng == 0) {
                    h[i] = h[p[prev]];
                    p[i] = p[p[prev]];
                } else {
                    h[i] = h[prev] + chng;
                    p[i] = prev;
                }
                sum += h[i];
            }
            io.println(sum);
        }
    }
}