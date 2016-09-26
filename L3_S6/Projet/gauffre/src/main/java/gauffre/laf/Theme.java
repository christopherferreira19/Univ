package gauffre.laf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import java.awt.*;

public class Theme {

    public static final Font FONT = Fonts.regular();

    public static final Color BACKGROUND = new ColorUIResource(244, 244, 244);
    public static final Color FOREGROUND = new ColorUIResource(50, 50, 50);
    public static final Color DISABLED_FOREGROUND = new ColorUIResource(120, 120, 120);

    public static final Color SELECTION_BACKGROUND = new ColorUIResource(211, 211, 211);
    public static final Color SELECTION_FOREGROUND = new ColorUIResource(50, 50, 50);
    public static final Color FOCUS_BORDER_COLOR = new ColorUIResource(51, 173, 213);

    public static final Color CONTROL_BORDER_COLOR = new ColorUIResource(181, 181, 181);
    public static final Color CONTROL_DISABLED_BORDER_COLOR = new ColorUIResource(218, 218, 218);
    public static final Color CONTROL_SELECTED_BORDER_COLOR = new ColorUIResource(160, 160, 160);
    public static final Color CONTROL_DISABLED_INTERNAL_BORDER_HI_COLOR = new ColorUIResource(247, 247, 247);
    public static final Color CONTROL_DISABLED_INTERNAL_BORDER_LOW_COLOR = new ColorUIResource(236, 236, 236);
    public static final Color CONTROL_INTERNAL_BORDER_HI_COLOR = new ColorUIResource(254, 254, 254);
    public static final Color CONTROL_INTERNAL_BORDER_LOW_COLOR = new ColorUIResource(226, 226, 226);
    public static final Insets CONTROL_BORDER_INSETS = new InsetsUIResource(4, 8, 4, 8);

    public static final Color CONTROL_BACKGROUND_HI = new ColorUIResource(240, 240, 240);
    public static final Color CONTROL_BACKGROUND_LO = new ColorUIResource(215, 215, 215);
    public static final Color CONTROL_DISABLED_BACKGROUND_HI = new ColorUIResource(241, 241, 241);
    public static final Color CONTROL_DISABLED_BACKGROUND_LO = new ColorUIResource(233, 233, 233);
    public static final Color CONTROL_HOVER_BACKGROUND_HI = new ColorUIResource(250, 250, 250);
    public static final Color CONTROL_HOVER_BACKGROUND_LO = new ColorUIResource(226, 226, 226);
    public static final Color CONTROL_ARMED_BACKGROUND_HI = new ColorUIResource(228, 228, 228);
    public static final Color CONTROL_ARMED_BACKGROUND_LO = new ColorUIResource(207, 207, 207);

    public static final Color CONTROL_SELECTED_BACKGROUND_HI = new ColorUIResource(186, 186, 186);
    public static final Color CONTROL_SELECTED_BACKGROUND_LO = new ColorUIResource(210, 210, 210);
    public static final Color CONTROL_DISABLED_SELECTED_BACKGROUND_HI = new ColorUIResource(220, 220, 220);
    public static final Color CONTROL_DISABLED_SELECTED_BACKGROUND_LO = new ColorUIResource(230, 230, 230);
    public static final Color CONTROL_SELECTED_HOVER_BACKGROUND_HI = new ColorUIResource(195, 195, 195);
    public static final Color CONTROL_SELECTED_HOVER_BACKGROUND_LO = new ColorUIResource(220, 220, 220);
    public static final Color CONTROL_SELECTED_ARMED_BACKGROUND_HI = new ColorUIResource(175, 175, 175);
    public static final Color CONTROL_SELECTED_ARMED_BACKGROUND_LO = new ColorUIResource(197, 197, 197);

    public static final Border SCROLL_PANE_BORDER =
            new BorderUIResource.LineBorderUIResource(CONTROL_BORDER_COLOR, 1);

    public static final Color COMBOBOX_POPUP_BORDER_COLOR = new ColorUIResource(182, 182, 182);

    public static final Color TOOLBAR_BORDER_COLOR = new ColorUIResource(181, 181, 181);
    public static final Color TOOLBAR_BACKGROUND_HI_COLOR = new ColorUIResource(248, 248, 248);
    public static final Color TOOLBAR_BACKGROUND_LO_COLOR = new ColorUIResource(231, 231, 231);

    public static final Color CONTEXT_MENU_BORDER_COLOR = new ColorUIResource(195, 195, 195);
    public static final Border CONTEXT_MENU_BORDER = new BorderUIResource(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(CONTEXT_MENU_BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(2, 0, 2, 0))
    );
    public static final Color CONTEXT_MENU_BACKGROUND = new ColorUIResource(255, 255, 255);

    public static final Color TABS_BACKGROUND_HI = new ColorUIResource(227, 227, 227);
    public static final Color TABS_BACKGROUND_LOW = new ColorUIResource(208, 208, 208);

    public static final Color TABLE_GRID_COLOR = new ColorUIResource(160, 160, 160);
    // Presque blanc mais pas exactement (254, permet d'eviter que awt fasses des trucs bizarre quand il paint
    // plusieurs fois la même cellule dans le cas des spans.
    public static final Color TABLE_CELL_BACKGROUND = new ColorUIResource(254, 254, 254);
    public static final Color TABLE_CELL_ALTERNATE_BACKGROUND = new ColorUIResource(244, 244, 244);
    public static final Border TABLE_CELL_FOCUS_BORDER =
            new BorderUIResource.LineBorderUIResource(FOCUS_BORDER_COLOR, 1);

    public static final Color TOOLTIP_BORDER_COLOR_HI = new ColorUIResource(122, 122, 122);
    public static final Color TOOLTIP_BORDER_COLOR_LO = new ColorUIResource(107, 107, 107);
    public static final Color TOOLTIP_BACKGROUND_LO = new ColorUIResource(55, 55, 55);
    public static final Color TOOLTIP_BACKGROUND_HI = new ColorUIResource(66, 66, 66);

    public static final Color LIST_BACKGROUND = new ColorUIResource(255, 255, 255);

    public static final Color TEXT_BACKGROUND = new ColorUIResource(255, 255, 255);
}
