package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public enum BooleanLiteral implements Expression {

    TRUE,

    FALSE;

    @Override
    public WhileType type() {
        return WhileType.BOOLEAN;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBoolean(this);
    }

    public boolean getValue() {
        return this == TRUE;
    }
}
