package fr.aumgn.nilang.ast.expression;

import fr.aumgn.nilang.ast.Expression;

public class ExprFloat implements Expression {

    private final double value;

    public ExprFloat(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
