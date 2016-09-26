package gauffre.laf.ui;

import gauffre.laf.NovintecLaf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

public class NcCheckBoxUI extends NcRadioButtonUI implements ControlUI {

    private static final int CHECKBOX_ICON_WIDTH = 16;
    private static final int CHECKBOX_ICON_HEIGHT = 16;

    private static final Icon CHECK_ICON = new ImageIcon(NovintecLaf.class.getResource("icons/CheckSymbol.png"));

    public static ComponentUI createUI(JComponent b) {
        return new NcCheckBoxUI();
    }

    @Override
    protected Icon createIcon() {
        return new CheckBoxIcon();
    }

    private class CheckBoxIcon implements Icon {

        private int xOffset = (CHECKBOX_ICON_WIDTH - CHECK_ICON.getIconWidth()) / 2;
        private int yOffset = (CHECKBOX_ICON_HEIGHT - CHECK_ICON.getIconHeight()) / 2;

        @Override
        public int getIconWidth() {
            return CHECKBOX_ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return CHECKBOX_ICON_HEIGHT;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            control.setEnabled(c.isEnabled());
            control.paint(g, x, y, CHECKBOX_ICON_WIDTH, CHECKBOX_ICON_HEIGHT);
            control.paintBorder(c.hasFocus(), g, x, y, CHECKBOX_ICON_WIDTH, CHECKBOX_ICON_HEIGHT);

            if (control.isSelected()) {
                CHECK_ICON.paintIcon(c, g, x + xOffset, y + yOffset);
            }
        }
    }
}
