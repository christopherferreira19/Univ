package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public class BinaryOp implements Expression {

    private final BinaryOperator operator;
    private final Ast left;
    private final Ast right;

    BinaryOp(BinaryOperator operator, Ast left, Ast right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public WhileType type() {
        return operator.resultType();
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBinaryOp(this);
    }

    public BinaryOperator getOperator() {
        return operator;
    }

    public Ast getLeft() {
        return left;
    }

    public Ast getRight() {
        return right;
    }
}
