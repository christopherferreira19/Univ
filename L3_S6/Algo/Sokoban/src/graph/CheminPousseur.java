package graph;

import etape.Stats;
import fileattente.FileAttente;
import terrain.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CheminPousseur {

    public static List<Coords> trouver(Terrain t, Coords depart, Coords arrivee) {
        return trouver(t, depart, arrivee, new Stats(), new Visualisation.Desactivee());
    }

    public static List<Coords> trouver(Terrain t, Coords depart, Coords arrivee, Stats stats, Visualisation visu) {
        CheminPousseur chemin = new CheminPousseur(t, depart, arrivee, stats, visu);
        return chemin.bfs()
                ? chemin.construireChemin()
                : Collections.emptyList();
    }

    private final Terrain terrain;
    private final Coords depart;
    private final Coords arrivee;

    private final Stats stats;
    private final Visualisation visu;

    private final FileAttente<Coords> file;
    private final Coords[][] provenances;

    private CheminPousseur(Terrain terrain, Coords depart, Coords arrivee, Stats stats, Visualisation visu) {
        this.terrain = terrain;
        this.depart = depart;
        this.arrivee = arrivee;
        this.stats = stats;
        this.visu = visu;

        this.file = FileAttente.fifo(terrain.hauteur() * terrain.largeur());
        this.provenances = new Coords[terrain.hauteur()][terrain.largeur()];
    }

    private boolean bfs() {
        Case arriveeCase = terrain.consulter(arrivee.ligne, arrivee.colonne);
        if (!arriveeCase.estLibreOuPousseur()) {
            return false;
        }

        terrain.getStatut(depart).setColor(null, Color.RED);
        visu.updatePousseur();

        provenances[arrivee.ligne][arrivee.colonne] = arrivee;
        file.ajout(arrivee);

        while (!file.estVide() && !file.consulter().equals(depart)) {
            Coords courant = file.prochain();
            stats.addPousseurConfiguration(1);

            terrain.getStatut(courant).setColor(null, Color.GREEN);
            visu.updatePousseur();

            for (Dir dir : Dir.values()) {
                enfilerVoisinSiLibre(courant, dir);
            }
            terrain.getStatut(courant).setColor(null, Color.GRAY);
            visu.updatePousseur();
        }

        return !file.estVide();
    }

    private void enfilerVoisinSiLibre(Coords courant, Dir dir) {
        Coords voisin = courant.voisin(dir);
        Case voisinCase = terrain.consulter(voisin.ligne, voisin.colonne);
        if (voisinCase.estLibreOuPousseur() && provenances[voisin.ligne][voisin.colonne] == null) {
            file.ajout(voisin);
            provenances[voisin.ligne][voisin.colonne] = courant;
            terrain.getStatut(voisin).setColor(null, Color.BLUE);
        }
    }

    private List<Coords> construireChemin() {
        List<Coords> chemin = new ArrayList<>();
        Coords courant = file.prochain();
        chemin.add(courant);
        while(courant != arrivee){
            courant = provenances[courant.ligne][courant.colonne];
            chemin.add(courant);
        }
        return chemin;
    }
}
