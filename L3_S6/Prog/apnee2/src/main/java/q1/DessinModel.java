package q1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DessinModel {

    public static class Line {
        public final Point from;
        public final Point to;

        public Line(Point from, Point to) {
            this.from = from;
            this.to = to;
        }
    }

    private final List<JComponent> components;
    private final List<Line> lines;

    public DessinModel() {
        this.components = new ArrayList<>();
        this.lines = new ArrayList<>();
    }

    public void register(JComponent component) {
        this.components.add(component);
    }

    public void addLine(Point from, Point to) {
        this.lines.add(new Line(from, to));

        for (JComponent component : components) {
            component.repaint();
        }
    }

    public Iterable<Line> lines() {
        return lines;
    }
}
