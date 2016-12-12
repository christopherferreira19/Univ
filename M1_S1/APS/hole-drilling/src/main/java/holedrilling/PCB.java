package holedrilling;

import java.util.*;

public class PCB {

    public final int width;
    public final int height;

    public final Point start;
    public final List<Point> holes;

    public static PCB random(int width, int height, int pointsCount) {
        List<Point> holes = new ArrayList<>();
        Set<Point> existing = new HashSet<>();
        Random random = new Random();

        Point start = randomPoint(width, height, existing, random);
        existing.add(start);

        for (int i = 0; i < pointsCount; i++) {
            Point hole = randomPoint(width, height, existing, random);
            holes.add(hole);
            existing.add(hole);
        }

        return new PCB(width, height, start, holes);
    }

    private static Point randomPoint(int width, int height, Set<Point> existing, Random random) {
        Point hole;
        do {
            double x = random.nextDouble() * width;
            double y = random.nextDouble() * height;
            hole = Point.of(x, y);
        } while (existing.contains(hole));
        return hole;
    }

    private PCB(int width, int height, Point start, List<Point> point) {
        this.width = width;
        this.height = height;
        this.start = start;
        this.holes = point;
    }
}
