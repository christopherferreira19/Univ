package holdemfornoobs;

import java.util.*;

public class Cards {

    private final Card[] array;
    private final int left;
    private final int right;

    public static Cards generate(int size, Random random) {
        ArrayList<Object> deck = new ArrayList<>();
        Collections.addAll(deck, Card.values());
        Collections.addAll(deck, Card.values());
        Collections.addAll(deck, Card.values());
        Collections.addAll(deck, Card.values());

        Collections.shuffle(deck, random);
        Card[] array = new Card[size];
        deck.subList(0, size).toArray(array);
        return new Cards(array);
    }

    private Cards(Card[] array) {
        this(array, 0, array.length - 1);
    }

    private Cards(Card[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    public boolean isEmpty() {
        return left > right;
    }

    public boolean isOnlyOneLeft() {
        return left == right;
    }

    public Card left() {
        return array[left];
    }

    public Card right() {
        return array[right];
    }

    public Cards popLeft() {
        return new Cards(array, left + 1, right);
    }

    public Cards popRight() {
        return new Cards(array, left, right - 1);
    }

    public int totalValue() {
        int totalValue = 0;
        for (Card card : array) {
            totalValue += card.value();
        }

        return totalValue;
    }

    public String toString() {
        return Arrays.asList(array).toString();
    }
}
