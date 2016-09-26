package grid;

import javax.swing.*;

public class Grid implements Runnable {

    private static final int N = 10;

    public void run() {
        GridModel model = new GridModel(N);

        JFrame frame = new JFrame("Grid");

        frame.add(new GridArea(model));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        frame.setVisible(true);
    }

    public static void main(String [] args) {
        SwingUtilities.invokeLater(new Grid());
    }
}
