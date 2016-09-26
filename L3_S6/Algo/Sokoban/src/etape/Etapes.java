package etape;

import terrain.Coords;
import terrain.Terrain;

import java.util.Arrays;
import java.util.List;

public class Etapes implements Etape {

    private final List<Etape> list;

    public static Etapes poussement(Coords pousseurPos, Coords sacPos, Coords sacDest) {
        return of(
                Deplacement.sac(sacPos, sacDest),
                Deplacement.pousseur(pousseurPos, sacPos)
        );
    }

    public static Etapes of(Etape... ary) {
        return of(Arrays.asList(ary));
    }

    public static Etapes of(List<Etape> list) {
        return new Etapes(list);
    }

    private Etapes(List<Etape> list) {
        this.list = list;
    }

    @Override
    public void appliquer(Terrain terrain, Stats stats) {
        for (Etape etape : list) {
            if (etape != null) {
                etape.appliquer(terrain, stats);
            }
        }
    }
}
