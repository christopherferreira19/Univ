package fr.aumgn.nilang.ast.expression;

import com.google.common.collect.ImmutableList;
import fr.aumgn.nilang.ast.Expression;

public class ExprTuple implements Expression {

    private final ImmutableList<Expression> expressions;

    public ExprTuple(ImmutableList<Expression> expressions) {
        this.expressions = expressions;
    }

    public ImmutableList<Expression> getExpressions() {
        return expressions;
    }
}
