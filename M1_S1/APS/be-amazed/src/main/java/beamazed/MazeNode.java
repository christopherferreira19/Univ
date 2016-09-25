package beamazed;

import java.util.Random;

public class MazeNode {

    public static MazeNode generate(Random random, int width, int height) {
        return doGenerate(random, null, 0, 0, width, height);
    }

    private static MazeNode doGenerate(Random random,
                MazeNode parent, int x, int y, int width, int height) {
        MazeNode node = new MazeNode(parent, x, y, width, height);

        if (width < 2 || height < 2) {
            return node;
        }

        if (width > height || width == height && random.nextBoolean()) {
            node.division = Division.VERTICAL;
            int divisionIndex = random.nextInt(width - 1) + 1;
            int doorPosition = random.nextInt(height);
            node.doorX = x + divisionIndex;
            node.doorY = y + doorPosition;

            node.topLeft = doGenerate(random, node, x, y, divisionIndex, height);
            node.botRight = doGenerate(random, node, x + divisionIndex, y, width - divisionIndex, height);

            node.topLeftDoorLeaf = node.topLeft.findLeafContaining(node.doorX - 1, node.doorY);
            node.botRightDoorLeaf = node.botRight.findLeafContaining(node.doorX, node.doorY);
        }
        else {
            node.division = Division.HORIZONTAL;
            int divisionIndex = random.nextInt(height - 1) + 1;
            int doorPosition = random.nextInt(width);
            node.doorX = x + doorPosition;
            node.doorY = y + divisionIndex;

            node.topLeft = doGenerate(random, node, x, y, width, divisionIndex);
            node.botRight = doGenerate(random, node, x, y + divisionIndex, width, height - divisionIndex);

            node.topLeftDoorLeaf = node.topLeft.findLeafContaining(node.doorX, node.doorY - 1);
            node.botRightDoorLeaf = node.botRight.findLeafContaining(node.doorX, node.doorY);
        }

        return node;
    }

    final MazeNode parent;

    final int x;
    final int y;
    final int width;
    final int height;

    Division division = Division.NONE;
    int doorX = -1;
    int doorY = -1;

    MazeNode topLeft = null;
    MazeNode botRight = null;

    MazeNode topLeftDoorLeaf = null;
    MazeNode botRightDoorLeaf = null;

    private MazeNode(MazeNode parent, int x, int y, int width, int height) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.division = Division.NONE;
        this.doorX = -1;
        this.doorY = -1;
        this.topLeft = null;
        this.botRight = null;
        this.topLeftDoorLeaf = null;
        this.botRightDoorLeaf = null;
    }

    boolean contains(int x, int y) {
        return this.x <= x && x < this.x + this.width
                && this.y <= y && y < this.y + this.height;
    }

    MazeNode findLeafContaining(int x, int y) {
        if (division == Division.NONE) {
            return this;
        }

        return topLeft.contains(x, y)
                ? topLeft.findLeafContaining(x, y)
                : botRight.findLeafContaining(x, y);
    }
}
