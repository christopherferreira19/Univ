package holedrilling;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class View extends JComponent {

    private final PCB pcb;

    public View(PCB pcb) {
        this.pcb = pcb;

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

        g.setColor(Color.RED);
        for (Point hole : pcb.holes) {
            g.drawLine((int) hole.x - 1, (int) hole.y - 1, (int) hole.x + 1, (int) hole.y + 1);
            g.drawLine((int) hole.x - 1, (int) hole.y + 1, (int) hole.x + 1, (int) hole.y - 1);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze View");

        PCB pcb = PCB.random(600, 600, 120);
        frame.add(new JScrollPane(new View(pcb)));

        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private static long elapsedMs(long startTime) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }
}
