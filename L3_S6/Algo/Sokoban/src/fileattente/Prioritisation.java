package fileattente;

public interface Prioritisation<E> {

    boolean estPrioritaire(E element, E parRapport);
}
