package fr.aumgn.nilang.ast.expression;

import fr.aumgn.nilang.ast.Expression;

public class ExprInt implements Expression {

    private final long value;

    public ExprInt(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
