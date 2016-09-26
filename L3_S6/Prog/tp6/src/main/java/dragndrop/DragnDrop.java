package dragndrop;

import javax.swing.*;
import java.awt.*;

public class DragnDrop implements Runnable {

    @Override
    public void run() {
        DragModel drag = new DragModel();

        JFrame frame = new JFrame("Fenetre");
        frame.add(new ImageToolBar(drag), BorderLayout.WEST);
        frame.add(new Dessin(drag));
        frame.setGlassPane(new DragPane(frame, drag));
        frame.getGlassPane().setVisible(true);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new DragnDrop());
    }
}
