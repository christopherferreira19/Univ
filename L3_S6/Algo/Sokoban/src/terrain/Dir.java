package terrain;

public enum Dir {

    LEFT(0, -1),

    RIGHT(0, 1),

    UP(-1, 0),

    DOWN(1, 0);

    public final int dl;
    public final int dc;

    Dir(int dl, int dc) {
        this.dl = dl;
        this.dc = dc;
    }

    public Dir oppose() {
        return of(-dl, -dc);
    }

    public static Dir of(int dl, int dc) {
        for (Dir dir : values()) {
            if (dir.dl == dl && dir.dc == dc) {
                return dir;
            }
        }

        return null;
    }
}
