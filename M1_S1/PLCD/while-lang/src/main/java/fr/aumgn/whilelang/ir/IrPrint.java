package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class IrPrint implements IrInstruction {

    private final IrVar var;

    public IrPrint(IrVar var) {
        this.var = var;
    }

    @Override
    public List<IrVar> getVars() {
        return ImmutableList.of(var);
    }

    public IrVar getVar() {
        return var;
    }

    @Override
    public void asString(StringBuilder builder) {
        builder.append("PRNT ");
        var.asString(builder);
    }
}
