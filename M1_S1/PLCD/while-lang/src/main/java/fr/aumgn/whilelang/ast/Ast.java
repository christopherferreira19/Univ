package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public interface Ast {

    WhileType type();

    <T> T accept(AstVisitor<T> visitor);
}
