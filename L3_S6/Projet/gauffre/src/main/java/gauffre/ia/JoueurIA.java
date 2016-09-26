package gauffre.ia;

import gauffre.Coords;
import gauffre.moteur.Joueur;
import gauffre.moteur.Moteur;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JoueurIA implements Joueur {

    private static long DELAY = 800;

    public enum Difficulty {
        FACILE {
            IA createIA(Moteur m, AtomicBoolean cancelled) {
                return new IAFacile(m, cancelled);
            }
        },
        MOYEN {
            IA createIA(Moteur m, AtomicBoolean cancelled) {
                return new IANormale(m, cancelled);
            }
        },
        DIFFICILE {
            IA createIA(Moteur m, AtomicBoolean cancelled) {
                return new IADifficile(m, cancelled);
            }
        };

        abstract IA createIA(Moteur m, AtomicBoolean cancelled);
    }

    private final Moteur moteur;
    private final Difficulty difficulty;
    private final boolean withDelay;

    private AtomicBoolean cancelled;
    private Thread thread;

    public JoueurIA(Moteur moteur, Difficulty difficulty, boolean withDelay) {
        this.moteur = moteur;
        this.difficulty = difficulty;
        this.withDelay = withDelay;

        this.cancelled = null;
        this.thread = null;
    }

    @Override
    public void startTurn() {
        this.cancelled = new AtomicBoolean(false);
        this.thread = new Thread(() -> turn(moteur));
        thread.start();
    }

    @Override
    public void cancelTurn() {
        if (thread == null) {
            return;
        }

        cancelled.set(true);
        thread.interrupt();
    }

    private void turn(Moteur m) {
        long startMillis = System.currentTimeMillis();
        IA ia = difficulty.createIA(moteur, cancelled);
        final Coords res = ia.prochainCoup();

        delay(startMillis);

        if (cancelled.get()) {
            return;
        }

        SwingUtilities.invokeLater(() -> turnDone(m, res));
    }

    private void delay(long startMillis) {
        if (!withDelay) {
            return;
        }

        long delay = DELAY - (System.currentTimeMillis() - startMillis);
        while (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                if (cancelled.get()) {
                    break;
                }
            }

            delay = DELAY - (System.currentTimeMillis() - startMillis);
        }
    }

    private void turnDone(Moteur m, Coords res) {
        boolean wasCancelled = cancelled.get();
        this.thread = null;
        this.cancelled = null;
        if (wasCancelled) {
            return;
        }

        if (!m.jouer(res.l, res.c)) {
            startTurn();
        }
    }
}
