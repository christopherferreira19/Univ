package circle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class AireDeDessin extends JComponent {

    private final int index;
    private final Model model;
    private int counter;

    public AireDeDessin(final int indexParam, Model modelParam) {
        this.index = indexParam;
        this.model = modelParam;
        this.counter = 1;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point center = center();
                int radius = model.getRadius();
                Point click = e.getPoint();

                System.out.println("Click " + index + " " + click);

                double diffX = center.getX() - click.getX();
                double diffY = center.getY() - click.getY();
                boolean isInCircle = diffX * diffX + diffY * diffY <= radius * radius;
                if (model.getFenetreIndex() == index && isInCircle) {
                    model.update(index == 1 ? 2 : 1, null);
                }
                else {
                    model.update(index, new Point(click));
                }
            }
        });

        model.register(this);
    }

    public void paintComponent(Graphics g) {
        Graphics2D drawable = (Graphics2D) g;

        // On reccupere quelques infos provenant de la partie JComponent
        int width = getSize().width;
        int height = getSize().height;

        // On efface tout
        drawable.setPaint(Color.white);
        drawable.fillRect(0, 0, width, height);
        drawable.setPaint(Color.black);

        drawable.drawString("Bienvenue dans la fenetre " + index, 10, 20);

        if (model.getFenetreIndex() != index) {
            return;
        }

        // On affiche un petit message et un ovale
        Point center = center();

        int radius = model.getRadius();
        drawable.drawOval(
                center.x - radius,
                center.y - radius,
                2 * radius,
                2 * radius);
    }

    private Point center() {
        Point center = model.getCenter();
        if (center == null) {
            center = new Point(getWidth() / 2, getHeight() / 2);
        }
        return center;
    }
}
