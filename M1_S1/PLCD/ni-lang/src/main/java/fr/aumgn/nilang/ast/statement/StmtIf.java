package fr.aumgn.nilang.ast.statement;

import fr.aumgn.nilang.ast.Expression;
import fr.aumgn.nilang.ast.Statement;

public class StmtIf implements Statement {

    private final Expression condition;
    private final StmtBlock thenBody;
    private final StmtBlock elseBody;

    public StmtIf(Expression condition, StmtBlock thenBody, StmtBlock elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    public Expression getCondition() {
        return condition;
    }

    public StmtBlock getThenBody() {
        return thenBody;
    }

    public StmtBlock getElseBody() {
        return elseBody;
    }
}
