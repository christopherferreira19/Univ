package gauffre.swing;

import gauffre.ia.JoueurIA;
import gauffre.laf.Icons;
import gauffre.laf.LafInitialization;
import gauffre.laf.ToolBar;
import gauffre.moteur.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SwingUI implements Runnable, MoteurObserver {

    private Moteur moteur;
    private JButton pause;
    private JLabel joueur1;
    private JLabel joueur2;

    public void run() {
        this.moteur = new MoteurImpl(10, 14);

        LafInitialization.initLaf();

        GauffreComponent gauffreComponent = new GauffreComponent(moteur);

        moteur.initJoueurs(
                new JoueurSwing(moteur, gauffreComponent),
                new JoueurIA(moteur, JoueurIA.Difficulty.DIFFICILE, true));

        JButton newButton = new JButton(Icons.RELAUNCH.get());
        JButton settingsButton = new JButton(Icons.GEAR.get());

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(1, 2, 30, 1));
        statusPanel.setOpaque(false);
        this.joueur1 = new JLabel("Joueur 1");
        joueur1.setIcon(Icons.BULLET_GRAY.get());
        this.joueur2 = new JLabel("Joueur 2");
        joueur2.setIcon(Icons.BULLET_GRAY.get());
        statusPanel.add(joueur1);
        statusPanel.add(joueur2);

        JButton annuler = new JButton(Icons.UNDO.get());
        this.pause = new JButton(Icons.PAUSE.get());
        JButton refaire = new JButton(Icons.REDO.get());

        ToolBar toolbar = new ToolBar();
        toolbar.add(newButton);
        toolbar.add(settingsButton);
        toolbar.addToMiddle(statusPanel);
        toolbar.addToEnd(annuler);
        toolbar.addToEnd(pause);
        toolbar.addToEnd(refaire);

        Settings settings = new Settings();
        SettingsDialog settingsDialog = new SettingsDialog(settings);
        settingsButton.addActionListener(actionEvent -> settingsDialog.setVisible(true));

        annuler.addActionListener(actionEvent -> moteur.annuler());
        pause.addActionListener(actionEvent -> moteur.togglePause());
        refaire.addActionListener(actionEvent -> moteur.refaire());

        JFrame frame = new JFrame("Gauffre Swing");
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(gauffreComponent);

        moteur.registerObserver(this);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                moteur.start();
            }
        });

        frame.setVisible(true);
    }

    @Override
    public void changed() {
        pause.setIcon(moteur.isPaused() ? Icons.PLAY.get() : Icons.PAUSE.get());
        if (moteur.getJoueurMain() == 0) {
            joueur1.setIcon(Icons.BULLET_GREEN.get());
            joueur2.setIcon(Icons.BULLET_GRAY.get());
        }
        else {
            joueur1.setIcon(Icons.BULLET_GRAY.get());
            joueur2.setIcon(Icons.BULLET_GREEN.get());
        }

        if (!moteur.isPaused() && !moteur.isRunning()) {
            int winner = moteur.getJoueurMain() + 1;
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(pause),
                    "Joueur " + winner + " a gagné !");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SwingUI());
    }
}
