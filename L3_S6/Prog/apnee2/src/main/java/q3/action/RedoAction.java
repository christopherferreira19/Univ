package q3.action;

import q3.DessinArea;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RedoAction extends AbstractAction {

    private final DessinArea area;

    public RedoAction(DessinArea area) {
        super("Refaire");
        this.area = area;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        area.redo();
    }
}
