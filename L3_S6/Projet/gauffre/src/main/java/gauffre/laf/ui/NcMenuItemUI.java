package gauffre.laf.ui;

import gauffre.laf.GraphicsUtils;
import gauffre.laf.Theme;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.text.View;
import java.awt.*;

public class NcMenuItemUI extends BasicMenuItemUI {

    private static final int ICON_WIDTH = 16;
    private static final int ACCELERATOR_GAP = 16;

    private String hotKeysText;

    public static ComponentUI createUI(JComponent c) {
        return new NcMenuItemUI();
    }

    public NcMenuItemUI() {
        this.hotKeysText = null;
    }

    public void initHotKeys(String hotKeysText) {
        this.hotKeysText = hotKeysText;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        menuItem.setOpaque(true);
        menuItem.setIconTextGap(7);
        menuItem.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 7));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        Object aa = GraphicsUtils.setupAntiAliasing(g2d);

        JMenuItem menuItem = (JMenuItem) c;
        int w = menuItem.getWidth();
        int h = menuItem.getHeight();
        Insets bi = menuItem.getInsets();
        int y = bi.top;
        int ih = h - bi.top - bi.bottom;
        ButtonModel model = menuItem.getModel();
        boolean selected = menuItem.isEnabled() && model.isArmed();

        int iconPlaceholderWidth = ICON_WIDTH;
        int gap = iconPlaceholderWidth > 0 ? menuItem.getIconTextGap() : 0;
        int x = bi.left;
        paintBackground(g2d, menuItem, x, y, iconPlaceholderWidth, ih, selected);
        paintIcon(g2d, menuItem, x, y, iconPlaceholderWidth, ih, selected);
        x += (iconPlaceholderWidth + gap);

        String text = menuItem.getText();
        boolean hasText = text != null && text.length() > 0;
        boolean hasAccelerator = hotKeysText != null;
        FontMetrics fm = menuItem.getFontMetrics(menuItem.getFont());
        if (hasText || hasAccelerator) {
            if (hasText) {
                View html = (View) menuItem.getClientProperty(BasicHTML.propertyKey);
                int tw = html != null ? (int) html.getPreferredSpan(View.X_AXIS) : fm.stringWidth(text);

                paintText(g2d, menuItem, fm, x, y, tw, ih, selected);
            }

            // Painting accelerator text
            if (hasAccelerator) {
                int aw = fm.stringWidth(hotKeysText);

                x = w - bi.right - aw;
                paintAcceleratorText(g2d, menuItem, fm, x, y, aw, ih, selected, true);
            }
        }

        GraphicsUtils.restoreAntiAliasing(g2d, aa);
    }

    protected void paintBackground(Graphics2D g2d, JMenuItem menuItem, int x, int y, int w, int h, boolean selected) {
        g2d.setColor(selected ? Theme.SELECTION_BACKGROUND : Theme.CONTEXT_MENU_BACKGROUND);
        g2d.fillRect(0, 0, menuItem.getWidth(), menuItem.getHeight());
    }

    private Color foreground(JMenuItem menuItem, boolean selected) {
        if (!menuItem.isEnabled()) {
            return Theme.DISABLED_FOREGROUND;
        }

        return selected ? Theme.SELECTION_FOREGROUND : Theme.FOREGROUND;
    }

    protected void paintText(Graphics2D g2d, JMenuItem menuItem, FontMetrics fm, int x, int y, int w,
                             int h, boolean selected) {
        g2d.setPaint(foreground(menuItem, selected));

        View html = (View) menuItem.getClientProperty(BasicHTML.propertyKey);
        if (html != null) {
            html.paint(g2d, new Rectangle(x, y, w, h));
        }
        else {
            g2d.drawString(menuItem.getText(), x, y + h / 2 + textCenterShearY(fm));
        }
    }

    protected void paintIcon(Graphics2D g2d, JMenuItem menuItem, int x, int y, int w, int h,
                             boolean selected) {
        boolean enabled = menuItem.isEnabled();
        Icon icon = menuItem.isSelected() && menuItem.getSelectedIcon() != null ?
                (enabled ? menuItem.getSelectedIcon() : menuItem.getDisabledSelectedIcon()) :
                (enabled ? menuItem.getIcon() : menuItem.getDisabledIcon());
        if (icon != null) {
            icon.paintIcon(menuItem, g2d, x, y + h / 2 - icon.getIconHeight() / 2);
        }
    }

    protected void paintAcceleratorText(Graphics2D g2d, JMenuItem menuItem, FontMetrics fm,
                                        int x, int y, int w, int h, boolean selected, boolean ltr) {
        g2d.setColor(foreground(menuItem, selected));
        g2d.drawString(hotKeysText, x, y + h / 2 + textCenterShearY(fm));
    }

    private int textCenterShearY(FontMetrics fm) {
        return (fm.getAscent() - fm.getLeading() - fm.getDescent()) / 2;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        JMenuItem menuItem = (JMenuItem) c;
        Insets bi = menuItem.getInsets();
        FontMetrics fm = menuItem.getFontMetrics(menuItem.getFont());

        View html = (View) menuItem.getClientProperty(BasicHTML.propertyKey);
        int textWidth;
        int textHeight;
        if (html != null) {
            textWidth = (int) html.getPreferredSpan(View.X_AXIS);
            textHeight = (int) html.getPreferredSpan(View.Y_AXIS);
        }
        else {
            String text = menuItem.getText();
            textWidth = text != null && text.length() > 0 ? fm.stringWidth(text) : 0;
            textHeight = fm.getHeight();
        }

        int gap = textWidth > 0 ? menuItem.getIconTextGap() : 0;
        int accWidth = hotKeysText != null ? ACCELERATOR_GAP + fm.stringWidth(hotKeysText) : 0;
        int iconHeight = menuItem.getIcon() != null ? menuItem.getIcon().getIconHeight() : 0;
        int contentHeight = Math.max(Math.max(iconHeight, textHeight), fm.getHeight());
        return new Dimension(bi.left + ICON_WIDTH + gap + textWidth + accWidth + bi.right + 20,
                bi.top + contentHeight + bi.bottom);
    }
}
