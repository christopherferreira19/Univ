package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class IrJump implements IrInstruction {

    final IrLabel dest;

    IrJump(IrLabel dest) {
        this.dest = dest;
    }

    @Override
    public List<IrVar> getVars() {
        return ImmutableList.of();
    }

    public IrLabel getDest() {
        return dest;
    }

    @Override
    public void asString(StringBuilder builder) {
        builder.append("JMP ");
        dest.asString(builder);
    }
}
