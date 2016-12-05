package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class IrJumpIf extends IrJump {

    private final IrVar var;

    IrJumpIf(IrLabel dest, IrVar var) {
        super(dest);
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
        builder.append("JIF ");
        dest.asString(builder);
        builder.append(' ');
        var.asString(builder);
    }
}
