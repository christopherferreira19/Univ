package fileattente;

class File<E> implements FileAttente<E> {

    private final E[] tableau;
    private int premier;
    private int taille;

    File(int capacity) {
        @SuppressWarnings("unchecked")
        E[] safeCast = (E[]) new Object[capacity];
        this.tableau = safeCast;
        this.premier = 0;
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

        return tableau[premier];
    }

    @Override
    public E prochain() {
        E prochain = consulter();
        premier++;
        taille--;
        return prochain;
    }

    @Override
    public void ajout(E element) {
        if (taille + 1 == tableau.length) {
            throw new PleineException();
        }

        tableau[(premier + taille) % tableau.length] = element;
        taille++;
    }
}
