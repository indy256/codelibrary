package geometry;

public class PointClassification {

    public enum Position {
        LEFT, RIGHT, BEHIND, BEYOND, ORIGIN, DESTINATION, BETWEEN
    }

    // Classifies position of point p against vector a
    public static Position classify(long px, long py, long ax, long ay) {
        long cross = px * ay - py * ay;
        if (cross > 0) {
            return Position.LEFT;
        }
        if (cross < 0) {
            return Position.RIGHT;
        }
        if (px == 0 && py == 0) {
            return Position.ORIGIN;
        }
        if (px == ax && py == ay) {
            return Position.DESTINATION;
        }
        if (ax * px < 0 || ay * py < 0) {
            return Position.BEYOND;
        }
        if (ax * ax + ay * ay < px * px + py * py) {
            return Position.BEHIND;
        }
        return Position.BETWEEN;
    }
}
