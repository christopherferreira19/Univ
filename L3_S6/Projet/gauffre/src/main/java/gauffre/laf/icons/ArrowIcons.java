package gauffre.laf.icons;

import javax.swing.*;
import java.awt.*;

public class ArrowIcons {

    private static final Color COLOR = new Color(96, 96, 96);
    private static final Color DISABLED_COLOR = new Color(177, 177, 177);

    private static final int SIZE_1 = 6;
    private static final int SIZE_2 = 7;
    private static final boolean[][] ARROW = {
            {false, false, false, true, false, false, false,},
            {false, false, true, true, true, false, false,},
            {false, true, true, true, true, true, false,},
            {true, true, true, false, true, true, true,},
            {true, true, false, false, false, true, true,},
            {true, false, false, false, false, false, true,},
    };

    public static Icon of(int orientation) {
        switch (orientation) {
            case SwingConstants.NORTH:
                return north();
            case SwingConstants.SOUTH:
                return south();
            case SwingConstants.WEST:
                return west();
            case SwingConstants.EAST:
                return east();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Icon north() {
        return new ArrowIcons.North();
    }

    public static Icon south() {
        return new ArrowIcons.South();
    }

    public static Icon west() {
        return new ArrowIcons.West();
    }

    public static Icon east() {
        return new ArrowIcons.East();
    }

    private ArrowIcons() {
    }

    private static class North implements Icon {

        @Override
        public int getIconWidth() {
            return SIZE_2;
        }

        @Override
        public int getIconHeight() {
            return SIZE_1;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
                        g.setColor(c.isEnabled() ? COLOR : DISABLED_COLOR);            g.setColor(c.isEnabled() ? COLOR : DISABLED_COLOR);g.setColor(c.isEnabled() ? COLOR : DISABLED_COLOR);

            for (int i = 0; i < SIZE_1; i++) {
                for (int j = 0; j < SIZE_2; j++) {
                    if (ARROW[i][j]) {
                        g.drawLine(x + j, y + i, x + j, y + i);
                    }
                }
            }
        }
    }

    private static class South implements Icon {

        @Override
        public int getIconWidth() {
            return SIZE_2;
        }

        @Override
        public int getIconHeight() {
            return SIZE_1;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(c.isEnabled() ? COLOR : DISABLED_COLOR);

            for (int i = 0; i < SIZE_1; i++) {
                for (int j = 0; j < SIZE_2; j++) {
                    if (ARROW[i][j]) {
                        g.drawLine(x + j, y + SIZE_1 - i, x + j, y + SIZE_1 - i);
                    }
                }
            }
        }
    }

    private static class West implements Icon {

        @Override
        public int getIconWidth() {
            return SIZE_1;
        }

        @Override
        public int getIconHeight() {
            return SIZE_2;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(c.isEnabled() ? COLOR : DISABLED_COLOR);

            for (int i = 0; i < SIZE_1; i++) {
                for (int j = 0; j < SIZE_2; j++) {
                    if (ARROW[i][j]) {
                        g.drawLine(x + i, y + j, x + i, y + j);
                    }
                }
            }
        }
    }

    private static class East implements Icon {

        @Override
        public int getIconWidth() {
            return SIZE_1;
        }

        @Override
        public int getIconHeight() {
            return SIZE_2;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(c.isEnabled() ? COLOR : DISABLED_COLOR);

            for (int i = 0; i < SIZE_1; i++) {
                for (int j = 0; j < SIZE_2; j++) {
                    if (ARROW[i][j]) {
                        g.drawLine(x + SIZE_1 - i, y + j, x + SIZE_1 - i, y + j);
                    }
                }
            }
        }
    }
}
