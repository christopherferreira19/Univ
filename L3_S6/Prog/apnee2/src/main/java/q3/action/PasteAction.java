package q3.action;

import q3.DessinArea;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PasteAction extends AbstractAction {

    private final DessinArea area;

    public PasteAction(DessinArea area) {
        super("Coller");
        this.area = area;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        area.setPasteState();
    }
}
