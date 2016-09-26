package q1;

import javax.swing.*;
import java.awt.*;

public class DessinArea extends JComponent {

    private final DessinModel model;

    public DessinArea(DessinModel model) {
        this.model = model;
        model.register(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getWidth(), getHeight());


        g2.setColor(Color.BLACK);
        for (DessinModel.Line line : model.lines()) {
            g2.drawLine(line.from.x, line.from.y, line.to.x, line.to.y);
        }
    }
}
