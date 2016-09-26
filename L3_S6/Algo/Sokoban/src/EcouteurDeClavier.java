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
import Dessin.Fenetre;
import terrain.Dir;
import terrain.TerrainGraphique;

import java.awt.event.*;

class EcouteurDeClavier implements KeyListener {

    private final Fenetre f;
    private final TerrainGraphique tg;
    private final Moteur m;

    EcouteurDeClavier(Fenetre f, TerrainGraphique tg, Moteur m) {
        this.f = f;
        this.m = m;
        this.tg = tg;
    }

    public void keyPressed(KeyEvent e) {
        Dir dir;
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
            dir = Dir.UP;
            break;
        case KeyEvent.VK_RIGHT:
            dir = Dir.RIGHT;
            break;
        case KeyEvent.VK_DOWN:
            dir = Dir.DOWN;
            break;
        case KeyEvent.VK_LEFT:
            dir = Dir.LEFT;
            break;
        default:
            return;
        }

        int ligne = m.lignePousseur + dir.dl;
        int colonne = m.colonnePousseur + dir.dc;
        if (m.action(ligne, colonne)) {
            f.tracerSansDelai(tg);
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
