package circle;

import javax.swing.*;
import java.awt.*;

class AireDeDessin extends JComponent implements ModelObserver {

    private final Model model;

    public AireDeDessin(Model modelParam) {
        this.model = modelParam;
        model.register(this);
    }

    @Override
    public void changed() {
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // On reccupere quelques infos provenant de la partie JComponent
        int width = getSize().width;
        int height = getSize().height;

        // On efface tout
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, width, height);
        g2.setPaint(Color.black);

        int x = model.getX();
        int y = model.getY();
        int radius = model.getRadius();
        boolean moving = model.isMoving();
        // On affiche un petit message et un ovale
        g2.setColor(moving ? Color.RED : Color.BLUE);
        g2.fillOval(
                x - radius,
                y - radius,
                2 * radius,
                2 * radius);
    }

}
