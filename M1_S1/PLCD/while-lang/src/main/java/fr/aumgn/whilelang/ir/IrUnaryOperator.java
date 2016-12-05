package fr.aumgn.whilelang.ir;

import fr.aumgn.whilelang.ast.UnaryOperator;

public enum IrUnaryOperator {
    NOT;

    void asString(StringBuilder builder) {
        builder.append(name());
    }

    public static IrUnaryOperator of(UnaryOperator operator) {
        if (operator != UnaryOperator.NOT) {
            throw new RuntimeException("Unknown operator : '" + operator + "'");
        }
        return NOT;
    }
}
