package grid;

public class GridModel {

    private final int n;
    private final boolean[] values;

    public GridModel(int n) {
        this.n = n;
        this.values = new boolean[n * n];
    }

    public int getN() {
        return n;
    }

    private int indexOf(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= n) {
            throw new IndexOutOfBoundsException();
        }

        return i * n + j;
    }

    public boolean get(int i, int j) {
        return values[indexOf(i, j)];
    }

    public void toggle(int i, int j) {
        int index = indexOf(i, j);
        values[index] = !values[index];
    }
}
