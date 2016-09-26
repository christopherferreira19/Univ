package terrain;

import Dessin.Fenetre;

public interface Visualisation {

    void updateSac();

    void updatePousseur();

    class SacEtPousseur implements Visualisation {
        private final Fenetre fenetre;
        private final TerrainGraphique tg;

        public SacEtPousseur(Fenetre fenetre, TerrainGraphique tg) {
            this.fenetre = fenetre;
            this.tg = tg;
        }

        @Override
        public void updateSac() {
            fenetre.tracer(tg);
        }

        @Override
        public void updatePousseur() {
            fenetre.tracer(tg);
        }
    }

    class Sac implements Visualisation {
        private final Fenetre fenetre;
        private final TerrainGraphique tg;

        public Sac(Fenetre fenetre, TerrainGraphique tg) {
            this.fenetre = fenetre;
            this.tg = tg;
        }

        @Override
        public void updateSac() {
            fenetre.tracer(tg);
        }

        @Override
        public void updatePousseur() {
        }
    }

    class Desactivee implements Visualisation {
        @Override
        public void updateSac() {
        }

        @Override
        public void updatePousseur() {
        }
    }
}
