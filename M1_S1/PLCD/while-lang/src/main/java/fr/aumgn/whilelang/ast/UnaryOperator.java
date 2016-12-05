package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public enum UnaryOperator {
    PLUS(WhileType.INTEGER),
    MINUS(WhileType.INTEGER),
    NOT(WhileType.BOOLEAN);

    private final WhileType resultType;

    UnaryOperator(WhileType resultType) {
        this.resultType = resultType;
    }

    public WhileType resultType() {
        return resultType;
    }
}
