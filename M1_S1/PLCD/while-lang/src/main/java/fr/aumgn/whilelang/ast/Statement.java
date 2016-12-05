package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public interface Statement extends Ast {

    default WhileType type() {
        return WhileType.UNIT;
    }
}
