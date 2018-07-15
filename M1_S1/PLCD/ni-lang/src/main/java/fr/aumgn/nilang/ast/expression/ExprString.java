package fr.aumgn.nilang.ast.expression;

import fr.aumgn.nilang.ast.Expression;

public class ExprString implements Expression {

    private final String value;

    public ExprString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
