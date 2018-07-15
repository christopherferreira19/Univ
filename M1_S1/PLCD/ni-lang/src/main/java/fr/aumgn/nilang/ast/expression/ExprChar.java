package fr.aumgn.nilang.ast.expression;

import fr.aumgn.nilang.ast.Expression;

public class ExprChar implements Expression {

    private final char value;

    public ExprChar(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
