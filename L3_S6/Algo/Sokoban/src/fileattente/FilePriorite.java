package fileattente;

import java.util.Random;

class FilePriorite<E> implements FileAttente<E> {

    private final E[] tableau;
    private final Prioritisation<E> prioritisation;
    private int taille;

    FilePriorite(int capacity, Prioritisation<E> prioritisation) {
        @SuppressWarnings("unchecked")
        E[] safeCast = (E[]) new Object[capacity];
        this.tableau = safeCast;
        this.prioritisation = prioritisation;
        this.taille = 0;
    }

    @Override
    public boolean estVide() {
        return taille == 0;
    }

    @Override
    public E consulter() {
        if (taille == 0) {
            throw new VideException();
        }

        return tableau[0];
    }

    @Override
    public E prochain() {
        E prochain = consulter();
        taille--;
        percolerBas(0, tableau[taille]);
        return prochain;
    }

    @Override
    public void ajout(E element) {
        if (taille + 1 == tableau.length) {
            throw new PleineException();
        }

        percolerHaut(taille, element);
        taille++;
    }

    private void percolerBas(int i, E element) {
        int derniereFeuille = taille / 2;
        while (i < derniereFeuille) {
            int fils1 = (i * 2) + 1;
            int fils2 = (i * 2) + 2;

            int fils = fils2 < taille && prioritisation.estPrioritaire(tableau[fils2], tableau[fils1])
                    ? fils2
                    : fils1;
            if (prioritisation.estPrioritaire(element, tableau[fils])) {
                break;
            }

            tableau[i] = tableau[fils];
            i = fils;
        }

        tableau[i] = element;
    }

    private void percolerHaut(int i, E element) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (!prioritisation.estPrioritaire(element, tableau[parent])) {
                break;
            }

            tableau[i] = tableau[parent];
            i = parent;
        }

        tableau[i] = element;
    }

    public static void main(String[] args) {
        Random random = new Random();

        int n = 20;
        FileAttente<Integer> file = FileAttente.parPriorite(n + 1, (e, p) -> e < p);
        for (int i = 0; i < n; i++) {
            file.ajout(random.nextInt(100));
        }

        while (!file.estVide()) {
            System.out.println(file.prochain());
        }
    }
}
