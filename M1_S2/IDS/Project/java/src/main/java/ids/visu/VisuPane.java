package ids.visu;

import javafx.scene.layout.StackPane;

class VisuPane extends StackPane {

    private final VisuCanvas canvas;

    VisuPane(int size) {
        this.canvas = new VisuCanvas(size);
        getChildren().add(canvas);
    }

    public VisuCanvas getCanvas() {
        return canvas;
    }

    @Override
    protected void layoutChildren() {
        final int top = (int)snappedTopInset();
        final int right = (int)snappedRightInset();
        final int bottom = (int)snappedBottomInset();
        final int left = (int)snappedLeftInset();
        final int w = (int)getWidth() - left - right;
        final int h = (int)getHeight() - top - bottom;

        canvas.setLayoutX(left);
        canvas.setLayoutY(top);
        if (w != canvas.getWidth() || h != canvas.getHeight()) {
            canvas.setWidth(w);
            canvas.setHeight(h);
            canvas.redraw();
        }
    }
}
