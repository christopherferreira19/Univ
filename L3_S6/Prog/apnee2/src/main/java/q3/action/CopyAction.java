package q3.action;

import q3.DessinArea;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CopyAction extends AbstractAction {

    private final DessinArea area;

    public CopyAction(DessinArea area) {
        super("Copier");
        this.area = area;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        area.setCopyState();
    }
}
