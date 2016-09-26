package q2;

import javax.swing.*;

public class Q2 implements Runnable {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;

    @Override
    public void run() {
        JFrame frame = new JFrame("Dessin");
        DessinArea area = new DessinArea();
        DessinListener dessinListener = new DessinListener(area);
        area.addMouseListener(dessinListener);
        area.addMouseMotionListener(dessinListener);

        frame.add(area);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Q2());
    }
}
