package circle;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class CircleMouseListener extends MouseAdapter {

    private final Model model;

    CircleMouseListener(Model model) {
        this.model = model;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        model.targetCenter(new Point(e.getPoint()));
    }
}
