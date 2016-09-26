package graph;

import fileattente.FileAttente;
import terrain.Coords;

public enum SolutionMode {

    MINIMUM_SAC {
        @Override
        public FileAttente<SacData> creerFileAttente(int capacite, Coords objectif) {
            return FileAttente.fifo(capacite);
        }
    },

    MINIMUM_POUSSEUR {
        @Override
        public FileAttente<SacData> creerFileAttente(int capacite, Coords objectif) {
            return FileAttente.parPriorite(capacite, SacData.prioritisationParDistance());
        }
    },

    MINIMUM_POUSSEUR_A_STAR {
        @Override
        public FileAttente<SacData> creerFileAttente(int capacite, Coords objectif) {
            return FileAttente.parPriorite(capacite, SacData.prioritisationVersObjectif(objectif));
        }
    };

    public abstract FileAttente<SacData> creerFileAttente(int capacite, Coords objectif);
}
