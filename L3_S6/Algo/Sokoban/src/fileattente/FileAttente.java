package fileattente;

public interface FileAttente<E> {

    boolean estVide();

    E consulter();

    E prochain();

    void ajout(E element);

    static <E> FileAttente<E> fifo(int capacity) {
        return new File<E>(capacity);
    }

    static <E> FileAttente<E> lifo(int capacity) {
        return new Pile<>(capacity);
    }

    static <E> FileAttente<E> parPriorite(int capacity, Prioritisation<E> prioritisation) {
        return new FilePriorite<>(capacity, prioritisation);
    }

    class PleineException extends RuntimeException {
    }

    class VideException extends RuntimeException {
    }
}
