package gauffre.ia;

import gauffre.Coords;
import gauffre.moteur.Gauffre;
import gauffre.moteur.Moteur;

import java.util.concurrent.atomic.AtomicBoolean;

class IAFacile implements IA {

    private final Moteur m;
    private final AtomicBoolean cancelled;

    IAFacile(Moteur m, AtomicBoolean cancelled) {
        this.m = m;
        this.cancelled = cancelled;
    }

    public Coords prochainCoup() {
        Gauffre g = m.getGauffre();
        int w = g.getWidth();
        int h = g.getHeight();

        while (true) {
            if (cancelled.get()) {
                return null;
            }

            int l = m.nextInt(h);
            int c = m.nextInt(w);
            if ((l != 0 || c != 0) && g.getState(l, c))
                return new Coords(l,c);
        }
    }
}
