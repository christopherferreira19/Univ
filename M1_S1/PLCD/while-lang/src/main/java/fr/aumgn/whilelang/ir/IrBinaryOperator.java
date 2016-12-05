package fr.aumgn.whilelang.ir;

import fr.aumgn.whilelang.ast.BinaryOperator;

public enum IrBinaryOperator {

    OR,
    AND,

    EQ,
    NE,

    LT,
    LE,
    GT,
    GE,

    ADD,
    SUB,
    MUL,
    DIV,
    MOD;

    void asString(StringBuilder builder) {
        builder.append(name());
    }

    public static IrBinaryOperator of(BinaryOperator operator) {
        switch (operator) {
            case OR: return OR;
            case AND: return AND;
            case EQUAL_TO: return EQ;
            case DIFFERENT: return NE;
            case LESS_THAN: return LT;
            case LESS_EQUAL_TO: return LE;
            case GREATER_THAN: return GT;
            case GREATER_EQUAL_TO: return GE;
            case PLUS: return ADD;
            case MINUS: return SUB;
            case TIMES: return MUL;
            case DIVIDE: return DIV;
            case MODULO: return MOD;
            default: throw new RuntimeException("Unknown operator : '" + operator + "'");
        }
    }
}
