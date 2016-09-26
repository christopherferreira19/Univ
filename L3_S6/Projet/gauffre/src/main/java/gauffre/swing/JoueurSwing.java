package gauffre.swing;

import gauffre.Coords;
import gauffre.moteur.Joueur;
import gauffre.moteur.Moteur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class JoueurSwing implements Joueur {

    private final Moteur moteur;
    private final GauffreComponent component;
    private final JoueurMouseListener listener;

    JoueurSwing(Moteur moteur, GauffreComponent component) {
        this.moteur = moteur;
        this.component = component;
        this.listener = new JoueurMouseListener();
    }

    @Override
    public void startTurn() {
        component.addMouseListener(listener);
        component.addMouseMotionListener(listener);
        listener.initOverlay();
    }

    private class JoueurMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            Coords coords = component.pointToCoords(e.getPoint());
            if (moteur.jouer(coords.l, coords.c)) {
                component.clearOverlay();
                component.removeMouseListener(this);
                component.removeMouseMotionListener(this);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Coords coords = component.pointToCoords(e.getPoint());
            component.setOverlay(coords.l, coords.c);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            component.clearOverlay();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            initOverlay();
        }

        private void initOverlay() {
            Point mousePosition = component.getMousePosition();
            if (mousePosition != null) {
                Coords coords = component.pointToCoords(mousePosition);
                component.setOverlay(coords.l, coords.c);
            }
        }
    }

    @Override
    public void cancelTurn() {
        component.removeMouseListener(listener);
        component.removeMouseMotionListener(listener);
    }
}
