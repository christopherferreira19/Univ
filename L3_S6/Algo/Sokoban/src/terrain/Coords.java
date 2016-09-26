package terrain;

public class Coords {

    public final int ligne;
    public final int colonne;

    public Coords(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public Coords voisin(Dir dir) {
        return new Coords(ligne + dir.dl, colonne + dir.dc);
    }

    @Override
    public int hashCode() {
        int result = ligne;
        result = 31 * result + colonne;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coords)) {
            return false;
        }

        Coords coords = (Coords) o;
        return ligne == coords.ligne && colonne == coords.colonne;
    }

    @Override
    public String toString() {
        return "Coords<" + ligne + ", " + colonne + ">";
    }
}
