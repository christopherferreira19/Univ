package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public enum BinaryOperator {
    OR(WhileType.BOOLEAN),
    AND(WhileType.BOOLEAN),

    EQUAL_TO(WhileType.BOOLEAN),
    DIFFERENT(WhileType.BOOLEAN),

    LESS_THAN(WhileType.BOOLEAN),
    LESS_EQUAL_TO(WhileType.BOOLEAN),
    GREATER_THAN(WhileType.BOOLEAN),
    GREATER_EQUAL_TO(WhileType.BOOLEAN),

    PLUS(WhileType.INTEGER),
    MINUS(WhileType.INTEGER),
    TIMES(WhileType.INTEGER),
    DIVIDE(WhileType.INTEGER),
    MODULO(WhileType.INTEGER);

    private final WhileType resultType;

    BinaryOperator(WhileType resultType) {
        this.resultType = resultType;
    }

    public WhileType resultType() {
        return resultType;
    }
}
