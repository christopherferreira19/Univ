package beamazed;

public class Path {

    final PathCell first;
    PathCell last;

    public Path(MazeNode node) {
        this.first = new PathCell(node);
        this.last = first;
    }

    public Path(PathCell first, PathCell last) {
        this.first = first;
        this.last = last;
    }

    public Path join(MazeNode doorNode, Path other) {
        last.doorNode = doorNode;
        last.next = other.first;
        this.last = other.last;
        return this;
    }
}
