package circle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Circle implements Runnable, ActionListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final Model model;
    private final Timer timer;

    public Circle() {
        this.model = new Model(WIDTH / 2, HEIGHT / 2);
        this.timer = new Timer(20, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.tick();
    }

    @Override
    public void run() {
        JFrame fenetre = new JFrame();

        AireDeDessin aire = new AireDeDessin(model);
        aire.addMouseListener(new CircleMouseListener(model));
        fenetre.add(aire);

        AnimationBar bar = new AnimationBar(model);
        fenetre.add(bar, BorderLayout.SOUTH);

        fenetre.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenetre.setSize(WIDTH, HEIGHT);
        timer.start();
        fenetre.setVisible(true);
    }

    public static void main(String [] args) {
        SwingUtilities.invokeLater(new Circle());
    }
}
