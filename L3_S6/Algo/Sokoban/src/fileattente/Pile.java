package fileattente;

class Pile<E> implements FileAttente<E> {

    private final E[] array;
    private int taille;

    Pile(int capacity) {
        @SuppressWarnings("unchecked")
        E[] safeCast = (E[]) new Object[capacity];
        this.array = safeCast;
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

        return array[taille - 1];
    }

    @Override
    public E prochain() {
        E prochain = consulter();
        taille--;
        return prochain;
    }

    @Override
    public void ajout(E element) {
        if (taille + 1 == array.length) {
            throw new PleineException();
        }

        array[taille] = element;
        taille++;
    }
}
