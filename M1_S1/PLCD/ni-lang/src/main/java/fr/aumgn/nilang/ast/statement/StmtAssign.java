package fr.aumgn.nilang.ast.statement;

import fr.aumgn.nilang.ast.Expression;
import fr.aumgn.nilang.ast.Statement;

public class StmtAssign implements Statement {

    private final Expression expression;

    public StmtAssign(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
