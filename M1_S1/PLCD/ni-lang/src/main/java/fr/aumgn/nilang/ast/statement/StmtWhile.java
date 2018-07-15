package fr.aumgn.nilang.ast.statement;

import fr.aumgn.nilang.ast.Expression;
import fr.aumgn.nilang.ast.Statement;

public class StmtWhile implements Statement {

    private final Expression condition;
    private final StmtBlock body;

    public StmtWhile(Expression condition, StmtBlock body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public StmtBlock getBody() {
        return body;
    }
}
