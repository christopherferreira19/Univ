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
import java.awt.event.*;
import Dessin.Fenetre;
import terrain.TerrainGraphique;

class EcouteurDeSouris implements MouseListener {
    Fenetre f;
    TerrainGraphique tg;
    Moteur m;

    EcouteurDeSouris(Fenetre f, TerrainGraphique tg, Moteur m) {
        this.f = f;
        this.tg = tg;
        this.m = m;
    }

    public void mousePressed(MouseEvent e) {
        int colonne,ligne;

        colonne = tg.calculeColonne(e.getX());
        ligne = tg.calculeLigne(e.getY());
        if (m.action(ligne, colonne)) {
            // Exemple d'utilisation du statut d'une case : plus on passe par
            // une case, plus celle-ci est fonc�e.
            //tg.setStatut(tg.getStatut(ligne,ligne).darker(),ligne,ligne);
            f.tracerSansDelai(tg);
        }
    }

    // Il faut aussi une implementation pour les autres methodes de l'interface
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
