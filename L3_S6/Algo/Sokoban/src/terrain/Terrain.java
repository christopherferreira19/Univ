package terrain;

/*
Sokoban - impl�mentation manuelle et automatique du c�l�bre jeu
Copyright (C) 2009 Guillaume Huard
Ce programme est libre, vous pouvez le redistribuer et/ou le modifier selon les
termes de la Licence Publique G�n�rale GNU publi�e par la Free Software
Foundation (version 2 ou bien toute autre version ult�rieure choisie par vous).

Ce programme est distribu� car potentiellement utile, mais SANS AUCUNE
GARANTIE, ni explicite ni implicite, y compris les garanties de
commercialisation ou d'adaptation dans un but sp�cifique. Reportez-vous � la
Licence Publique G�n�rale GNU pour plus de d�tails.

Vous devez avoir re�u une copie de la Licence Publique G�n�rale GNU en m�me
temps que ce programme ; si ce n'est pas le cas, �crivez � la Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
�tats-Unis.

Contact: Guillaume.Huard@imag.fr
         ENSIMAG - Laboratoire LIG
         51 avenue Jean Kuntzmann
         38330 Montbonnot Saint-Martin
*/

import java.util.Scanner;
import java.io.InputStream;

public class Terrain {

    private final int largeur;
    private final int hauteur;
    private final Case [][] terrain;
    private final Statut[][] statuts;

    private Terrain(int largeur, int hauteur) {
        this.hauteur = hauteur;
        this.largeur = largeur;
        terrain = new Case[hauteur][largeur];
        statuts = new Statut[hauteur][largeur];
    }

    public static Terrain random(int largeur, int hauteur, double ratio) {
        return random(largeur, hauteur, ratio, (int) System.nanoTime());
    }

    public static Terrain random(int largeur, int hauteur, double ratio, int seed) {
        Terrain terrain = new Terrain(largeur, hauteur);
        terrain.efface();
        Gen.run(terrain, seed, ratio);
        return terrain;
    }

    public static Terrain read(InputStream s) {
        return read(new Scanner(s));
    }

    public static Terrain read(Scanner s) {
        Terrain terrain = new Terrain(s.nextInt(), s.nextInt());
        terrain.lecture(s);
        terrain.initStatut();
        return terrain;
    }

    public Terrain clone() {
        Terrain nouveau = new Terrain(largeur, hauteur);
        nouveau.copie(this);
        return nouveau;
    }

    public static Terrain defaut() {
        Terrain nouveau = new Terrain(5, 5);
        nouveau.efface();
        nouveau.initStatut();
        nouveau.terrain[2][1] = Case.POUSSEUR;
        nouveau.terrain[2][2] = Case.SAC;
        nouveau.terrain[2][3] = Case.BUT;
        return nouveau;
    }

    public int largeur() {
        return largeur;
    }
    
    public int hauteur() {
        return hauteur;
    }

    public Case consulter(int ligne, int colonne) {
        return ligne >= 0 && ligne < hauteur && colonne >= 0 && colonne < largeur
                ? terrain[ligne][colonne]
                : Case.OBSTACLE;

    }

    public Case consulter(Coords coords) {
        return consulter(coords.ligne, coords.colonne);
    }

    public void assigner(Case c, int ligne, int colonne) {
        terrain[ligne][colonne] = c;
    }

    public void assigner(Case c, Coords coords) {
        assigner(c, coords.ligne, coords.colonne);
    }

    public void deplacer(Case c, int depuisLigne, int depuisColonne, int versLigne, int versColonne) {
        Case c1 = consulter(depuisLigne, depuisColonne);
        c1 = c1.retrait(c);
        assigner(c1, depuisLigne, depuisColonne);

        Case c2 = consulter(versLigne, versColonne);
        c2 = c2.ajout(c);
        assigner(c2, versLigne, versColonne);

    }

    public Statut getStatut(int ligne, int colonne) {
        return ligne >= 0 && ligne < hauteur && colonne >= 0 && colonne < largeur
                ? statuts[ligne][colonne]
                : Statut.DUMMY;
    }

    public Statut getStatut(Coords coords) {
        return getStatut(coords.ligne, coords.colonne);
    }

    public void resetAllStatuts(Dir... dirs) {
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                for (Dir dir : dirs) {
                    statuts[i][j].resetColor(dir);
                    statuts[i][j].resetText(dir);
                }
            }
        }
    }

    public void deplacer(Case c, Coords depuis, Coords vers) {
        deplacer(c, depuis.ligne, depuis.colonne, vers.ligne, vers.colonne);
    }

    public void lecture(Scanner s) {
        for (int i=0; i<hauteur(); i++) {
            String ligne;
            ligne = s.next();
            for (int j=0; j<largeur(); j++)
                switch (ligne.charAt(j)) {
                case '+':
                    terrain[i][j] = Case.OBSTACLE;
                    break;
                case 'X':
                    terrain[i][j] = Case.POUSSEUR;
                    break;
                case '&':
                    terrain[i][j] = Case.SAC;
                    break;
                case 'O':
                    terrain[i][j] = Case.BUT;
                    break;
                case 'Q':
                    terrain[i][j] = Case.SAC_SUR_BUT;
                    break;
                case '0':
                    terrain[i][j] = Case.POUSSEUR_SUR_BUT;
                    break;
                default:
                    terrain[i][j] = Case.LIBRE;
                }
        }
    }

    public String toString() {
        String resultat = largeur() + "\n" + hauteur();
        for (int i=0; i<hauteur(); i++) {
            resultat += "\n";
            for (int j=0; j<largeur(); j++)
                switch (terrain[i][j]) {
                case OBSTACLE:
                    resultat += "+";
                    break;
                case POUSSEUR:
                    resultat += "X";
                    break;
                case SAC:
                    resultat += "&";
                    break;
                case BUT:
                    resultat += "O";
                    break;
                case LIBRE:
                    resultat += ".";
                    break;
                case SAC_SUR_BUT:
                    resultat += "Q";
                    break;
                case POUSSEUR_SUR_BUT:
                    resultat += "0";
                    break;
                default:
                    resultat += "?";
                }
        }
        return resultat;
    }

    private void efface() {
        for (int i=0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                terrain[i][j] = Case.LIBRE;
                statuts[i][j] = new Statut();
            }
        }
    }

    private void initStatut() {
        for (int i=0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                statuts[i][j] = new Statut();
            }
        }
    }

    private void copie(Terrain from) {
        for (int i = 0; i < terrain.length; i++) {
            System.arraycopy(from.terrain[i], 0, terrain[i], 0, terrain[i].length);
            for (int j = 0; j < terrain[i].length; j++) {
                statuts[i][j] = from.statuts[i][j].clone();
            }
        }
    }

    public boolean equals(Terrain autre) {
        if ((autre.largeur() != largeur()) || (autre.hauteur() != hauteur()))
            return false;
        for (int i=0; i<hauteur(); i++) {
            for (int j = 0; j < largeur(); j++) {
                if (terrain[i][j] != autre.terrain[i][j])
                    return false;
            }
        }
        return true;
    }
}
