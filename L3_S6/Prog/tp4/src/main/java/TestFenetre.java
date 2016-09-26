import java.awt.*;
import javax.swing.*;

public class TestFenetre implements Runnable {
    public void run() {
        // Creation d'une fenetre
        JFrame frame = new JFrame("Ma fenetre a moi");

        // Un clic sur le bouton de fermeture clos l'application
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creation et ajout de composants
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));
        JLabel label = new JLabel("Salut");
        ImageIcon image = new ImageIcon("get_powered_med.jpg");
        JButton button1 = new JButton("Ok");
        JButton button2 = new JButton("Cancel");

        button1.addActionListener(new EcouteurDeBouton("Touched", label));
        button2.addActionListener(new EcouteurDeBouton("Waiting", label));

        panel.add(label);
        panel.add(new JLabel(image));
        panel.add(button1);
        panel.add(button2);
        frame.add(panel);

        // On fixe la taille et on demarre
        frame.setSize(500, 200);
        frame.setVisible(true);
    }

    public static void main(String [] args) {
        SwingUtilities.invokeLater(new TestFenetre());
    }
}
