package graph;

import etape.*;
import fileattente.FileAttente;
import terrain.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution {

    public static List<Etape> trouver(Terrain terrain, SolutionMode mode, Stats stats, Visualisation visu) {
        Solution solution = new Solution(terrain, mode, stats, visu);
        return solution.djikstra()
                ? solution.creerEtapes()
                : Collections.emptyList();
    }

    private final Terrain terrain;

    private final Stats stats;
    private final Visualisation visu;

    private final Coords pousseurDepart;
    private final Coords sacDepart;
    private final Coords but;

    private final FileAttente<SacData> file;
    private final SacDataTable datas;

    private Solution(Terrain terrain, SolutionMode mode, Stats stats, Visualisation visu) {
        this.terrain = terrain;
        this.stats = stats;
        this.visu = visu;

        // On récupère les positions importantes (pousseur, sac et but)
        // en parcourant le terrain
        Coords pousseurDepartTmp = null;
        Coords sacDepartTmp = null;
        Coords butTmp = null;
        for (int i = 0; i < terrain.hauteur(); i++) {
            for (int j = 0; j < terrain.largeur(); j++) {
                if (terrain.consulter(i, j).contient(Case.POUSSEUR)) {
                    pousseurDepartTmp = new Coords(i, j);
                }
                else if (terrain.consulter(i, j).contient(Case.SAC)) {
                    sacDepartTmp = new Coords(i, j);
                }
                else if (terrain.consulter(i, j).contient(Case.BUT)) {
                    butTmp = new Coords(i, j);
                }
            }
        }

        if (pousseurDepartTmp == null || sacDepartTmp == null || butTmp == null) {
            throw new RuntimeException("Terrain invalide");
        }

        this.pousseurDepart = pousseurDepartTmp;
        this.sacDepart = sacDepartTmp;
        this.but = butTmp;

        int capacite = terrain.hauteur() * terrain.largeur() * 4;
        this.file = mode.creerFileAttente(capacite, but);
        this.datas = new SacDataTable(terrain);
    }

    private boolean djikstra() {
        // On initialise notre file avec les potentiels 4 positions
        // desquelles le sac peut être poussé au début
        stats.addSacConfiguration(1);
        for (Dir dir : Dir.values()) {
            ajouterVoisinAccessibles(sacDepart, pousseurDepart, dir, null);
            terrain.resetAllStatuts((Dir) null);
            visu.updateSac();
        }

        Coords sacPos = sacDepart;
        Coords pousseurPos = pousseurDepart;
        while (!file.estVide() && !file.consulter().coords.equals(but)) {
            SacData courant = file.prochain();
            stats.addSacConfiguration(1);

            // Deplacement du pousseur et du sac, et affichage de couleur si besoin
            terrain.getStatut(courant.coords).setColor(courant.dir.oppose(), Color.GREEN);
            terrain.assigner(terrain.consulter(sacPos).retrait(Case.SAC), sacPos);
            terrain.assigner(terrain.consulter(pousseurPos).retrait(Case.POUSSEUR), pousseurPos);
            pousseurPos = courant.coords.voisin(courant.dir.oppose());
            sacPos = courant.coords;
            terrain.assigner(terrain.consulter(sacPos).ajout(Case.SAC), sacPos);
            terrain.assigner(terrain.consulter(pousseurPos).ajout(Case.POUSSEUR), pousseurPos);
            visu.updateSac();

            for (Dir dir : Dir.values()) {
                ajouterVoisinAccessibles(courant, dir);
            }

            terrain.getStatut(courant.coords).setColor(courant.dir.oppose(), Color.GRAY);
            visu.updateSac();
        }

        // On remet le pousseur et le sac à leur places initiales
        terrain.assigner(terrain.consulter(sacPos).retrait(Case.SAC), sacPos);
        terrain.assigner(terrain.consulter(pousseurPos).retrait(Case.POUSSEUR), pousseurPos);
        terrain.assigner(terrain.consulter(sacDepart).ajout(Case.SAC), sacDepart);
        terrain.assigner(terrain.consulter(pousseurDepart).ajout(Case.POUSSEUR), pousseurDepart);
        terrain.resetAllStatuts((Dir) null);
        terrain.resetAllStatuts(Dir.values());
        visu.updateSac();

        return !file.estVide();
    }

    private void ajouterVoisinAccessibles(SacData sacData, Dir dir) {
        Coords pousseur = sacData.coords.voisin(sacData.dir.oppose());
        ajouterVoisinAccessibles(sacData.coords, pousseur, dir, sacData);
    }

    private void ajouterVoisinAccessibles(Coords sacPos, Coords pousseur, Dir dir, SacData provenance) {
        Coords sacDest = sacPos.voisin(dir);
        Case sacDestCase = terrain.consulter(sacDest);
        if (!sacDestCase.estLibreOuPousseur()) {
            return;
        }

        Dir dirOppose = dir.oppose();
        Statut terrainStatut = terrain.getStatut(sacDest);
        terrainStatut.setColor(dirOppose, Color.YELLOW);
        Coords pousseDepuis = sacPos.voisin(dirOppose);

        List<Coords> chemin = CheminPousseur.trouver(terrain, pousseur, pousseDepuis, stats, visu);
        terrain.resetAllStatuts((Dir) null);

        if (chemin.isEmpty()) {
            terrainStatut.setColor(dirOppose, Color.BLACK);
            visu.updateSac();
        }
        else {
            terrainStatut.setColor(dirOppose, Color.BLUE);
            visu.updateSac();

            SacData voisin = new SacData(sacDest, dir, provenance, chemin);
            SacData existant = datas.get(voisin);
            if (existant != null && existant.distance <= voisin.distance) {
                return;
            }

            file.ajout(voisin);
            datas.set(voisin, voisin);
        }
    }

    private List<Etape> creerEtapes() {
        // On commence par remonter nos datas depuis
        // l'arrivée jusqu'au départ
        FileAttente<SacData> sacEtapes = FileAttente.lifo(terrain.hauteur() * terrain.largeur() * 4);
        SacData courant = file.prochain();
        while (courant != null) {
            sacEtapes.ajout(courant);
            courant = courant.provenance;
        }

        // A partir de là on peut créer l'enchainement des mouvements
        // qui forment notre solution
        List<Etape> etapes = new ArrayList<>();
        Coords sacPos = sacDepart;
        Coords pousseurPos = pousseurDepart;

        while (!sacEtapes.estVide()) {
            SacData sacData = sacEtapes.prochain();

            // Déplacement du pousseur du bon côté du sac si besoin
            if (sacData.cheminPousseur.size() > 2) {
                for (Coords coords : sacData.cheminPousseur) {
                    etapes.add(Deplacement.pousseur(pousseurPos, coords));
                    pousseurPos = coords;
                }
            }

            // Déplacement du sac
            etapes.add(Etapes.poussement(pousseurPos, sacPos, sacData.coords));
            pousseurPos = sacPos;
            sacPos = sacData.coords;
        }

        return etapes;
    }
}
