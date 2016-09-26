package q3.action;

import q3.DessinArea;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewAction extends AbstractAction {

    private final DessinArea dessinArea;

    public NewAction(DessinArea dessinArea) {
        super("Nouveau");
        this.dessinArea = dessinArea;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        dessinArea.clear();
    }
}
