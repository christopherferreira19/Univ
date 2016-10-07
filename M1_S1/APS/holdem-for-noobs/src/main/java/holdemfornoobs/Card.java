package holdemfornoobs;

public enum Card {

    C2,

    C3,

    C4,

    C5,

    C6,

    C7,

    C8,

    C9,

    C10,

    JACK,

    QUEEN,

    KING,

    ACE;

    public int value() {
        return ordinal() + 2;
    }
}
