package terrain;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.IOException;
import java.net.URL;
import Dessin.ObjetGraphique;
import Dessin.Fenetre;

public class TerrainGraphique implements ObjetGraphique {

    public static int CASE_L = 50;
    public static int CASE_H = 50;

    private static int CASE_L_DEMI = CASE_L / 2;
    private static int CASE_H_DEMI = CASE_H / 2;

    private static int CASE_L_TIERS = CASE_L / 6;
    private static int CASE_H_TIERS = CASE_H / 6;

    Fenetre f;
    Terrain t;
    BufferedImage pousseur, sac, but, pousseurSurBut, sacSurBut;

    private URL getImage(String nom) {
        ClassLoader cl = getClass().getClassLoader();
        return cl.getResource("Images/" + nom);
    }

    public TerrainGraphique(Fenetre f, Terrain t) {
        this.f = f;
        this.t = t;
        
        try {
            pousseur = ImageIO.read(getImage("Pousseur.png"));
            sac = ImageIO.read(getImage("Sac.png"));
            but = ImageIO.read(getImage("But.png"));
            pousseurSurBut = ImageIO.read(getImage("PousseurSurBut.png"));
            sacSurBut = ImageIO.read(getImage("SacSurBut.png"));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private int largeurCase() {
        return CASE_L;
    }

    private int hauteurCase() {
        return CASE_H;
    }

    @Override
    public ObjetGraphique clone() {
        return new TerrainGraphique(f, t.clone());
    }

    public void draw(Graphics2D g) {
        // On efface tout
        g.setPaint(Color.white);
        g.fillRect(0, 0, f.largeur(), f.hauteur());
        g.setPaint(Color.black);

        int lC = largeurCase();
        int hC = hauteurCase();

        // On affiche les cases
        for (int i=0; i<t.hauteur(); i++)
            for (int j=0; j<t.largeur(); j++) {
                int x,y;
                x = j*lC;
                y = i*hC;

                g.setPaint(Color.WHITE);
                g.fillRect(x, y, lC, hC);
                statutPaint(g, i, j, x, y);

                switch (t.consulter(i, j)) {
                case OBSTACLE:
                    g.setPaint(Color.black);
                    g.fillRect(x, y, lC, hC);
                    break;
                case POUSSEUR:
                    g.drawImage(pousseur, x, y, lC, hC, null);
                    break;
                case POUSSEUR_SUR_BUT:
                    g.drawImage(pousseurSurBut, x, y, lC, hC, null);
                    break;
                case SAC:
                    g.drawImage(sac, x, y, lC, hC, null);
                    break;
                case SAC_SUR_BUT:
                    g.drawImage(sacSurBut, x, y, lC, hC, null);
                    break;
                case BUT:
                    g.drawImage(but, x, y, lC, hC, null);
                    break;
                }
            }
    }

    private void statutPaint(Graphics2D g, int i, int j, int x, int y) {
        Statut statut = t.getStatut(i, j);
        statutPaint(g, x, y, 0, 0, statut.getColor(null), statut.getText(null), true);
        for (Dir dir : Dir.values()) {
            statutPaint(g, x, y, dir.dl, dir.dc, statut.getColor(dir), statut.getText(dir), false);
        }
    }

    private void statutPaint(Graphics2D g, int x, int y, int dl, int dc, Color color, String text, boolean middle) {
        g.setPaint(color);
        if (middle) {
            g.fillRect(
                    x + (dc + 1) * (CASE_L_DEMI - CASE_L_TIERS),
                    y + (dl + 1) * (CASE_H_DEMI - CASE_H_TIERS),
                    2 * CASE_L_TIERS,
                    2 * CASE_H_TIERS);
        }
        else {
            g.fillOval(
                    x + (dc + 1) * (CASE_L_DEMI - CASE_L_TIERS),
                    y + (dl + 1) * (CASE_H_DEMI - CASE_H_TIERS),
                    2 * CASE_L_TIERS,
                    2 * CASE_H_TIERS);
        }

        g.setPaint(Color.BLACK);
        if (!text.isEmpty()) {
            g.drawString(text,
                    x + (dc + 1) * CASE_L_DEMI,
                    y + (dc + 1) * CASE_H_DEMI);
        }
    }

    public int calculeLigne(int y) {
        return y / hauteurCase();
    }

    public int calculeColonne(int x) {
        return x / largeurCase();
    }

    public boolean equals(ObjetGraphique o) {
        TerrainGraphique autre = null;
        try {
            autre = (TerrainGraphique) o;
        } catch (ClassCastException e) {
            return false;
        }
        return t.equals(autre.t);
    }

    public int key() {
        // Non utilise car un terrain n'est jamais efface (mais ecrase)
        return 0;
    }

    public String toString() {
        return t.toString();
    }
}
