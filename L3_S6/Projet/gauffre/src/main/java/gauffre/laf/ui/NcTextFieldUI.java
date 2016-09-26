package gauffre.laf.ui;

import gauffre.laf.GraphicsUtils;
import gauffre.laf.Theme;
import gauffre.laf.layout.TextComponentLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class NcTextFieldUI extends BasicTextFieldUI {

    private final JTextField             textField;
    private final TextFieldBorder        border;
    private       TextFieldFocusListener focusListener;

    public NcTextFieldUI(JTextField textField) {
        this.textField = textField;
        this.border = new TextFieldBorder();
        this.focusListener = new TextFieldFocusListener();
    }

    public static ComponentUI createUI(JComponent component) {
        return new NcTextFieldUI((JTextField) component);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.addFocusListener(focusListener);
        c.setLayout(new TextComponentLayout());
        c.setBorder(border);
    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        c.removeFocusListener(focusListener);
    }

    public void setLeadingTrailingWidths(int leadingWidth, int trailingWidth) {
        border.insets.left = 4 + leadingWidth;
        border.insets.right = 4 + trailingWidth;
    }

    private static class TextFieldFocusListener implements FocusListener {

        public void focusGained(FocusEvent e) {
            e.getComponent().repaint();
        }

        public void focusLost(FocusEvent e) {
            e.getComponent().repaint();
        }
    }

    private class TextFieldBorder implements Border {

        private final Insets insets = new Insets(4, 4, 4, 4);

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;

            Color shadow = c.getBackground();
            for (int i = 4; i >= 1; i--) {
                shadow = new Color(shadow.getRed() - 2, shadow.getGreen() - 2, shadow.getBlue() - 2);
                g2d.setColor(shadow);
                g2d.drawLine(x + 1, y + i, x + width - 2, y + i);
            }

            Object oldAntiAliasing = GraphicsUtils.setupAntiAliasing(g2d);
            g2d.setColor(c.hasFocus()
                    ? Theme.FOCUS_BORDER_COLOR
                    : Theme.CONTROL_BORDER_COLOR);
            g2d.drawRect(x, y, width - 1, height - 1);
            GraphicsUtils.restoreAntiAliasing(g2d, oldAntiAliasing);
        }
    }
}
