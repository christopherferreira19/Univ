package gauffre.moteur;

class GauffreImpl implements Gauffre {

    private int[] lineSizes;
    private int[] columnSizes;

    GauffreImpl(int height, int width) {
        this(new int[height], new int[width]);
        reset();
    }

    private GauffreImpl(int[] lineSizes, int[] columnSizes) {
        this.lineSizes = lineSizes;
        this.columnSizes = columnSizes;
    }

    void reset() {
        for (int l = 0; l < lineSizes.length; l++) {
            lineSizes[l] = columnSizes.length;
        }

        for (int c = 0; c < columnSizes.length; c++) {
            columnSizes[c] = lineSizes.length;
        }
    }

    public int getWidth() {
        return columnSizes.length;
    }

    public int getHeight() {
        return lineSizes.length;
    }

    @Override
    public int getLineSize(int l) {
        return lineSizes[l];
    }

    @Override
    public int getColumnSize(int c) {
        return columnSizes[c];
    }

    public boolean getState(int l, int c) {
        return l >= 0 && c >= 0 && lineSizes[l] > c;
    }

    public Configuration getConfig(){
        return new Configuration( lineSizes );
    }

    public void removeFrom(int lFrom, int cFrom) {
        for (int l = lFrom; l < lineSizes.length; l++) {
            lineSizes[l] = Math.min(lineSizes[l], cFrom);
        }

        for (int c = cFrom; c < columnSizes.length; c++) {
            columnSizes[c] = Math.min(columnSizes[c], lFrom);
        }
    }

    @Override
    public Gauffre cloneGauffre() {
        int[] newLineSizes = new int[lineSizes.length];
        System.arraycopy(lineSizes, 0, newLineSizes, 0, lineSizes.length);
        int[] newColumnSizes = new int[columnSizes.length];
        System.arraycopy(columnSizes, 0, newColumnSizes, 0, columnSizes.length);
        return new GauffreImpl(newLineSizes, newColumnSizes);
    }
}
