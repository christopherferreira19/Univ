package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public class Numeral implements Expression {

    private final int value;

    Numeral(int value) {
        this.value = value;
    }

    @Override
    public WhileType type() {
        return WhileType.INTEGER;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitNumeral(this);
    }

    public int getValue() {
        return value;
    }
}
