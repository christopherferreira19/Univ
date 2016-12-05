package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class IrBinaryOp implements IrInstruction {

    private final IrBinaryOperator operator;
    private final IrVar to;
    private final IrVar left;
    private final IrVar right;

    IrBinaryOp(IrBinaryOperator operator, IrVar to, IrVar left, IrVar right) {
        this.operator = operator;
        this.to = to;
        this.left = left;
        this.right = right;
    }

    @Override
    public List<IrVar> getVars() {
        return ImmutableList.of(to, left, right);
    }

    public IrBinaryOperator getOperator() {
        return operator;
    }

    public IrVar getTo() {
        return to;
    }

    public IrVar getLeft() {
        return left;
    }

    public IrVar getRight() {
        return right;
    }

    @Override
    public void asString(StringBuilder builder) {
        operator.asString(builder);
        builder.append(' ');
        to.asString(builder);
        builder.append(' ');
        left.asString(builder);
        builder.append(' ');
        right.asString(builder);
    }
}
