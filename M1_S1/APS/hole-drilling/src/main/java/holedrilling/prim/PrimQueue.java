package holedrilling.prim;

import holedrilling.Point;

import java.util.Arrays;

public class PrimQueue {

    static class Pair {
        final Point from;
        final int to;

        Pair(Point from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    private int size;
    private final int[] heap;
    private final Point[] from;
    private final double[] priorities;

    PrimQueue(int size) {
        this.size = size;
        this.heap = new int[size];
        this.from = new Point[size];
        this.priorities = new double[size];

        for (int i = 0; i < size; i++) {
            heap[i] = i;
        }
        Arrays.fill(from, null);
        Arrays.fill(priorities, Integer.MAX_VALUE);
    }

    boolean isEmpty() {
        return size == 0;
    }

    void update(int node, Point from, double priority) {
        if (priorities[node] > priority) {
            priorities[node] = priority;
            this.from[node] = from;
        }
    }

    private void siftDown(int parent, int node) {
        double priority = priorities[node];

        while (parent * 2 + 1 < size) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;

            int min = right >= size || priorities[heap[left]] < priorities[heap[right]]
                    ? left
                    : right;

            if (priorities[heap[min]] < priority) {
                heap[parent] = heap[min];
                parent = min;
            }
            else {
                break;
            }
        }
        heap[parent] = node;
    }

    void reorder() {
        int lastParent = (size - 2) / 2;
        for (int parent = lastParent; parent >= 0; parent--) {
            siftDown(parent, heap[parent]);
        }
    }

    Pair extract() {
        if (size == 0) {
            throw new RuntimeException("PrimQueue#extract while empty");
        }

        int to = heap[0];
        Point from = this.from[to];
        size--;
        siftDown(0, heap[size]);
        return new Pair(from, to);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        buildString(0, builder, 0);
        return builder.toString();
    }

    private void buildString(int i, StringBuilder builder, int level) {
        if (i >= size) {
            return;
        }

        for (int j = 0; j < level; j++) {
            builder.append("  ");
        }
        builder.append("(");
        builder.append(heap[i]);
        builder.append(", ");
        builder.append(priorities[heap[i]]);
        builder.append(")\n");

        buildString(i*2+1, builder, level + 1);
        buildString(i*2+2, builder, level + 1);
    }

    public static void main(String[] args) {
        double[] priorities = { 5, 3, 10, 4, 1, 8, };
        PrimQueue queue = new PrimQueue(priorities.length);
        for (int i = 0; i < priorities.length; i++) {
            double priority = priorities[i];
            queue.update(i, null, priority);
            System.out.println(i + "<= " + i + ", " + priority);
        }

        System.out.println();
        System.out.println(queue);
        queue.reorder();
        System.out.println(queue);

        for (int i = 0; i < priorities.length; i++) {
            Pair pair = queue.extract();
            System.out.println(i + " => " + pair.to + ", " + priorities[pair.to]);
        }
    }
}
