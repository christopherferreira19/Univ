package gauffre.laf.ui;

import gauffre.laf.layout.ExtendBarScrollPaneLayout;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

public class NcScrollPaneUI extends BasicScrollPaneUI {

    public static ComponentUI createUI(JComponent c) {
        return new NcScrollPaneUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setLayout(new ExtendBarScrollPaneLayout());
    }
}
