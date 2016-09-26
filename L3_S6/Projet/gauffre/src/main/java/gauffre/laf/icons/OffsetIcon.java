package gauffre.laf.icons;

import javax.swing.*;
import java.awt.*;

public class OffsetIcon implements Icon {

    private final Icon delegate;
    private final int width;
    private final int height;

    public OffsetIcon(Icon delegate, int width, int height) {
        this.delegate = delegate;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int offsetX = (width - delegate.getIconWidth()) / 2;
        int offsetY = (width - delegate.getIconWidth()) / 2;
        delegate.paintIcon(c, g, x + offsetX, y + offsetY);
    }
}
