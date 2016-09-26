package gauffre;


import gauffre.ia.JoueurIA;
import gauffre.moteur.MoteurImpl;
import org.junit.Assert;
import org.junit.Test;

public class Battle {
    private static final int NB_PARTIE = 1000;
    private static final int NB_PARTIE_LIGHT = 100;
    private static final int HEIGHT = 5;
    private static final int WIDTH = 6;

    boolean delay = false;

    @Test
     public void battleFacileVsMoyen() {
        long seed = -1;
        int nbPartieGagner[] = new int[] {0, 0};

        MoteurImpl moteur = new MoteurImpl(HEIGHT, WIDTH);
        seed = moteur.getSeed();
        moteur.initJoueurs(
                new JoueurIA(moteur, JoueurIA.Difficulty.FACILE, false),
                new JoueurIA(moteur, JoueurIA.Difficulty.MOYEN, false));
        for (int i = 0; i < NB_PARTIE; i++) {
            moteur.start();

            while (moteur.isRunning()) {
                Thread.yield();
            }

            nbPartieGagner[moteur.getJoueurMain()]++;
        }

        System.out.println("Seed : " + seed
                + "\nNombre de partie gagner par IA_FACILE : " + nbPartieGagner[0]
                + "\nNombre de partie gagner par IA_MOYEN : " + nbPartieGagner[1]);

        Assert.assertTrue(nbPartieGagner[1] > nbPartieGagner[0]);
    }

    @Test
    public void battleMoyenVsDifficile() {
        long seed = -1;
        int nbPartieGagner[] = new int[] {0, 0};

        MoteurImpl moteur = new MoteurImpl(HEIGHT, WIDTH);
        seed = moteur.getSeed();
        moteur.initJoueurs(
                new JoueurIA(moteur, JoueurIA.Difficulty.MOYEN, false),
                new JoueurIA(moteur, JoueurIA.Difficulty.DIFFICILE, false));
        for (int i = 0; i < NB_PARTIE_LIGHT; i++) {
            moteur.start();

            while (moteur.isRunning()) {
                Thread.yield();
            }

            nbPartieGagner[moteur.getJoueurMain()]++;
        }

        System.out.println("Seed : " + seed
                + "\nNombre de partie gagner par IA_MOYEN : " + nbPartieGagner[0]
                + "\nNombre de partie gagner par IA_DIFFICILE : " + nbPartieGagner[1]);

        Assert.assertTrue(nbPartieGagner[1] > nbPartieGagner[0]);
    }

    @Test
    public void battleFacileVsDifficile() {
        long seed = -1;
        int nbPartieGagner[] = new int[] {0, 0};

        MoteurImpl moteur = new MoteurImpl(HEIGHT, WIDTH);
        seed = moteur.getSeed();
        moteur.initJoueurs(
                new JoueurIA(moteur, JoueurIA.Difficulty.FACILE, false),
                new JoueurIA(moteur, JoueurIA.Difficulty.DIFFICILE, false));
        for (int i = 0; i < NB_PARTIE_LIGHT; i++) {
            moteur.start();

            while (moteur.isRunning()) {
                Thread.yield();
            }

            nbPartieGagner[moteur.getJoueurMain()]++;
        }

        System.out.println("Seed : " + seed
                + "\nNombre de partie gagner par IA_FACILE : " + nbPartieGagner[0]
                + "\nNombre de partie gagner par IA_DIFFICILE : " + nbPartieGagner[1]);

        Assert.assertTrue(nbPartieGagner[1] > nbPartieGagner[0]);
    }

}

