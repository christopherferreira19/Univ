package gauffre.laf.layout;

import gauffre.laf.ui.NcTextFieldUI;

import javax.swing.text.JTextComponent;
import java.awt.*;

public class TextComponentLayout extends AbstractLayoutManager {

    public static final String LEADING = "LEADING";
    public static final String TRAILING = "TRAILING";

    private static final int MARGIN = 4;

    private Component leading;
    private Component trailing;

    public TextComponentLayout() {
        super();
    }

    @Override
    public void addComponent(Component component, Object constraints) {
        String value = (String) constraints;
        if (value == null || !value.equals(LEADING) && !value.equals(TRAILING)) {
            throw new IllegalArgumentException("Cannot add to layout: constraint must be 'LEADING' or 'TRAILING' string");
        }
        if (value.equals(LEADING)) {
            this.leading = component;
        }
        else {
            this.trailing = component;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent(Component component) {
        if (leading == component) {
            leading = null;
        }
        if (trailing == component) {
            trailing = null;
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Insets b = parent.getInsets();
        Dimension l = leading != null ? leading.getPreferredSize() : new Dimension();
        Dimension t = trailing != null ? trailing.getPreferredSize() : new Dimension();
        return new Dimension(b.left + l.width + t.width + b.right, b.top + b.bottom);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets b = parent.getInsets();
        int height = parent.getHeight() - b.top - b.bottom;
        int leadingWidth = 0;
        int trailingWidth = 0;
        if (leading != null) {
            leadingWidth = leading.getPreferredSize().width;
            leading.setBounds(MARGIN, b.top, leadingWidth, height);
        }
        if (trailing != null) {
            trailingWidth = trailing.getPreferredSize().width;
            int trailingX = parent.getWidth() - trailingWidth - MARGIN;
            trailing.setBounds(trailingX, b.top, trailingWidth, height);
        }

        JTextComponent textComponent = ((JTextComponent) parent);
        NcTextFieldUI ui = (NcTextFieldUI) textComponent.getUI();
        ui.setLeadingTrailingWidths(leadingWidth + MARGIN, trailingWidth + MARGIN);
    }
}