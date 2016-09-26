package graph;

import fileattente.Prioritisation;
import terrain.Coords;
import terrain.Dir;

import java.util.List;

class SacData {

    final Coords coords;
    final Dir dir;
    final SacData provenance;
    final int distance;
    final List<Coords> cheminPousseur;

    SacData(Coords coords, Dir dir, SacData provenance, List<Coords> cheminPousseur) {
        this.coords = coords;
        this.dir = dir;
        this.provenance = provenance;
        this.distance = provenance == null
                ? cheminPousseur.size()
                : cheminPousseur.size() + provenance.distance;
        this.cheminPousseur = cheminPousseur;
    }

    static Prioritisation<SacData> prioritisationParDistance() {
        return PrioritisationParDistance.DISTANCE;
    }

    private enum PrioritisationParDistance implements fileattente.Prioritisation<SacData> {
        DISTANCE {
            @Override
            public boolean estPrioritaire(SacData element, SacData parRapport) {
                return element.distance < parRapport.distance;
            }
        },
    }

    static Prioritisation<SacData> prioritisationVersObjectif(final Coords objectif) {
        return new PrioritisationVersObjectif(objectif);
    }

    private static class PrioritisationVersObjectif implements Prioritisation<SacData> {

        private final Coords objectif;

        public PrioritisationVersObjectif(Coords objectif) {
            this.objectif = objectif;
        }

        private int manhattan(Coords c1, Coords c2) {
            return Math.abs(c1.ligne - c2.ligne) + Math.abs(c1.colonne - c2.colonne);
        }

        @Override
        public boolean estPrioritaire(SacData element, SacData parRapport) {
            int elementDH = element.distance + manhattan(element.coords, objectif);
            int parRapportDH = parRapport.distance + manhattan(parRapport.coords, objectif);
            return elementDH < parRapportDH;
        }
    }
}
