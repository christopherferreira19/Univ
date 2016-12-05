package fr.aumgn.whilelang.ast;

import java.util.List;

public class Block implements Statement {

    private final BlockId blockId;
    private final List<Declaration> declarations;
    private final List<Ast> statements;

    Block(List<Declaration> declarations, BlockId blockId, List<Ast> statements) {
        this.declarations = declarations;
        this.blockId = blockId;
        this.statements = statements;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBlock(this);
    }

    public BlockId getId() {
        return blockId;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public List<Ast> getStatements() {
        return statements;
    }
}
