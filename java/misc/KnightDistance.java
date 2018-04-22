package misc;

public class KnightDistance {

    public static int dist(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int moves = Math.max((dx + 1) / 2, (dy + 1) / 2);
        moves = Math.max(moves, (dx + dy + 2) / 3);
        if (moves % 2 != (dx + dy) % 2)
            ++moves;
        if (dx == 1 && dy == 0)
            return 3;
        if (dy == 1 && dx == 0)
            return 3;
        if (dx == 2 && dy == 2)
            return 4;
        return moves;
    }

    public static void main(String[] args) {

    }
}
