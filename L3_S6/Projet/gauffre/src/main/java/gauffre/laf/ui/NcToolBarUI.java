package gauffre.laf.ui;

import gauffre.laf.Theme;
import gauffre.laf.layout.ToolBarLayout;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;

public class NcToolBarUI extends BasicToolBarUI {

    private boolean undecorated;

    public static ComponentUI createUI(JComponent component) {
        return new NcToolBarUI();
    }


    public boolean isUndecorated() {
        return undecorated;
    }

    public void setUndecorated(boolean undecorated) {
        this.undecorated = undecorated;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        this.undecorated = false;
        toolBar.setLayout(new ToolBarLayout());
        toolBar.setFloatable(false);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (undecorated) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        drawBackground(c, g2d);
        drawBorder(c, g2d);
        super.paint(g, c);
    }

    protected void drawBackground(JComponent c, Graphics2D g2d) {
        g2d.setPaint(new GradientPaint(0, 0, Theme.TOOLBAR_BACKGROUND_HI_COLOR,
                0, c.getHeight() - 1, Theme.TOOLBAR_BACKGROUND_LO_COLOR));
        g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
    }

    protected void drawBorder(JComponent c, Graphics2D g2d) {
        LayoutManager layout = toolBar.getParent().getLayout();
        if (!(layout instanceof BorderLayout)) {
            return;
        }

        String border = (String) ((BorderLayout) layout).getConstraints(toolBar);
        g2d.setColor(Theme.TOOLBAR_BORDER_COLOR);
        if (border.equals(BorderLayout.NORTH)) {
            g2d.drawLine(0, c.getHeight() - 1, c.getWidth() - 1, c.getHeight() - 1);
        }
        else if (border.equals(BorderLayout.SOUTH)) {
            g2d.drawLine(0, 0, c.getWidth() - 1, 0);
        }
        else if (border.equals(BorderLayout.WEST)) {
            g2d.drawLine(c.getWidth() - 1, 0, c.getWidth() - 1, c.getHeight() - 1);
        }
        else if (border.equals(BorderLayout.EAST)) {
            g2d.drawLine(0, 0, 0, c.getHeight() - 1);
        }

    }
}
