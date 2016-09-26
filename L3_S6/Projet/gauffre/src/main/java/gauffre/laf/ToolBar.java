package gauffre.laf;

import gauffre.laf.layout.ToolBarLayout;
import gauffre.laf.ui.NcToolBarUI;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import java.awt.Component;
import java.util.List;
import java.util.function.Supplier;

public class ToolBar extends JToolBar {

    public ToolBar() {
        super();
    }

    public ToolBar(int orientation) {
        super(orientation);
    }

    public void setUndecorated(boolean undecorated) {
        ((NcToolBarUI) getUI()).setUndecorated(undecorated);
    }

    public Component add(Component component) {
        add(component, ToolBarLayout.START);
        return component;
    }

    public void addToMiddle(Component component) {
        add(component, ToolBarLayout.MIDDLE);
    }

    public void addFill(Component component) {
        add(component, ToolBarLayout.FILL);
    }

    public void addToEnd(Component component) {
        add(component, ToolBarLayout.END);
    }

    public Component add(Supplier<? extends Component> supplier) {
        return add(supplier.get());
    }

    public void addToMiddle(Supplier<? extends Component> supplier) {
        addToMiddle(supplier.get());
    }

    public void addFill(Supplier<? extends Component> supplier) {
        addFill(supplier.get());
    }

    public void addToEnd(Supplier<? extends Component> supplier) {
        addToEnd(supplier.get());
    }

    @Override
    public void addSeparator() {
        addSeparator(ToolBarLayout.START);
    }

    public JSeparator addSeparatorToEnd() {
        return addSeparator(ToolBarLayout.END);
    }

    public JSeparator addSeparator(String constraint) {
        JSeparator separator = new JSeparator(getOrientation());
        add(separator, constraint);
        return separator;
    }

    public void addSpacing(int spacing) {
        addSpacing(spacing, ToolBarLayout.START);
    }

    public void addSpacingToEnd(int spacing) {
        addSpacing(spacing, ToolBarLayout.END);
    }

    public void addSpacing(int spacing, String constraint) {
        add(new ToolBarLayout.Spacing(spacing), constraint);
    }

    public void addLabel(String text) {
        add(new JLabel(text));
    }

    public void addLabelToEnd(String text) {
        addToEnd(new JLabel(text));
    }

    /**
     * Additional childs interaction methods
     */

    public void add(List<? extends Component> components, int index) {
        if (components != null) {
            for (int i = 0; i < components.size(); i++) {
                add(components.get(i), index + i);
            }
        }
    }

    public void add(List<? extends Component> components, String constraints) {
        if (components != null) {
            for (Component component : components) {
                add(component, constraints);
            }
        }
    }

    public void add(List<? extends Component> components) {
        if (components != null) {
            for (Component component : components) {
                add(component);
            }
        }
    }

    public void add(int index, Component... components) {
        if (components != null && components.length > 0) {
            for (int i = 0; i < components.length; i++) {
                add(components[i], index + i);
            }
        }
    }

    public void add(String constraints, Component... components) {
        if (components != null && components.length > 0) {
            for (Component component : components) {
                add(component, constraints);
            }
        }
    }

    public void add(Component... components) {
        if (components != null && components.length > 0) {
            for (Component component : components) {
                add(component);
            }
        }
    }
}