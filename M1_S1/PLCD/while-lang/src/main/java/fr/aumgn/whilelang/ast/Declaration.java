package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public class Declaration implements Ast {
    private final BlockId blockId;
    private final String name;
    private final WhileType whileType;

    public Declaration(BlockId blockId, String name, WhileType whileType) {
        this.blockId = blockId;
        this.name = name;
        this.whileType = whileType;
    }

    @Override
    public WhileType type() {
        return whileType;
    }

    public BlockId getBlockId() {
        return blockId;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitDeclaration(this);
    }
}
