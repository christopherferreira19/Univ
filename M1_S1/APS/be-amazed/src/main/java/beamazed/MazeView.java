package beamazed;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MazeView extends JComponent {

    private static final int CELL_SIZE = 20;

    private final MazeNode maze;

    private final int sx;
    private final int sy;
    private final int dx;
    private final int dy;

    private final Path path;

    public MazeView(MazeNode maze, int sx, int sy, int dx, int dy, Path path) {
        this.maze = maze;
        this.sx = sx;
        this.sy = sy;
        this.dx = dx;
        this.dy = dy;
        this.path = path;

        setSize((maze.width + 2) * CELL_SIZE, (maze.height + 2) * CELL_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawRect(CELL_SIZE, CELL_SIZE, maze.width * CELL_SIZE, maze.height * CELL_SIZE);

        drawWalls(g, maze);

        boolean even = true;
        Path pathIt = path;
        while (pathIt != null) {
            even = !even;
            g.setColor(even ? Color.GREEN : Color.CYAN);
            g.fillRect(
                    CELL_SIZE + pathIt.node.x * CELL_SIZE + 1,
                    CELL_SIZE + pathIt.node.y * CELL_SIZE + 1,
                    pathIt.node.width * CELL_SIZE - 1,
                    pathIt.node.height * CELL_SIZE - 1);

            MazeNode door = pathIt.doorNode;
            g.setColor(Color.YELLOW);
            if (door != null) {

                if (door.division == Division.VERTICAL) {
                    g.drawLine(
                            CELL_SIZE + door.doorX * CELL_SIZE,
                            CELL_SIZE + door.doorY * CELL_SIZE + 1,
                            CELL_SIZE + door.doorX * CELL_SIZE,
                            CELL_SIZE + (door.doorY + 1) * CELL_SIZE - 1);
                }
                else {
                    g.drawLine(
                            CELL_SIZE + door.doorX * CELL_SIZE + 1,
                            CELL_SIZE + door.doorY * CELL_SIZE,
                            CELL_SIZE + (door.doorX + 1) * CELL_SIZE - 1,
                            CELL_SIZE + door.doorY * CELL_SIZE);
                }
            }

            pathIt = pathIt.next;
        }

        g.setColor(Color.ORANGE);
        g.fillRect(
                CELL_SIZE + sx * CELL_SIZE + 4,
                CELL_SIZE + sy * CELL_SIZE + 4,
                CELL_SIZE - 8, CELL_SIZE - 8);
        g.setColor(Color.RED);
        g.fillRect(
                CELL_SIZE + dx * CELL_SIZE + 4,
                CELL_SIZE + dy * CELL_SIZE + 4,
                CELL_SIZE - 8, CELL_SIZE - 8);
    }

    private void drawWalls(Graphics g, MazeNode node) {
        switch (node.division) {
            case NONE:
                return;
            case HORIZONTAL:
                drawLine(g, node.x, node.doorY, node.doorX, node.doorY);
                drawLine(g, node.doorX + 1, node.doorY, node.x + node.width, node.doorY);
                break;
            case VERTICAL:
                drawLine(g, node.doorX, node.y, node.doorX, node.doorY);
                drawLine(g, node.doorX, node.doorY + 1, node.doorX, node.y + node.height);
                break;
        }

        drawWalls(g, node.topLeft);
        drawWalls(g, node.botRight);
    }

    private void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine(
                CELL_SIZE + x1 * CELL_SIZE,
                CELL_SIZE + y1 * CELL_SIZE,
                CELL_SIZE + x2 * CELL_SIZE,
                CELL_SIZE + y2 * CELL_SIZE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze View");

        Random random = new Random();
        long startTime = System.nanoTime();
        MazeNode maze = MazeNode.generate(random, 30, 30);
        System.out.println("Gen : " + elapsedMs(startTime) + "ms");

        startTime = System.nanoTime();
        int sx = random.nextInt(maze.width);
        int sy = random.nextInt(maze.height);
        int dx = maze.width - 1;//random.nextInt(maze.width);
        int dy = maze.height - 1;//random.nextInt(maze.height);
        Path path = PathSolver.solve(maze, sx, sy, dx, dy);
        System.out.println("Solve : " + elapsedMs(startTime) + "ms");
        int pathSize = 0;
        Path pathIt = path;
        while (pathIt != null) { pathSize++; pathIt = pathIt.next; }
        System.out.println("Path Size: " + pathSize);

        frame.add(new MazeView(maze, sx, sy, dx, dy, path));

        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private static long elapsedMs(long startTime) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }
}
