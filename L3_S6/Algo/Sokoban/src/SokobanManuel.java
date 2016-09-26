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
import Dessin.*;
import terrain.Terrain;
import terrain.TerrainGraphique;

import java.io.*;
import java.util.*;

public class SokobanManuel {

    public static void main(Fenetre f, Evenements e, String [] args) {
        // Desactivee les mecanismes mis en oeuvre (reexecution d'une partie des
        // commandes, cache, hashage des commandes) dans Dessin lorsque les
        // commandes successives ne modifient qu'une partie de l'affichage.
        // Ici, l'affichage d'un terrain ecrase l'integralite de l'affichage
        // precedent.
        Parameters.requiresOverdraw = false;

        Terrain t = TerrainArgs.read(args);

        f.setDrawAreaSize(
                TerrainGraphique.CASE_L * t.largeur(),
                TerrainGraphique.CASE_H * t.hauteur());
        Moteur m = new Moteur(t);
        TerrainGraphique tg = new TerrainGraphique(f, t);

        e.addMouseListener(new EcouteurDeSouris(f, tg, m));
        e.addKeyListener(new EcouteurDeClavier(f, tg, m));

        f.tracerSansDelai(tg);
        
        while (true) {
            e.waitForEvent();
        }
    }
}
