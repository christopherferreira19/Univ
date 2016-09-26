package circle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private static final double EPSILON = 0.5;
    private static final int RADIUS = 10;

    private double x;
    private double y;
    private double dX;
    private double dY;

    private int step;
    private int stepsCount;

    private final List<ModelObserver> observers;

    public Model(int x, int y) {
        this.x = x;
        this.y = y;
        this.dX = 0;
        this.dY = 0;
        this.step = 0;
        this.stepsCount = 0;

        this.observers = new ArrayList<>();
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getRadius() {
        return RADIUS;
    }

    public boolean isMoving() {
        return step < stepsCount;
    }

    public int getStep() {
        return step;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public void register(ModelObserver observer) {
        this.observers.add(observer);
    }

    public void targetCenter(Point center) {
        double dx = (double) center.x - x;
        double dy = (double) center.y - y;

        this.step = 0;
        this.stepsCount = (int) Math.sqrt(dx * dx + dy * dy) / 3;
        this.dX = dx / stepsCount;
        this.dY = dy / stepsCount;

        observers.forEach(ModelObserver::changed);
    }

    public void tick() {
        if (!isMoving()) {
            return;
        }

        step++;
        x += dX;
        y += dY;
        observers.forEach(ModelObserver::changed);
    }
}
