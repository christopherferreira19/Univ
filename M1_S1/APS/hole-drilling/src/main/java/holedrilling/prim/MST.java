package holedrilling.prim;

import com.google.common.collect.SetMultimap;
import holedrilling.Point;

import java.util.Set;

public class MST {

    private final SetMultimap<Point, Point> map;

    public MST(SetMultimap<Point, Point> map) {
        this.map = map;
    }

    public Set<Point> children(Point parent) {
        return map.get(parent);
    }
}
