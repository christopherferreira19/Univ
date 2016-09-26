package gauffre.laf.control;

import gauffre.laf.Theme;

import javax.swing.*;
import java.awt.*;

import static gauffre.laf.GraphicsUtils.*;

public class Control {

    private final boolean drawSelectedBackground;

    private boolean enabled;
    private boolean armed;
    private boolean rollover;
    private boolean selected;

    private int arc;
    private boolean roundedTopLeft;
    private boolean roundedTopRight;
    private boolean roundedBottomLeft;
    private boolean roundedBottomRight;


    public Control(boolean drawSelectedBackground) {
        this.drawSelectedBackground = drawSelectedBackground;

        this.enabled = true;
        this.armed = false;
        this.rollover = false;
        this.selected = false;

        this.arc = 6;
        this.roundedTopLeft = true;
        this.roundedTopRight = true;
        this.roundedBottomLeft = true;
        this.roundedBottomRight = true;
    }

    public void bindTo(JComponent component) {
        component.setBorder(new ControlBorder(this));
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            button.setRolloverEnabled(true);
            button.getModel().addChangeListener(new ButtonModelBinding(this));
        }
        else {
            ControlListener controlListener = new ControlListener(this);
            component.addMouseListener(controlListener);
            component.addMouseMotionListener(controlListener);
            component.addFocusListener(controlListener);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isArmed() {
        return armed;
    }

    public void setArmed(boolean armed) {
        this.armed = armed;
    }

    public boolean isRollover() {
        return rollover;
    }

    public void setRollover(boolean rollover) {
        this.rollover = rollover;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getArc() {
        return arc;
    }

    public void setArc(int arc) {
        this.arc = arc;
    }

    public boolean isRoundedTopLeft() {
        return roundedTopLeft;
    }

    public void setRoundedTopLeft(boolean roundedTopLeft) {
        this.roundedTopLeft = roundedTopLeft;
    }

    public boolean isRoundedTopRight() {
        return roundedTopRight;
    }

    public void setRoundedTopRight(boolean roundedTopRight) {
        this.roundedTopRight = roundedTopRight;
    }

    public boolean isRoundedBottomLeft() {
        return roundedBottomLeft;
    }

    public void setRoundedBottomLeft(boolean roundedBottomLeft) {
        this.roundedBottomLeft = roundedBottomLeft;
    }

    public boolean isRoundedBottomRight() {
        return roundedBottomRight;
    }

    public void setRoundedBottomRight(boolean roundedBottomRight) {
        this.roundedBottomRight = roundedBottomRight;
    }

    public void setRounded(boolean rounded) {
        setRounded(rounded, rounded, rounded, rounded);
    }

    public void setRounded(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        this.roundedTopLeft = topLeft;
        this.roundedTopRight = topRight;
        this.roundedBottomLeft = bottomLeft;
        this.roundedBottomRight = bottomRight;
    }

    public void paint(Graphics g, Rectangle rect) {
        paint(g, rect.x, rect.y, rect.width, rect.height);
    }

    public void paint(Graphics g, JComponent component) {
        paint(g, 0, 0, component.getWidth(), component.getHeight());
    }

    public void paint(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        Object oldAntiAliasing = setupAntiAliasing(g2d);
        if (selected && drawSelectedBackground) {
            int midHeight = height / 2;
            GradientPaint selectedPaint = selectedControlPaint(x, y, midHeight);
            g2d.setPaint(selectedPaint);
            g2d.fillRoundRect(x, y, width, midHeight, arc, arc);
            g2d.setPaint(selectedPaint.getColor2());
            g2d.fillRoundRect(x, y + midHeight - 1, width, midHeight + 1, arc, arc);
        }
        else {
            g2d.setPaint(controlPaint(x, y, height));
            fillRounded(g2d, x, y, width, height, this);
        }
        restoreAntiAliasing(g2d, oldAntiAliasing);
    }

    public GradientPaint controlPaint(int x, int y, int height) {
        Color hiColor;
        Color lowColor;
        if (!enabled) {
            hiColor = Theme.CONTROL_DISABLED_BACKGROUND_HI;
            lowColor = Theme.CONTROL_DISABLED_BACKGROUND_LO;
        }
        else if (armed) {
            hiColor = Theme.CONTROL_ARMED_BACKGROUND_HI;
            lowColor = Theme.CONTROL_ARMED_BACKGROUND_LO;
        }
        else if (rollover) {
            hiColor = Theme.CONTROL_HOVER_BACKGROUND_HI;
            lowColor = Theme.CONTROL_HOVER_BACKGROUND_LO;
        }
        else {
            hiColor = Theme.CONTROL_BACKGROUND_HI;
            lowColor = Theme.CONTROL_BACKGROUND_LO;
        }

        return new GradientPaint(x, y, hiColor, x, y + height, lowColor);
    }

    private GradientPaint selectedControlPaint(int x, int y, int midHeight) {
        Color hiColor;
        Color lowColor;
        if (!enabled) {
            hiColor = Theme.CONTROL_DISABLED_SELECTED_BACKGROUND_HI;
            lowColor = Theme.CONTROL_DISABLED_SELECTED_BACKGROUND_LO;
        }
        else if (armed) {
            hiColor = Theme.CONTROL_SELECTED_ARMED_BACKGROUND_HI;
            lowColor = Theme.CONTROL_SELECTED_ARMED_BACKGROUND_LO;
        }
        else if (rollover) {
            hiColor = Theme.CONTROL_SELECTED_HOVER_BACKGROUND_HI;
            lowColor = Theme.CONTROL_SELECTED_HOVER_BACKGROUND_LO;
        }
        else {
            hiColor = Theme.CONTROL_SELECTED_BACKGROUND_HI;
            lowColor = Theme.CONTROL_SELECTED_BACKGROUND_LO;
        }

        return new GradientPaint(x, y, hiColor, x, y + midHeight, lowColor);
    }

    public void paintBorder(boolean focus, Graphics g, int x, int y, int w, int h) {
        Graphics2D g2d = (Graphics2D) g;
        Object oldAntiAliasing = setupAntiAliasing(g2d);
        g2d.setPaint(borderPaint(focus));
        drawRounded(g, x, y, w - 1, h - 1, this);
        if (!selected) {
            g2d.setPaint(internalBorderPaint(x, y, h));
            drawRounded(g, x + 1, y + 1, w - 3, h - 3, this);
        }
        restoreAntiAliasing(g2d, oldAntiAliasing);
    }

    public GradientPaint internalBorderPaint(int x, int y, int h) {
        return !enabled
                ? new GradientPaint(x + 1, y + 1, Theme.CONTROL_DISABLED_INTERNAL_BORDER_HI_COLOR,
                        x + 1, y + h - 3, Theme.CONTROL_DISABLED_INTERNAL_BORDER_LOW_COLOR)
                : new GradientPaint(x + 1, y + 1, Theme.CONTROL_INTERNAL_BORDER_HI_COLOR,
                        x + 1, y + h - 3, Theme.CONTROL_INTERNAL_BORDER_LOW_COLOR);
    }

    public Color borderPaint(boolean focus) {
        if (!enabled) {
            return Theme.CONTROL_DISABLED_BORDER_COLOR;
        }
        else if (focus) {
            return Theme.FOCUS_BORDER_COLOR;
        }
        else if (selected && drawSelectedBackground) {
            return Theme.CONTROL_SELECTED_BORDER_COLOR;
        }
        else {
            return Theme.CONTROL_BORDER_COLOR;
        }
    }
}
