package fr.aumgn.whilelang.ast;

public class WhileStatement implements Statement {

    private final Ast condition;
    private final Block body;

    WhileStatement(Ast condition, Block body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitWhileStatement(this);
    }

    public Ast getCondition() {
        return condition;
    }

    public Block getBody() {
        return body;
    }
}
