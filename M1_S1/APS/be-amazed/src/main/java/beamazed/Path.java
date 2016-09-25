package beamazed;

public class Path {

    final MazeNode node;

    MazeNode doorNode;
    Path next;

    public Path(MazeNode node) {
        this.node = node;

        this.doorNode = null;
        this.next = null;
    }

    public Path join(MazeNode doorNode, Path other) {
        Path pathIt = this;
        while (pathIt.next != null) {
            pathIt = pathIt.next;
        }

        pathIt.doorNode = doorNode;
        pathIt.next = other;
        return this;
    }
}
