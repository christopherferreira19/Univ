package gauffre.laf.control;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class ButtonModelBinding implements ChangeListener {

    private Control control;

    public ButtonModelBinding(Control control) {
        this.control = control;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        ButtonModel buttonModel = (ButtonModel) e.getSource();
        control.setEnabled(buttonModel.isEnabled());
        control.setArmed(buttonModel.isArmed() || buttonModel.isPressed());
        control.setSelected(buttonModel.isSelected());
        control.setRollover(buttonModel.isRollover());
    }
}
