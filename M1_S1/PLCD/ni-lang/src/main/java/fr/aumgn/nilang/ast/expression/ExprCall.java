package fr.aumgn.nilang.ast.expression;

import com.google.common.collect.ImmutableList;
import fr.aumgn.nilang.ast.Expression;

public class ExprCall implements Expression {

    private final Expression fn;
    private final ImmutableList<Expression> parameters;

    public ExprCall(Expression fn, ImmutableList<Expression> parameters) {
        this.fn = fn;
        this.parameters = parameters;
    }

    public Expression getFunction() {
        return fn;
    }

    public ImmutableList<Expression> getParameters() {
        return parameters;
    }
}
