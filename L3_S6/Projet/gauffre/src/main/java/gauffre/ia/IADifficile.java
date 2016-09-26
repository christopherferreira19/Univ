package gauffre.ia;

import gauffre.Coords;
import gauffre.moteur.Configuration;
import gauffre.moteur.Gauffre;
import gauffre.moteur.Moteur;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


class IADifficile implements IA {

    private final Moteur m;
    private final AtomicBoolean cancelled;

    IADifficile(Moteur m, AtomicBoolean cancelled) {
        this.m = m;
        this.cancelled = cancelled;
    }

    @Override
    public Coords prochainCoup() {
        return prochainCoup(7);
    }

     Coords prochainCoup( int profondeur ){
        HashMap<Configuration, ConfigInfos> hashJoueurA = new HashMap<>();
        HashMap<Configuration, ConfigInfos> hashJoueurB = new HashMap<>();

        Gauffre g = m.getGauffre();

        Coords solution = EvaluationJoueur(g, profondeur,
                hashJoueurA, hashJoueurA, hashJoueurB, true);

        if (cancelled.get() || solution != null) {
            return solution;
        }

        // Si on a pas trouve de solution, alors on clique au hasard
        Random r = new Random();
        int l = r.nextInt(g.getHeight());
        int c = -1;
        while ( c == -1 ){
            if( g.getLineSize( l ) > 0 ) {
                c = r.nextInt(g.getLineSize(l));
                if( l == 0 && c == 0 )
                    c = -1;
            }
            else
                l = r.nextInt(g.getHeight());
        }

        return new Coords(l, c);
    }

    // Renvoie un clic gagnant pour A ou (0,0) sinon
    Coords EvaluationJoueur( Gauffre g, int profondeur,
             HashMap<Configuration, ConfigInfos> map,
             HashMap<Configuration, ConfigInfos> map1,
             HashMap<Configuration, ConfigInfos> map2,
             boolean value) {
        if (cancelled.get()) {
            return null;
        }

        // Cas de base, le joueur perd car oblige de croquer le poison
        if( g.getLineSize(0) == 1 && g.getColumnSize(0) == 1 )
            return null;

        // Sinon on regarde si la config a deja ete calculee
        Configuration config = g.getConfig();
        ConfigInfos info = map.get(config);
        if (info != null) {
            return info.value == value ? info.sol : null;
        }

        if( profondeur == 0 )
            return null;

        // Sinon on calcule son resultat
        // Pour toutes les configurations qui derivent
        for (int l = 0; l < g.getHeight(); l++) {
            for (int c = g.getLineSize(l) - 1; c >= 0; c--) {
                if (l == 0 && c == 0) {
                    // On ne mord pas la case poison
                    continue;
                }

                Gauffre next = g.cloneGauffre();
                next.removeFrom(l,c);
                Coords solution = EvaluationJoueur(next , profondeur - 1,
                        map == map1 ? map2 : map1,
                        map1, map2,
                        !value);
                if (cancelled.get()) {
                    return null;
                }

                if (solution == null) {
                    ConfigInfos newInfo = new ConfigInfos(value, new Coords(l, c));
                    map.put(config, newInfo);
                    return newInfo.sol;
                }
            }
        }

        // Si jamais on a pas trouve, on ajoute la config comme perdante.
        map.put(config, new ConfigInfos(!value, null));
        return null;
    }

    private class ConfigInfos {
        private boolean value;
        private Coords sol;

        ConfigInfos(boolean value, Coords sol) {
            this.value = value;
            this.sol = sol;
        }
    }
}
