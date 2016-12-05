package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.type.WhileType;

public class Identifier implements Expression {

    private final BlockId blockId;
    private final String name;
    private WhileType type;

    Identifier(BlockId blockId, String name) {
        this.blockId = blockId;
        this.name = name;
    }

    @Override
    public WhileType type() {
        return type;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIdentifier(this);
    }

    public BlockId getBlockId() {
        return blockId;
    }

    public String getName() {
        return name;
    }

    public void initType(WhileType type) {
        this.type = type;
    }
}
