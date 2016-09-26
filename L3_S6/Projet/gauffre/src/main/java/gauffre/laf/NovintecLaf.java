package gauffre.laf;

import gauffre.laf.icons.ArrowIcons;
import gauffre.laf.ui.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

public class NovintecLaf extends MetalLookAndFeel {

    public NovintecLaf() {
        setCurrentTheme(new DefaultMetalTheme());
    }

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);

        Object[] uiDefaults = {
                "ButtonUI", NcButtonUI.class.getCanonicalName(),
                "ToggleButtonUI", NcToggleButtonUI.class.getCanonicalName(),
                "RadioButtonUI", NcRadioButtonUI.class.getCanonicalName(),
                "CheckBoxUI", NcCheckBoxUI.class.getCanonicalName(),
                "ComboBoxUI", NcComboBoxUI.class.getCanonicalName(),
                "ScrollBarUI", NcScrollBarUI.class.getCanonicalName(),
                "ScrollPaneUI", NcScrollPaneUI.class.getCanonicalName(),
                "MenuItemUI", NcMenuItemUI.class.getCanonicalName(),
                "TabbedPaneUI", NcTabbedPaneUI.class.getCanonicalName(),
                "ToolBarUI", NcToolBarUI.class.getCanonicalName(),
                "ToolTipUI", NcToolTipUI.class.getCanonicalName(),
                "TextFieldUI", NcTextFieldUI.class.getCanonicalName(),
                "SliderUI", NcSliderUI.class.getCanonicalName(),
        };

        table.putDefaults(uiDefaults);
    }

    @Override
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);

        for (Object keyObject : table.keySet()) {
            if (keyObject instanceof String) {
                String key = (String) keyObject;
                if (key.endsWith(".font")) {
                    table.put(key, Theme.FONT);
                }
                else if (key.endsWith(".background")) {
                    table.put(key, Theme.BACKGROUND);
                }
                else if (key.endsWith(".foreground")) {
                    table.put(key, Theme.FOREGROUND);
                }
                else if (key.endsWith(".selectionBackground")) {
                    table.put(key, Theme.SELECTION_BACKGROUND);
                }
                else if (key.endsWith(".selectionForeground")) {
                    table.put(key, Theme.SELECTION_FOREGROUND);
                }
            }
        }

        BorderUIResource textBorder = new BorderUIResource(new TextBorder());

        Object[] uiDefaults = {
                "ScrollPane.border", Theme.SCROLL_PANE_BORDER,
                "ScrollBar.track", Theme.BACKGROUND,

                "List.background", Theme.LIST_BACKGROUND,
                "List.scrollPaneBorder", Theme.SCROLL_PANE_BORDER,
                "ComboBox.background", Theme.LIST_BACKGROUND,
                "Table.background", Theme.LIST_BACKGROUND,

                "Menu.background", Theme.LIST_BACKGROUND,
                "PopupMenu.background", Theme.LIST_BACKGROUND,
                "PopupMenu.border", Theme.CONTEXT_MENU_BORDER,
                "Separator.foreground", Theme.CONTEXT_MENU_BORDER_COLOR,

                "Table.gridColor", Theme.TABLE_GRID_COLOR,
                "List.focusCellHighlightBorder", Theme.TABLE_CELL_FOCUS_BORDER,
                "Table.focusCellHighlightBorder", Theme.TABLE_CELL_FOCUS_BORDER,
                "Table.focusCellForeground", Theme.FOREGROUND,
                "Table.scrollPaneBorder", Theme.SCROLL_PANE_BORDER,
                "Table.ascendingSortIcon", new IconUIResource(ArrowIcons.north()),
                "Table.descendingSortIcon", new IconUIResource(ArrowIcons.south()),

                "TextField.background", Theme.TEXT_BACKGROUND,
                "TextField.border", textBorder,
                "TextArea.background", Theme.TEXT_BACKGROUND,
                "TextArea.border", textBorder,
                "TextPane.background", Theme.TEXT_BACKGROUND,
                "TextPane.border", textBorder,

                "OptionPane.errorIcon", iconUiResource("error.png"),
                "OptionPane.informationIcon", iconUiResource("information.png"),
                "OptionPane.questionIcon", iconUiResource("question.png"),
                "OptionPane.warningIcon", iconUiResource("warning.png"),

                "SplitPane.oneTouchButtonOffset", 0,

                "Slider.tickColor", Theme.FOREGROUND,
        };
        table.putDefaults(uiDefaults);
    }

    private IconUIResource iconUiResource(String name) {
        return new IconUIResource(new ImageIcon(getClass().getResource("icons/" + name)));
    }

    private class TextBorder implements Border {

        private Insets insets = new Insets(4, 4, 4, 4);

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;

            Color shadow = c.getBackground();
            for (int i = 4; i >= 1; i--) {
                shadow = new Color(shadow.getRed() - 2, shadow.getGreen() - 2, shadow.getBlue() - 2);
                g2d.setColor(shadow);
                g2d.drawLine(x + 1, y + i, x + width - 2, y + i);
            }

            Object oldAntiAliasing = GraphicsUtils.setupAntiAliasing(g2d);
            g2d.setColor(c.hasFocus()
                    ? Theme.FOCUS_BORDER_COLOR
                    : Theme.CONTROL_BORDER_COLOR);
            g2d.drawRect(x, y, width - 1, height - 1);//, 6, 6);
            GraphicsUtils.restoreAntiAliasing(g2d, oldAntiAliasing);
        }
    }
}
