package beamazed;

import java.util.Random;

public class MazeNode {

    public static MazeNode generate(Random rand, int width, int height) {
        return generate(rand, null, 0, 0, width, height);
    }

private static MazeNode generate(Random rand,
        MazeNode parent, int x, int y, int w, int h) {
    MazeNode node = new MazeNode(parent, x, y, w, h);

    if (w < 2 || h < 2) {
        return node;
    }

    if (w > h || w == h && rand.nextBoolean()) {
        node.division = Division.VERTICAL;
        int wallIdx = rand.nextInt(w - 1) + 1;
        int doorPos = rand.nextInt(h);
        node.doorX = x + wallIdx;
        node.doorY = y + doorPos;

        node.topLeft = generate(rand, node, x, y, wallIdx, h);
        node.botRight = generate(rand, node, x + wallIdx, y, w - wallIdx, h);

        node.topLeftDoorLeaf = node.topLeft.findLeafContaining(
                node.doorX - 1, node.doorY);
        node.botRightDoorLeaf = node.botRight.findLeafContaining(
                node.doorX, node.doorY);
    }
    else {
        node.division = Division.HORIZONTAL;
        int wallIdx = rand.nextInt(h - 1) + 1;
        int doorPos = rand.nextInt(w);
        node.doorX = x + doorPos;
        node.doorY = y + wallIdx;

        node.topLeft = generate(rand, node, x, y, w, wallIdx);
        node.botRight = generate(rand, node, x, y + wallIdx, w, h - wallIdx);

        node.topLeftDoorLeaf = node.topLeft.findLeafContaining(
                node.doorX, node.doorY - 1);
        node.botRightDoorLeaf = node.botRight.findLeafContaining(
                node.doorX, node.doorY);
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
