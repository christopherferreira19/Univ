package circle;

import javax.swing.*;

public class Circle implements Runnable {

    public void run() {
        Model model = new Model();
        Fenetre fenetre1 = new Fenetre(1, model);
        Fenetre fenetre2 = new Fenetre(2, model);

        fenetre1.setVisible(true);
        fenetre2.setVisible(true);
    }

    public static class Fenetre extends JFrame {

        public Fenetre(int index, Model model) {
            super("Fenetre " + index);
            add(new AireDeDessin(index, model));
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setSize(500, 200);
        }
    }

    public static void main(String [] args) {
        SwingUtilities.invokeLater(new Circle());
    }
}
