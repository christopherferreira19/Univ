package gauffre.laf.ui;

import gauffre.laf.GraphicsUtils;
import gauffre.laf.Theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.text.View;
import java.awt.*;

public class NcToolTipUI extends BasicToolTipUI {

    private static final int MARGIN = 1;
    public static final Border BORDER = BorderFactory.createEmptyBorder(6, 6, 6, 6);

    public static ComponentUI createUI(JComponent c) {
        return new NcToolTipUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setBorder(BORDER);
        c.setForeground(Color.WHITE);
        c.setOpaque(false);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Dimension size = c.getSize();
        Graphics2D g2d = (Graphics2D) g;

        Object oldAntialiasing = GraphicsUtils.setupAntiAliasing(g2d);
        g2d.setPaint(new GradientPaint(0, 0, Theme.TOOLTIP_BORDER_COLOR_HI,
                0, size.height - 1, Theme.TOOLTIP_BORDER_COLOR_LO));
        g2d.drawRect(0, 0, size.width - 1, size.height - 1);
        g2d.setPaint(new GradientPaint(1, 1, Theme.TOOLTIP_BACKGROUND_HI,
                1, size.height - 3, Theme.TOOLTIP_BACKGROUND_LO));
        g2d.fillRect(1, 1, size.width - 2, size.height - 2);

        String tipText = ((JToolTip) c).getTipText();
        if (tipText == null) {
            tipText = "";
        }

        Insets insets = c.getInsets();
        Rectangle paintTextR = new Rectangle(
                insets.left + 3,
                insets.top,
                size.width - (insets.left + insets.right) - 6,
                size.height - (insets.top + insets.bottom));
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);

        if (v != null) {
            v.paint(g2d, paintTextR);
        }
        else {
            g2d.setFont(c.getFont());
            g2d.setColor(c.getForeground());
            FontMetrics metrics = g.getFontMetrics();
            g2d.drawString(tipText, paintTextR.x, paintTextR.y + metrics.getAscent());
        }

        GraphicsUtils.restoreAntiAliasing(g2d, oldAntialiasing);
    }
}
