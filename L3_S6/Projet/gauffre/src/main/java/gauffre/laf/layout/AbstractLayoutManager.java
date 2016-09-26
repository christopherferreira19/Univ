package gauffre.laf.layout;

import java.awt.*;

public abstract class AbstractLayoutManager implements LayoutManager2 {

    @Override
    public void addLayoutComponent(final Component comp, final Object constraints) {
        addComponent(comp, constraints);
    }

    @Override
    public void addLayoutComponent(final String name, final Component comp) {
        addComponent(comp, name);
    }

    @Override
    public void removeLayoutComponent(final Component comp) {
        removeComponent(comp);
    }

    public void addComponent(final Component component, final Object constraints) {
        // Do nothing
    }

    public void removeComponent(final Component component) {
        // Do nothing
    }

    @Override
    public Dimension minimumLayoutSize(final Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public Dimension maximumLayoutSize(final Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(final Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(final Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(final Container target) {
        //
    }
}