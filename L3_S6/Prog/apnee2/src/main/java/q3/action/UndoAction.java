package q3.action;

import q3.DessinArea;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UndoAction extends AbstractAction {

    private final DessinArea area;

    public UndoAction(DessinArea area) {
        super("Annuler");
        this.area = area;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        area.undo();
    }
}
