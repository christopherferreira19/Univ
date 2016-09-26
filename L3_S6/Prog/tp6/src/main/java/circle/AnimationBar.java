package circle;

import javax.swing.*;

public class AnimationBar extends JProgressBar implements ModelObserver {

    private final Model model;

    public AnimationBar(Model model) {
        this.model = model;
        model.register(this);

        setStringPainted(true);
        changed();
    }

    @Override
    public void changed() {
        getModel().setMaximum(model.getStepsCount());
        getModel().setValue(model.getStep());
        setString(model.getX() + ", " + model.getY());
    }
}
