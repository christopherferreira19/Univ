/*
* Copyright (c) 2002 and later by MH Software-Entwicklung. All Rights Reserved.
*  
* JTattoo is multiple licensed. If your are an open source developer you can use
* it under the terms and conditions of the GNU General Public License version 2.0
* or later as published by the Free Software Foundation.
*  
* see: gpl-2.0.txt
* 
* If you pay for a license you will become a registered user who could use the
* software under the terms and conditions of the GNU Lesser General Public License
* version 2.0 or later with classpath exception as published by the Free Software
* Foundation.
* 
* see: lgpl-2.0.txt
* see: classpath-exception.txt
* 
* Registered users could also use JTattoo under the terms and conditions of the 
* Apache License, Version 2.0 as published by the Apache Software Foundation.
*  
* see: APACHE-LICENSE-2.0.txt
*/

package gauffre.laf.ui;

import gauffre.laf.Theme;
import gauffre.laf.control.Control;
import gauffre.laf.icons.ArrowIcons;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class NcComboBoxUI extends BasicComboBoxUI implements ControlUI {

    private final Control control;

    public static ComponentUI createUI(JComponent c) {
        return new NcComboBoxUI();
    }

    public NcComboBoxUI() {
        this.control = new Control(false);
    }

    @Override
    public Control getControl() {
        return control;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        control.bindTo(c);
    }

    @Override
    public JButton createArrowButton() {
        JButton button = new JButton(ArrowIcons.south()) {
            @Override
            public void repaint() {
                NcComboBoxUI.this.comboBox.repaint();
            }
        };
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setRolloverEnabled(true);
        control.bindTo(button);
        return button;
    }

    @Override
    public void configureArrowButton() {
        super.configureArrowButton();
        arrowButton.setFocusable(false);
    }

    @Override
    public void paint(final Graphics g, final JComponent c) {
        arrowButton.setIcon(control.isEnabled() ? ArrowIcons.south() : null);

        boolean hasFocus = comboBox.hasFocus();
        boolean popupVisible = isPopupVisible(comboBox);
        control.setEnabled(c.isEnabled());
        control.setRoundedBottomLeft(!popupVisible);
        control.setRoundedBottomRight(!popupVisible);
        Rectangle bounds = rectangleForCurrentValue();
        control.paint(g, 0, 0, comboBox.getWidth(), comboBox.getHeight());
        if (comboBox.isEditable()) {
            return;
        }

        ListCellRenderer renderer = comboBox.getRenderer();
        JComponent rendered = (JComponent) renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(),
                -1, (hasFocus && !isPopupVisible(comboBox)), false);
        rendered.setFont(comboBox.getFont());

        Color oldBackground = rendered.getBackground();
        Color oldForeground = rendered.getForeground();
        boolean oldOpaque = rendered.isOpaque();
        rendered.setBackground(null);
        rendered.setForeground(control.isEnabled() ? Theme.FOREGROUND : Theme.DISABLED_FOREGROUND);
        rendered.setOpaque(false);
        currentValuePane.paintComponent(g, rendered, comboBox, bounds.x, bounds.y, bounds.width, bounds.height,
                rendered instanceof JPanel);
        rendered.setBackground(oldBackground);
        rendered.setForeground(oldForeground);
        rendered.setOpaque(oldOpaque);
    }
}
