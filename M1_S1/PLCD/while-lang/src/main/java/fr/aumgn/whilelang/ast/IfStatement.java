package fr.aumgn.whilelang.ast;

public class IfStatement implements Statement {

    private final Ast condition;
    private final Block thenBody;
    private final Block elseBody;

    IfStatement(Ast condition, Block thenBody, Block elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIfStatement(this);
    }

    public Ast getCondition() {
        return condition;
    }

    public Block getThenBody() {
        return thenBody;
    }

    public Block getElseBody() {
        return elseBody;
    }
}
