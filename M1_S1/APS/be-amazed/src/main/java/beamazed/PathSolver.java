package beamazed;

import java.util.IdentityHashMap;
import java.util.Map;

public class PathSolver {

    public static Path solve(MazeNode maze, int sx, int sy, int dx, int dy) {
        MazeNode startLeaf = maze.findLeafContaining(sx, sy);
        MazeNode destLeaf = maze.findLeafContaining(dx, dy);
        return doSolve(startLeaf, destLeaf);
    }

    private static Path doSolve(MazeNode startLeaf, MazeNode destLeaf) {
        if (startLeaf == destLeaf) {
            return new Path(startLeaf);
        }

        MazeNode door = lowestCommonAncestor(startLeaf, destLeaf);
        if (door.topLeft.contains(startLeaf.x, startLeaf.y)) {
            Path topLeftPath = doSolve(startLeaf, door.topLeftDoorLeaf);
            Path botRightPath = doSolve(door.botRightDoorLeaf, destLeaf);
            return topLeftPath.join(door, botRightPath);
        }
        else {
            Path botRightPath = doSolve(startLeaf, door.botRightDoorLeaf);
            Path topLeftPath = doSolve(door.topLeftDoorLeaf, destLeaf);
            return botRightPath.join(door, topLeftPath);
        }
    }

    private static MazeNode lowestCommonAncestor(MazeNode node1, MazeNode node2) {
        Map<MazeNode, Boolean> map = new IdentityHashMap<>();
        MazeNode it = node1;
        while (it != null) {
            map.put(it, true);
            it = it.parent;
        }

        it = node2;
        while (it != null) {
            if (map.containsKey(it)) {
                return it;
            }

            it = it.parent;
        }

        throw new RuntimeException("The two nodes are not part of the same tree");
    }
}
