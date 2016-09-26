package gauffre.laf.ui;

import gauffre.laf.control.Control;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.*;

public class NcButtonUI extends BasicButtonUI implements ControlUI {

    private final Control control;

    public static ComponentUI createUI(JComponent c) {
        return new NcButtonUI();
    }

    public NcButtonUI() {
        this.control = new Control(false);
    }

    @Override
    public Control getControl() {
        return control;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
        control.bindTo(c);
        this.defaultTextShiftOffset = 1;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        AbstractButton b = (AbstractButton)c;
        boolean noText = b.getText() == null;
        if (noText) {
            b.setText("Hack");
        }
        Dimension size = BasicGraphicsUtils.getPreferredButtonSize(b, b.getIconTextGap());
        if (noText) {
            b.setText(null);
        }
        return size;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        control.setEnabled(c.isEnabled());
        if (button.isBorderPainted()) {
            control.paint(g, c);
        }

        super.paint(g, c);
    }

    protected void paintButtonPressed(Graphics g, AbstractButton b){
        setTextShiftOffset();
    }
}
