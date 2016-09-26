package gauffre.ia;

import gauffre.Coords;
import gauffre.moteur.Gauffre;
import gauffre.moteur.Moteur;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

class IANormale implements IA {

    private final Moteur m;
    private final AtomicBoolean cancelled;

    IANormale(Moteur m, AtomicBoolean cancelled) {
        this.m = m;
        this.cancelled = cancelled;
    }

    public Coords prochainCoup(){
        Gauffre g = m.getGauffre();
        int w = g.getWidth();
        int h = g.getHeight();
        Random r = new Random();

        // Choix d'un coup gagnant si possible
        if (g.getColumnSize(1) == 0)
            return new Coords( 1, 0 );
        else if (g.getLineSize(1) == 0)
            return new Coords( 0, 1 );

        // Forcément perdant
        if (g.getLineSize(0) == 2 && g.getColumnSize(0) == 2) {
            boolean rb = r.nextBoolean();
            if (rb) {
                return new Coords(0, 1);
            } else {
                return new Coords(1, 0);
            }
        }

        // Si aucun coup gagnant choix d'un coup aleatoire mais non perdant
        while (true) {
            if (cancelled.get()) {
                return null;
            }

            int l = m.nextInt(h);
            if (g.getLineSize(l) == 0) {
                continue;
            }

            int c = m.nextInt(g.getLineSize(l));
            if (l == 0 && c == 1 || l == 1 && c == 0) // Perdant
                continue;

            return new Coords(l, c);
        }
    }
}
