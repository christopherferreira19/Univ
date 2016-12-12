package holedrilling.prim;

import com.google.common.collect.ImmutableSetMultimap;
import holedrilling.PCB;
import holedrilling.Point;

public class Prim {

    public static MST run(PCB pcb) {
        PrimQueue queue = new PrimQueue(pcb.holes.size());
        ImmutableSetMultimap.Builder<Point, Point> builder = ImmutableSetMultimap.builder();

        updateQueue(pcb, queue, pcb.start);

        while (!queue.isEmpty()) {
            PrimQueue.Pair pair = queue.extract();
            Point from = pair.from;
            Point to = pcb.holes.get(pair.to);
            builder.put(from, to);
            updateQueue(pcb, queue, to);
        }

        return new MST(builder.build());
    }

    private static void updateQueue(PCB pcb, PrimQueue queue, Point from) {
        for (int i = 0; i < pcb.holes.size(); i++) {
            double distanceSq = Point.distanceSq(from, pcb.holes.get(i));
            queue.update(i, from, distanceSq);
        }
        queue.reorder();
    }
}
