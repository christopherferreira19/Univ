package holedrilling;

import holedrilling.prim.MST;
import holedrilling.prim.Prim;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class View extends JComponent {

    private final PCB pcb;
    private final MST mst;

    public View(PCB pcb, MST mst) {
        this.pcb = pcb;
        this.mst = mst;

        Dimension dimension = new Dimension(pcb.width, pcb.height);
        setSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, pcb.width, pcb.height);

        g.setColor(Color.YELLOW);
        drawMSTEdge(g, pcb.start);
        g.setColor(Color.BLUE);
        for (Point from : pcb.holes) {
            drawMSTEdge(g, from);
        }

        g.setColor(Color.ORANGE);
        drawPoint(g, pcb.start);

        g.setColor(Color.RED);
        for (Point hole : pcb.holes) {
            drawPoint(g, hole);
        }
    }

    private void drawMSTEdge(Graphics g, Point from) {
        for (Point to : mst.children(from)) {
            g.drawLine((int) from.x, (int) from.y, (int) to.x, (int) to.y);
        }
    }

    private void drawPoint(Graphics g, Point hole) {
        g.drawLine((int) hole.x - 1, (int) hole.y - 1, (int) hole.x + 1, (int) hole.y + 1);
        g.drawLine((int) hole.x - 1, (int) hole.y + 1, (int) hole.x + 1, (int) hole.y - 1);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze View");

        PCB pcb = PCB.random(1000, 800, 1000);
        MST mst = Prim.run(pcb);
        frame.add(new JScrollPane(new View(pcb, mst)));

        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private static long elapsedMs(long startTime) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }
}
