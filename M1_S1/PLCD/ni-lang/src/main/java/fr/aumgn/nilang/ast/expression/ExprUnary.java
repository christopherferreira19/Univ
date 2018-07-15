package fr.aumgn.nilang.ast.expression;

import fr.aumgn.nilang.ast.Expression;

public class ExprUnary implements Expression {

    public enum Operator {

        PLUS,
        MINUS,

        NOT
    }

    private final Operator operator;
    private final Expression operande;

    public ExprUnary(Operator operator, Expression operande) {
        this.operator = operator;
        this.operande = operande;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getOperande() {
        return operande;
    }
}
