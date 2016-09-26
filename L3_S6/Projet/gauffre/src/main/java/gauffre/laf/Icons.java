package gauffre.laf;

import gauffre.laf.icons.OffsetIcon;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.function.Supplier;

public enum Icons implements Supplier<Icon> {

    BULLET_BLACK,

    BULLET_GRAY,

    BULLET_GREEN,

    BULLET_RED,

    BULLET_YELLOW,

    CROSS,

    ERROR,

    FORBIDDEN,

    GEAR,

    HELP,

    INFORMATION,

    MAGNIFIER,

    PAUSE,

    PLAY,

    REDO,

    UNDO,

    QUESTION,

    SPINNER("gif"),

    TICK,

    WARNING,

    RELAUNCH,

    SORT_ASCENDING {
        @Override
        public Icon create() {
            return new OffsetIcon(UIManager.getIcon("Table.ascendingSortIcon"), 16, 16);
        }
    },

    SORT_DESCENDING {
        @Override
        public Icon create() {
            return new OffsetIcon(UIManager.getIcon("Table.descendingSortIcon"), 16, 16);
        }
    },

    EMPTY {
        @Override
        public Icon create() {
            return new EmptyIcon();
        }
    };

    private static final String DEFAULT_EXTENSION = "png";

    private final String extension;
    private WeakReference<Icon> reference;

    private Icons() {
        this(null);
    }

    private Icons(String extension) {
        this.extension = extension;
        this.reference = new WeakReference<Icon>(null);
    }

    @Override
    public synchronized Icon get() {
        Icon icon = reference.get();
        if (icon == null) {
            icon = create();
            reference = new WeakReference<Icon>(icon);
        }

        return icon;
    }

    protected Icon create() {
        URL resource = Icons.class.getResource(filename());
        if (resource == null) {
            return new ImageIcon(new byte[1]);
        }

        return new ImageIcon(resource);
    }

    private String filename() {
        return "icons/" + name().toLowerCase() + "." + (extension == null ? DEFAULT_EXTENSION : extension);
    }

    private static final class EmptyIcon implements Icon {

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
        }

        @Override
        public int getIconWidth() {
            return 0;
        }

        @Override
        public int getIconHeight() {
            return 0;
        }
    }
}


