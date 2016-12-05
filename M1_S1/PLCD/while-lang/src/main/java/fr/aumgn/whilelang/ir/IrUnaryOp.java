package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class IrUnaryOp implements IrInstruction {

    private final IrUnaryOperator operator;
    private final IrVar to;
    private final IrVar opde;

    IrUnaryOp(IrUnaryOperator operator, IrVar to, IrVar opde) {
        this.operator = operator;
        this.to = to;
        this.opde = opde;
    }

    @Override
    public List<IrVar> getVars() {
        return ImmutableList.of(to, opde);
    }

    public IrUnaryOperator getOperator() {
        return operator;
    }

    public IrVar getTo() {
        return to;
    }

    public IrVar getOpde() {
        return opde;
    }

    @Override
    public void asString(StringBuilder builder) {
        operator.asString(builder);
        builder.append(' ');
        to.asString(builder);
        builder.append(' ');
        opde.asString(builder);
    }
}
