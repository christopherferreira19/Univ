package fr.aumgn.whilelang.ast;

public class Assignement implements Statement {

    private final BlockId blockId;
    private final String name;
    private final Ast value;

    Assignement(BlockId blockId, String name, Ast value) {
        this.blockId = blockId;
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAssignement(this);
    }

    public BlockId getBlockId() {
        return blockId;
    }

    public String getName() {
        return name;
    }

    public Ast getValue() {
        return value;
    }
}
