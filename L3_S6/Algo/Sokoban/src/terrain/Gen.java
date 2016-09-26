package terrain;

import java.util.Random;

class Gen {

    static void run(Terrain terrain, int seed, double libreRatio) {
        System.out.println("Generating using seed : " + seed);
        Random random = new Random(seed);
        int hauteur = terrain.hauteur();
        int largeur = terrain.largeur();

        Coords pousseur = new Coords(random.nextInt(hauteur), random.nextInt(largeur));
        Coords sac;
        do {
            sac = new Coords(random.nextInt(hauteur - 2) + 1, random.nextInt(largeur - 2) + 1);
        } while (sac.equals(pousseur));

        Coords but;
        do {
            boolean hautBas = random.nextBoolean();
            if (hautBas) {
                boolean haut = random.nextBoolean();
                but = new Coords(haut ? 0 : hauteur - 1, random.nextInt(largeur));
            }
            else {
                boolean gauche = random.nextBoolean();
                but = new Coords(random.nextInt(hauteur), gauche ? 0 : largeur - 1);
            }
        } while (but.equals(sac));

        if (pousseur.equals(but)) {
            terrain.assigner(Case.POUSSEUR_SUR_BUT, pousseur);
        }
        else {
            terrain.assigner(Case.POUSSEUR, pousseur);
            terrain.assigner(Case.BUT, but);
        }
        terrain.assigner(Case.SAC, sac);

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (pousseur.ligne == i && pousseur.colonne == j
                        || sac.ligne == i && sac.colonne == j
                        || but.ligne == i && but.colonne == j) {
                    break;
                }

                if (random.nextDouble() > libreRatio) {
                    terrain.assigner(Case.OBSTACLE, i, j);
                }
            }
        }
    }
}
