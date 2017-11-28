import java.util.ArrayDeque;
import java.util.Deque;

import mooc.EdxIO;

//ITMOx: I2CPx Kenobi's Lightsabers
public class Kenobi {
    public static void main(String[] args) {
        try (EdxIO io = EdxIO.create()) {
            int n = io.nextInt();
            Deque<Integer> l = new ArrayDeque<>();
            Deque<Integer> r = new ArrayDeque<>();
            for (int i = 0; i < n; ++i) {
                String cmd = io.next();
                switch (cmd.charAt(0)) {
                    case 'a': r.addLast(io.nextInt()); break;
                    case 't': r.removeLast(); break;
                    case 'm': {
                        Deque<Integer> tmp = l;
                        l = r;
                        r = tmp;
                        break;
                    }
                }
                if (l.size() > r.size()) {
                    r.addFirst(l.removeLast());
                } else if (l.size() < r.size() - 1) {
                    l.addLast(r.removeFirst());
                }
            }
            l.addAll(r);
            io.println(l.size());
            for (int i : l) {
                io.print(i).print(' ');
            }
            io.println();
        }
    }
}