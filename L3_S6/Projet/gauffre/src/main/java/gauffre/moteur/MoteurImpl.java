package gauffre.moteur;

import gauffre.Coords;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MoteurImpl implements Moteur {

    private final List<MoteurObserver> observers;

    private final GauffreImpl gauffre;

    private Joueur[] joueurs;
    private int joueurMain;

    private boolean paused;
    private History history;
    private final long seed;
    private Random r;
    private boolean running;

    public MoteurImpl(int height, int width) {
        this(height, width, System.nanoTime());
    }

    public MoteurImpl(int height, int width, long seed) {
        if (height <= 1 || width <= 1) {
            throw new RuntimeException("There's no game to play with less than one column or less than one line...");
        }

        this.observers = new ArrayList<>();

        this.gauffre = new GauffreImpl(height, width);

        this.joueurs = null;
        this.joueurMain = 0;

        this.paused = false;
        this.history = new History();
        this.seed = seed;
        this.r = new Random(seed);
        this.running = false;
    }

    public void initJoueurs(Joueur... joueurs) {
        if (this.joueurs != null) {
            throw new RuntimeException("Joueurs already initialized");
        }

        this.joueurs = new Joueur[joueurs.length];
        System.arraycopy(joueurs, 0, this.joueurs, 0, joueurs.length);
    }

    public void registerObserver(MoteurObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
         observers.forEach(MoteurObserver::changed);
    }

    public Gauffre getGauffre() {
        return gauffre;
    }

    @Override
    public void start() {
        if (joueurs == null) {
            throw new RuntimeException("Joueurs not initialized");
        }
        if (joueurs.length == 1) {
            throw new RuntimeException("Won't play a game with one player, come on !");
        }

        this.running = true;
        this.joueurMain = 0;
        gauffre.reset();
        joueurs[joueurMain].startTurn();
        notifyObservers();
    }

    @Override
    public boolean jouer(int l, int c) {
        if (!running) {
            throw new RuntimeException();
        }
        if ((l == 0 && c == 0) || !gauffre.getState(l, c)) {
            return false;
        }

        history.add(new Coords(l, c));
        gauffre.removeFrom(l, c);

        // S'il reste au moins un coup à jouer
        if (gauffre.getState(1, 0) || gauffre.getState(0, 1)) {
            joueurMain = (joueurMain + 1) % joueurs.length;
            joueurs[joueurMain].startTurn();
        } else {
            running = false;
        }

        notifyObservers();

        return true;
    }

    public int getJoueurMain() {
        return joueurMain;
    }

    @Override
    public boolean togglePause() {
        if (paused) {
            if (!running) {
                running = true;
                joueurMain = (joueurMain + 1) % joueurs.length;
            }
            paused = false;
            joueurs[joueurMain].startTurn();
        }
        else {
            paused = true;
            joueurs[joueurMain].cancelTurn();
        }

        notifyObservers();
        return true;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public boolean annuler() {
        if (!paused) {
            togglePause();
        }

        List<Coords> undoList = history.annuler();
        if (undoList.isEmpty()) return false;

        joueurMain = (joueurMain - 1) % joueurs.length;
        joueurMain = joueurMain < 0 ? joueurMain + joueurs.length : joueurMain;


        Iterator<Coords> it = undoList.iterator();
        // On ignore le premier qui est celui qu'on vient d'enlever
        it.next();

        gauffre.reset();
        while (it.hasNext()) {
            Coords next = it.next();
            gauffre.removeFrom(next.l, next.c);
        }

        notifyObservers();
        return true;
    }

    @Override
    public boolean refaire() {
        if (!paused) {
            togglePause();
        }

        joueurMain = (joueurMain + 1) % joueurs.length;

        List<Coords> redoList = history.refaire();
        if (redoList.isEmpty()) return false;

        Iterator<Coords> it = redoList.iterator();
        Coords current = it.next();
        gauffre.removeFrom(current.l, current.c);

        notifyObservers();
        return true;
    }

    @Override
    public int nextInt(int n) {
        return r.nextInt(n);
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
