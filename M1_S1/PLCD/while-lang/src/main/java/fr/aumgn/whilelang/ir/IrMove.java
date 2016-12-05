package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class IrMove implements IrInstruction {

    private final IrVar to;
    private final IrVar from;

    IrMove(IrVar to, IrVar from) {
        this.to = to;
        this.from = from;
    }

    @Override
    public List<IrVar> getVars() {
        return ImmutableList.of(to, from);
    }

    public IrVar getTo() {
        return to;
    }

    public IrVar getFrom() {
        return from;
    }

    @Override
    public void asString(StringBuilder builder) {
        builder.append("MOV ");
        to.asString(builder);
        builder.append(' ');
        from.asString(builder);
    }
}
