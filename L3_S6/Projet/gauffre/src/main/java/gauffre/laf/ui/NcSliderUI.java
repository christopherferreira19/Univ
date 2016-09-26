package gauffre.laf.ui;

import gauffre.laf.control.Control;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NcSliderUI extends BasicSliderUI {

    private static final Dimension HORIZONTAL_THUMB_SIZE = new Dimension(9, 20);
    private static final Dimension VERTICAL_THUMB_SIZE = new Dimension(20, 9);

    private final Control thumbControl;
    private final Control trackControl;

    public static ComponentUI createUI(JComponent c) {
        return new NcSliderUI((JSlider) c);
    }

    public NcSliderUI(JSlider b) {
        super(b);
        this.thumbControl = new Control(false);
        this.trackControl = new Control(false);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        SliderMouseAdapter mouseAdapter = new SliderMouseAdapter();
        slider.addMouseListener(mouseAdapter);
        slider.addMouseMotionListener(mouseAdapter);
    }

    protected Dimension getThumbSize() {
        return slider.getOrientation() == SwingConstants.HORIZONTAL
                ? HORIZONTAL_THUMB_SIZE
                : VERTICAL_THUMB_SIZE;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        thumbControl.setEnabled(slider.isEnabled());
        trackControl.setEnabled(slider.isEnabled());
        super.paint(g, c);
    }

    public void paintThumb(Graphics g) {
        thumbControl.setArmed(isDragging());
        thumbControl.paint(g, thumbRect);
        thumbControl.paintBorder(slider.hasFocus(), g, thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
    }

    @Override
    public void paintTrack(Graphics g) {
        if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
            trackControl.paint(g, trackRect.x, trackRect.y + 6, trackRect.width, trackRect.height - 12);
            trackControl.paintBorder(false, g,
                    trackRect.x, trackRect.y + 6, trackRect.width, trackRect.height - 12);
        } else {
            trackControl.paint(g, trackRect.x + 6, trackRect.y, trackRect.width - 12, trackRect.height);
            trackControl.paintBorder(false, g,
                    trackRect.x + 6, trackRect.y, trackRect.width - 12, trackRect.height);
        }
    }

    @Override
    public void paintFocus(Graphics g) {
    }

    private class SliderMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            slider.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            slider.repaint();
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            slider.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent event) {
            thumbControl.setRollover(true);
            trackControl.setRollover(true);
            slider.repaint();
        }

        @Override
        public void mouseExited(MouseEvent event) {
            thumbControl.setRollover(false);
            trackControl.setRollover(false);
            slider.repaint();
        }
    }
}
