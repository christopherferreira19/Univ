package q3;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DessinArea extends JComponent {

    private static final Paint TRANSPARENT = new Color(0, 0, 0, 0);

    private final List<BufferedImage> bufs;
    private int bufIndex;

    private boolean copyState;
    private boolean pasteState;
    private Point copyFrom;
    private Point copyTo;
    private BufferedImage clipboard;

    private Action undoAction;
    private Action redoAction;
    private Action pasteAction;

    public DessinArea() {
        this.bufs = new ArrayList<>();
        this.bufIndex = 0;
        this.copyState = false;
        this.copyFrom = null;
        this.copyTo = null;
        this.pasteState = false;
        this.clipboard = null;
    }

    public void initActions(Action undoAction, Action redoAction, Action pasteAction) {
        this.undoAction = undoAction;
        this.redoAction = redoAction;
        updateUndoRedoActions();
        this.pasteAction = pasteAction;
        updatePasteAction();
    }

    private void updatePasteAction() {
        pasteAction.setEnabled(clipboard != null);
    }

    private BufferedImage buf() {
        if (bufs.size() <= bufIndex) {
            BufferedImage newBuf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            bufs.add(newBuf);
        }
        else if (bufs.get(bufIndex).getWidth() < getWidth()
                || bufs.get(bufIndex).getHeight() < getHeight()) {
            BufferedImage newBuf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D newG2 = newBuf.createGraphics();
            newG2.drawImage(bufs.get(bufIndex), 0, 0, null);
            bufs.set(bufIndex, newBuf);
        }

        return bufs.get(bufIndex);
    }

    public BufferedImage nextBuf() {
        BufferedImage buf = buf();

        BufferedImage newBuf = new BufferedImage(buf.getWidth(), buf.getHeight(), buf.getType());
        Graphics g = newBuf.getGraphics();
        g.drawImage(buf, 0, 0, null);

        bufIndex++;
        while (bufIndex < bufs.size()) {
            bufs.remove(bufIndex);
        }
        bufs.add(newBuf);

        updateUndoRedoActions();
        return newBuf;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.drawImage(buf(), 0, 0, null);

        if (copyFrom != null) {
            g2.setPaint(Color.RED);
            int x = Math.min(copyFrom.x, copyTo.x);
            int y = Math.min(copyFrom.y, copyTo.y);
            int w = Math.abs(copyFrom.x - copyTo.x);
            int h = Math.abs(copyFrom.y - copyTo.y);
            g2.drawRect(x, y, w, h);
        }
    }

    public void draw(Point from, Point to) {
        BufferedImage buf = buf();
        Graphics2D g2 = buf.createGraphics();
        g2.setPaint(Color.black);
        g2.drawLine(from.x, from.y, to.x, to.y);
        repaint();
    }

    public void erase(Point around) {
        BufferedImage buf = buf();
        Graphics2D g2 = buf.createGraphics();
        g2.setPaint(TRANSPARENT);
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(around.x - 5, around.y - 5, 10, 10);
        repaint();
    }

    public void clear() {
        this.bufs.clear();
        this.bufIndex = 0;
        repaint();
    }

    public void save(File dest) throws IOException {
        ImageIO.write(buf(), "png", dest);
    }

    public void load(File src) throws IOException {
        this.bufs.clear();
        this.bufIndex = 0;
        BufferedImage buf = ImageIO.read(src);
        bufs.add(buf);
        updateUndoRedoActions();
        repaint();
    }

    private void updateUndoRedoActions() {
        undoAction.setEnabled(isUndoAvailable());
        redoAction.setEnabled(isRedoAvailable());
    }

    public boolean isUndoAvailable() {
        return bufIndex > 0;
    }

    public void undo() {
        if (!isUndoAvailable()) {
            throw new IllegalStateException();
        }

        bufIndex--;
        updateUndoRedoActions();
        repaint();
    }

    public boolean isRedoAvailable() {
        return bufIndex < bufs.size() - 1;
    }

    public void redo() {
        if (!isRedoAvailable()) {
            throw new IllegalStateException();
        }

        bufIndex++;
        updateUndoRedoActions();
        repaint();
    }

    public boolean isCopyState() {
        return copyState;
    }

    public void setCopyState() {
        this.copyState = true;
    }

    public void startCopySelect(Point point) {
        copyFrom = point;
        copyTo = point;
        repaint();
    }

    public void updateCopySelect(Point point) {
        copyTo = point;
        repaint();
    }

    public void stopCopySelect(Point copyTo) {
        int x = Math.min(copyFrom.x, copyTo.x);
        int y = Math.min(copyFrom.y, copyTo.y);
        int w = Math.abs(copyFrom.x - copyTo.x);
        int h = Math.abs(copyFrom.y - copyTo.y);
        clipboard = buf().getSubimage(x, y, w, h);
        copyState = false;
        copyFrom = null;
        copyTo = null;
        updatePasteAction();
        repaint();
    }

    public boolean isPasteState() {
        return pasteState;
    }

    public void setPasteState() {
        pasteState = true;
    }

    public void paste(Point point) {
        BufferedImage buf = nextBuf();
        Graphics2D g2 = buf.createGraphics();
        g2.drawImage(clipboard, point.x, point.y, null);
        pasteState = false;
        updateUndoRedoActions();
        repaint();
    }
}
