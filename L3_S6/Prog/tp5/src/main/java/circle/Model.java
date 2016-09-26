package circle;

import javax.swing.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private static final int RADIUS = 5;

    private int fenetreIndex;
    private Point center;
    private final List<JComponent> components;

    public Model() {
        this.fenetreIndex = 1;
        this.center = null;
        this.components = new ArrayList<>();
    }

    public int getFenetreIndex() {
        return fenetreIndex;
    }

    public Point getCenter() {
        return center;
    }

    public int getRadius() {
        return RADIUS;
    }

    public void register(JComponent component) {
        this.components.add(component);
    }

    public void update(int fenetreIndex, Point center) {
        this.fenetreIndex = fenetreIndex;
        this.center = center;

        components.forEach(JComponent::repaint);
    }
}
