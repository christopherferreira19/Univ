package graph;

import terrain.Coords;
import terrain.Dir;
import terrain.Terrain;

class SacDataTable {

    public final SacData[][][] table;

    public SacDataTable(Terrain terrain) {
        this.table = new SacData[terrain.hauteur()][terrain.largeur()][Dir.values().length];
    }

    public SacData get(Coords coords, Dir dir) {
        return table[coords.ligne][coords.colonne][dir.ordinal()];
    }

    public SacData get(SacData sacData) {
        return get(sacData.coords, sacData.dir);
    }

    public void set(Coords coords, Dir dir, SacData value) {
        table[coords.ligne][coords.colonne][dir.ordinal()] = value;
    }

    public void set(SacData sacData, SacData value) {
        set(sacData.coords, sacData.dir, value);
    }
}
