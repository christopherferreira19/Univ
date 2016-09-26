package beamazed;

public class PathCell {

    final MazeNode node;

    MazeNode doorNode;
    PathCell next;

    public PathCell(MazeNode node) {
        this.node = node;

        this.doorNode = null;
        this.next = null;
    }
}
