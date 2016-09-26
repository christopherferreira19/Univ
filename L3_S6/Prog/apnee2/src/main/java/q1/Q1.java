package q1;

import javax.swing.*;

public class Q1 implements Runnable {

    @Override
    public void run() {
        JFrame frame = new JFrame("Dessin");
        DessinModel model = new DessinModel();
        DessinArea area = new DessinArea(model);
        DessinListener dessinListener = new DessinListener(model);
        area.addMouseListener(dessinListener);
        area.addMouseMotionListener(dessinListener);

        frame.add(area);
        frame.setSize(800, 500);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Q1());
    }
}
