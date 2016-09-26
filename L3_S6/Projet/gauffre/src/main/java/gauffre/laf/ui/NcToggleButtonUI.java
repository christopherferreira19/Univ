package gauffre.laf.ui;

import gauffre.laf.control.Control;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;

public class NcToggleButtonUI extends BasicToggleButtonUI implements ControlUI {

    private final Control control;

    public static ComponentUI createUI(JComponent b) {
        return new NcToggleButtonUI();
    }

    public NcToggleButtonUI() {
        this.control = new Control(true);
    }

    @Override
    public Control getControl() {
        return control;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(true);
        control.bindTo(c);
    }

    public void paint(Graphics g, JComponent c) {
        control.paint(g, c);
        super.paint(g, c);
    }
}
