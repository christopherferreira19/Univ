package holedrilling;

import java.util.Objects;

public class Point {

    public final double x;
    public final double y;

    public static Point of(double x, double y) {
        return new Point(x, y);
    }

    private Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Point)) {
            return false;
        }

        Point other = (Point) object;
        return x == other.x && y == other.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static double distanceSq(Point left, Point right) {
        double distX = (left.x - right.x);
        double distY = (left.y - right.y);
        return distX * distX + distY * distY;
    }
}
