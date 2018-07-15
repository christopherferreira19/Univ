package fr.aumgn.nilang.ast.expression;

import com.google.common.collect.ImmutableList;
import fr.aumgn.nilang.ast.Expression;

public class Expressions {

    public static ExprString string(String str) {
        return new ExprString(str);
    }

    public static ExprTuple tuple(Iterable<Expression> expressions) {
        return new ExprTuple(ImmutableList.copyOf(expressions));
    }

}
