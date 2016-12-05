package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public class UnaryOp implements Expression {

    private final UnaryOperator operator;
    private final Ast ast;

    UnaryOp(UnaryOperator operator, Ast ast) {
        this.operator = operator;
        this.ast = ast;
    }

    @Override
    public WhileType type() {
        return operator.resultType();
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitUnaryOp(this);
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public Ast getAst() {
        return ast;
    }
}
