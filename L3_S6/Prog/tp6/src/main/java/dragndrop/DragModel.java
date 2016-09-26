package dragndrop;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class DragModel {

    private final List<DragObserver> observers;

    private boolean ongoing;
    private Image image;
    private Point point;


    public DragModel() {
        this.observers = new ArrayList<>();

        this.ongoing = false;
        this.point = null;
    }

    public void register(DragObserver observer) {
        this.observers.add(observer);
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public Image getImage() {
        return image;
    }

    public Point getPoint() {
        return point;
    }

    public void start(Image image, Point point) {
        if (ongoing) {
            return;
        }

        this.ongoing = true;
        this.image = image;
        this.point = point;

        observers.forEach(DragObserver::started);
    }

    public void update(Point point) {
        if (!ongoing) {
            return;
        }

        this.point = point;
        observers.forEach(DragObserver::updated);
    }

    public void cancel() {
        if (!ongoing) {
            return;
        }

        this.ongoing = false;
        observers.forEach(DragObserver::cancelled);
    }

    public void stop() {
        if (!ongoing) {
            return;
        }

        this.ongoing = false;
        observers.forEach(DragObserver::stopped);
    }
}
