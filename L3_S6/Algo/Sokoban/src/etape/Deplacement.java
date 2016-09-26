package etape;

import terrain.Case;
import terrain.Coords;
import terrain.Terrain;

public class Deplacement implements Etape {

    private final Case c;
    private final Coords depuis;
    private final Coords vers;

    public static Deplacement pousseur(Coords depuis, Coords vers) {
        return new Deplacement(Case.POUSSEUR, depuis, vers);
    }

    public static Deplacement sac(Coords depuis, Coords vers) {
        return new Deplacement(Case.SAC, depuis, vers);
    }

    private Deplacement(Case c, Coords depuis, Coords vers) {
        this.c = c;
        this.depuis = depuis;
        this.vers = vers;
    }

    @Override
    public void appliquer(Terrain terrain, Stats stats) {
        terrain.deplacer(c, depuis, vers);

        if (c.contient(Case.SAC))      stats.addSacDeplacements(1);
        if (c.contient(Case.POUSSEUR)) stats.addPousseurDeplacements(1);
    }
}
