package gauffre.moteur;

public interface Moteur {

    void initJoueurs(Joueur... joueurs);

    void registerObserver(MoteurObserver observer);

    Gauffre getGauffre();

    void start();

    boolean jouer(int l, int c);

    boolean togglePause();

    boolean isPaused();

    boolean annuler();

    boolean refaire();

    int nextInt(int n);

    int getJoueurMain();

    long getSeed();

    boolean isRunning();
}
