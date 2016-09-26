package q3.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class QuitAction extends AbstractAction {

    public QuitAction() {
        super("Quitter");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.exit(0);
    }
}
