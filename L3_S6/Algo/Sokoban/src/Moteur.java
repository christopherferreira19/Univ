import graph.CheminPousseur;
import terrain.*;

import java.util.List;

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
class Moteur {
    Terrain t;
    int lignePousseur, colonnePousseur;

    Moteur(Terrain t) {
        this.t = t;
        for (int i=0; i<t.hauteur(); i++)
            for (int j=0; j<t.largeur(); j++)
                if (t.consulter(i,j).contient(Case.POUSSEUR)) {
                    lignePousseur = i;
                    colonnePousseur = j;
                    return;
                }
    }

    public boolean action(int ligne, int colonne) {
        if (t.consulter(ligne,colonne).contient(Case.SAC)){
            return actionSac(ligne, colonne);
        }
        else {
            return actionPousseur(ligne, colonne);
        }
    }

    private boolean actionPousseur(int ligne, int colonne) {
        Coords depart = new Coords(lignePousseur, colonnePousseur);
        Coords arrivee = new Coords(ligne, colonne);
        List<Coords> chemin = CheminPousseur.trouver(t, depart, arrivee);
        t.resetAllStatuts((Dir) null);
        if (chemin.isEmpty()) {
            return false;
        }

        t.deplacer(Case.POUSSEUR, lignePousseur, colonnePousseur, ligne, colonne);

        lignePousseur = ligne;
        colonnePousseur = colonne;
        return true;
    }

    private boolean actionSac(int ligne, int colonne) {
        int diffLigne = ligne - lignePousseur;
        int diffColonne = colonne - colonnePousseur;

        if (Math.abs(diffLigne) + Math.abs(diffColonne) != 1) {
            return false;
        }

        Case courante = t.consulter(ligne + diffLigne, colonne + diffColonne);
        if (!courante.estLibre()) {
            return false;
        }

        t.deplacer(Case.SAC, ligne, colonne, ligne + diffLigne, colonne +diffColonne);
        t.deplacer(Case.POUSSEUR,
                lignePousseur, colonnePousseur,
                lignePousseur + diffLigne, colonnePousseur + diffColonne);

        lignePousseur = lignePousseur + diffLigne;
        colonnePousseur = colonnePousseur + diffColonne;

        return true;
    }
}
