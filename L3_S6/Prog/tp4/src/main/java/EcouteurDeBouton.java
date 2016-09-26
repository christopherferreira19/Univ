import java.awt.event.*;
import javax.swing.*;

class EcouteurDeBouton implements ActionListener {
    String message;
    JLabel label;

    EcouteurDeBouton(String s, JLabel l) {
        message = s;
        label = l;
    }

    public void actionPerformed(ActionEvent e) {
        label.setText(message);
    }
}
