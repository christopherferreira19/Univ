package fr.aumgn.nilang.ast.statement;

import fr.aumgn.nilang.ast.Expression;
import fr.aumgn.nilang.ast.Statement;

public class StmtReturn implements Statement {

    private final Expression expression;

    public StmtReturn(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
