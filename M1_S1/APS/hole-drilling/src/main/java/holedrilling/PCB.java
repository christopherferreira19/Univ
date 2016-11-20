package holedrilling;

import java.util.*;

public class PCB {

    public final int width;
    public final int height;

    public final List<Point> holes;

    public static PCB random(int width, int height, int pointsCount) {
        List<Point> holes = new ArrayList<>();
        Set<Point> existing = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < pointsCount; i++) {
            Point hole;
            do {
                double x = random.nextDouble() * width;
                double y = random.nextDouble() * height;
                hole = Point.of(x, y);
            } while (existing.contains(hole));

            holes.add(hole);
            existing.add(hole);
        }

        return new PCB(width, height, holes);
    }

    private PCB(int width, int height, List<Point> point) {
        this.width = width;
        this.height = height;
        this.holes = point;
    }
}
