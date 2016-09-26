package gauffre.laf.ui;

import gauffre.laf.GraphicsUtils;
import gauffre.laf.Theme;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class NcTabbedPaneUI extends BasicTabbedPaneUI {

    private enum ShapeType {
        shade,
        background,
        backgroundPainter,
        border
    }

    private FocusAdapter focusAdapter;

    public static ComponentUI createUI(JComponent c) {
        return new NcTabbedPaneUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        focusAdapter = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                tabPane.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                tabPane.repaint();
            }
        };
        tabPane.addFocusListener(focusAdapter);
    }

    @Override
    public void uninstallUI(JComponent c) {
        if (focusAdapter != null) {
            c.removeFocusListener(focusAdapter);
        }
        super.uninstallUI(c);
    }

    @Override
    protected int getTabRunIndent(int tabPlacement, int run) {
        return 0;
    }

    @Override
    protected int getTabRunOverlay(int tabPlacement) {
        return 1;
    }

    @Override
    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        Insets insets = new Insets(3, 4, 3, 4);
        if (tabIndex == 0 && tabPane.getSelectedIndex() == 0) {
            insets.left -= 1;
            insets.right += 1;
        }
        return insets;
    }

    @Override
    protected Insets getSelectedTabPadInsets(int tabPlacement) {
        Insets targetInsets = new Insets(0, 0, 0, 0);
        rotateInsets(new Insets(2, 2, 2, 1), targetInsets, tabPlacement);
        return targetInsets;
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                  boolean isSelected) {
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                      boolean isSelected) {
        Graphics2D g2d = (Graphics2D) g;

        GeneralPath borderShape = createTabShape(ShapeType.border, tabPlacement, x, y, w, h);

        GeneralPath bgShape = createTabShape(ShapeType.background, tabPlacement, x, y, w, h);
        Point top = getTopTabBgPoint(tabPlacement, x, y, w, h);
        Point bottomPoint = getBottomTabBgPoint(tabPlacement, x, y, w, h);
        if (isSelected) {
            g2d.setPaint(new GradientPaint(top.x, top.y, Color.WHITE,
                    bottomPoint.x, bottomPoint.y, Theme.BACKGROUND));
        }
        else if (tabIndex == getRolloverTab()) {
            g2d.setPaint(new GradientPaint(top.x, top.y, Theme.CONTROL_HOVER_BACKGROUND_HI,
                    bottomPoint.x, bottomPoint.y, Theme.CONTROL_HOVER_BACKGROUND_LO));
        }
        else {
            g2d.setPaint(new GradientPaint(top.x, top.y, Theme.TABS_BACKGROUND_HI,
                    bottomPoint.x, bottomPoint.y, Theme.TABS_BACKGROUND_LOW));
        }
        g2d.fill(isSelected ? borderShape : bgShape);

        if (isSelected && tabPane.isFocusOwner ()) {
            g2d.setPaint(Theme.FOCUS_BORDER_COLOR);
        }
        else {
            g2d.setPaint(Theme.CONTROL_BORDER_COLOR);
        }
        g2d.draw(borderShape);
    }

    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex,
                             String title, Rectangle textRect, boolean isSelected) {
        g.setFont(font);
        View v = getTextViewForTab(tabIndex);
        if (v != null) {
            v.paint(g, textRect);
        }
        else {
            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                Color fg = tabPane.getForegroundAt(tabIndex);
                if (isSelected && (fg instanceof UIResource)) {
                    Color selectedFG = UIManager.getColor("TabbedPane.selectedForeground");
                    if (selectedFG != null) {
                        fg = selectedFG;
                    }
                }
                g.setColor(fg);
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }
            else {
                g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
                g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
                g.drawString(title, textRect.x - 1, textRect.y + metrics.getAscent() - 1);
            }
        }
    }

    private GeneralPath createTabShape(ShapeType shapeType, int tabPlacement, int x, int y,
                                       int w, int h) {
        GeneralPath bgShape = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        if (tabPlacement == JTabbedPane.TOP) {
            bgShape.moveTo(x, y + h + getChange(shapeType));
            bgShape.lineTo(x, y);
            bgShape.quadTo(x, y, x, y);
            bgShape.lineTo(x + w, y);
            bgShape.quadTo(x + w, y, x + w, y);
            bgShape.lineTo(x + w, y + h + getChange(shapeType));
        }
        else if (tabPlacement == JTabbedPane.BOTTOM) {
            bgShape.moveTo(x, y - getChange(shapeType));
            bgShape.lineTo(x, y + h);
            bgShape.quadTo(x, y + h, x, y + h);
            bgShape.lineTo(x + w, y + h);
            bgShape.quadTo(x + w, y + h, x + w, y + h);
            bgShape.lineTo(x + w, y - getChange(shapeType));
        }
        else if (tabPlacement == JTabbedPane.LEFT) {
            bgShape.moveTo(x + w + getChange(shapeType), y);
            bgShape.lineTo(x, y);
            bgShape.quadTo(x, y, x, y);
            bgShape.lineTo(x, y + h);
            bgShape.quadTo(x, y + h, x, y + h);
            bgShape.lineTo(x + w + getChange(shapeType), y + h);
        }
        else {
            bgShape.moveTo(x - getChange(shapeType), y);
            bgShape.lineTo(x + w, y);
            bgShape.quadTo(x + w, y, x + w, y);
            bgShape.lineTo(x + w, y + h);
            bgShape.quadTo(x + w, y + h, x + w, y + h);
            bgShape.lineTo(x - getChange(shapeType), y + h);
        }
        return bgShape;
    }

    private int getChange(ShapeType shapeType) {
        if (shapeType.equals(ShapeType.shade) || shapeType.equals(ShapeType.border)) {
            return -1;
        }
        else if (shapeType.equals(ShapeType.backgroundPainter)) {
            return 2;
        }
        else {
            return 0;
        }
    }

    private Point getTopTabBgPoint(int tabPlacement, int x, int y, int w, int h) {
        if (tabPlacement == JTabbedPane.TOP) {
            return new Point(x, y);
        }
        else if (tabPlacement == JTabbedPane.BOTTOM) {
            return new Point(x, y + h);
        }
        else if (tabPlacement == JTabbedPane.LEFT) {
            return new Point(x, y);
        }
        else {
            return new Point(x + w, y);
        }
    }

    private Point getBottomTabBgPoint(int tabPlacement, int x, int y, int w, int h) {
        if (tabPlacement == JTabbedPane.TOP) {
            return new Point(x, y + h - 4);
        }
        else if (tabPlacement == JTabbedPane.BOTTOM) {
            return new Point(x, y + 4);
        }
        else if (tabPlacement == JTabbedPane.LEFT) {
            return new Point(x + w - 4, y);
        }
        else {
            return new Point(x + 4, y);
        }
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        Graphics2D g2d = (Graphics2D) g;

        int tabAreaSize = getTabAreaLength(tabPlacement);

        Insets bi = tabPane.getInsets();
        if (tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM) {
            bi.right += 1;
        }
        else {
            bi.bottom += 1;
        }

        Rectangle selected = selectedIndex != -1 ? getTabBounds(tabPane, selectedIndex) : null;
        Shape bs = createBackgroundShape(tabPlacement, tabAreaSize, bi, selected);

        GeneralPath clip = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        clip.append(new Rectangle2D.Double(0, 0, tabPane.getWidth(), tabPane.getHeight()), false);
        clip.append(bs, false);

        Color bg = selectedIndex != -1 ? tabPane.getBackgroundAt(selectedIndex) : null;
        g2d.setPaint(bg != null ? bg : tabPane.getBackground());
        g2d.fill(bs);

        if (tabPane.isFocusOwner()) {
            g2d.setPaint(Theme.FOCUS_BORDER_COLOR);
        }
        else {
            g2d.setPaint(Theme.CONTROL_BORDER_COLOR);
        }
        g2d.draw(bs);
    }

    private int getTabAreaLength(int tabPlacement) {
        return tabPlacement == JTabbedPane.TOP || tabPlacement == JTabbedPane.BOTTOM ?
                calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight) - 1 :
                calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth) - 1;
    }

    private Shape createBackgroundShape(int tabPlacement, int tabAreaSize, Insets bi, Rectangle selected) {
        if (selected != null) {
            GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
            if (tabPlacement == JTabbedPane.TOP) {
                int topY = bi.top + tabAreaSize;
                gp.moveTo(selected.x, topY);
                gp.lineTo(bi.left, topY);
                gp.lineTo(bi.left, tabPane.getHeight() - bi.bottom);
                gp.lineTo(tabPane.getWidth() - bi.right, tabPane.getHeight() - bi.bottom);
                gp.lineTo(tabPane.getWidth() - bi.right, topY);
                gp.lineTo(selected.x + selected.width, topY);
            }
            else if (tabPlacement == JTabbedPane.BOTTOM) {
                int bottomY = tabPane.getHeight() - bi.bottom - tabAreaSize;
                gp.moveTo(selected.x, bottomY);
                gp.lineTo(bi.left, bottomY);
                gp.lineTo(bi.left, bi.top);
                gp.lineTo(tabPane.getWidth() - bi.right, bi.top);
                gp.lineTo(tabPane.getWidth() - bi.right, bottomY);
                gp.lineTo(selected.x + selected.width, bottomY);
            }
            else if (tabPlacement == JTabbedPane.LEFT) {
                int leftX = bi.left + tabAreaSize;
                gp.moveTo(leftX, selected.y);
                gp.lineTo(leftX, bi.top);
                gp.lineTo(tabPane.getWidth() - bi.right, bi.top);
                gp.lineTo(tabPane.getWidth() - bi.right, tabPane.getHeight() - bi.bottom);
                gp.lineTo(leftX, tabPane.getHeight() - bi.bottom);
                gp.lineTo(leftX, selected.y + selected.height);
            }
            else {
                int rightX = tabPane.getWidth() - bi.right - tabAreaSize;
                gp.moveTo(rightX, selected.y);
                gp.lineTo(rightX, bi.top);
                gp.lineTo(bi.left, bi.top);
                gp.lineTo(bi.left, tabPane.getHeight() - bi.bottom);
                gp.lineTo(rightX, tabPane.getHeight() - bi.bottom);
                gp.lineTo(rightX, selected.y + selected.height);
            }
            return gp;
        }
        else {
            boolean top = tabPlacement == JTabbedPane.TOP;
            boolean bottom = tabPlacement == JTabbedPane.BOTTOM;
            boolean left = tabPlacement == JTabbedPane.LEFT;
            boolean right = tabPlacement == JTabbedPane.RIGHT;
            return new RoundRectangle2D.Double(bi.left + (left ? tabAreaSize : 0), bi.top + (top ? tabAreaSize : 0),
                    tabPane.getWidth() - bi.left - bi.right -
                            (left || right ? tabAreaSize : 0), tabPane.getHeight() - bi.top - bi.bottom -
                    (top || bottom ? tabAreaSize : 0), 0, 0
            );
        }
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
                                       Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        // We don't need this one
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        Object aa = GraphicsUtils.setupAntiAliasing(g2d);
        super.paint(g, c);
        GraphicsUtils.restoreAntiAliasing(g2d, aa);
    }

    protected void setRolloverTab(int index) {
        super.setRolloverTab(index);
        tabPane.repaint();
    }
}