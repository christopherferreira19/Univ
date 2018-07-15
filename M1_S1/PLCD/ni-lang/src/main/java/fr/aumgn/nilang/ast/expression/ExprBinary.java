package fr.aumgn.nilang.ast.expression;

import fr.aumgn.nilang.ast.Expression;

public class ExprBinary implements Expression {

    public enum Operator {

        PLUS,
        MINUS,
        TIMES,
        DIVIDE,
        MODULO,

        LESS_THAN,
        LESS_EQUAL,
        GREATER_THAN,
        GREATER_EQUAL,
        EQUAL,
        DIFFERENT,

        AND,
        OR
    }

    private final Operator operator;
    private final Expression left;
    private final Expression right;

    public ExprBinary(Operator operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}
