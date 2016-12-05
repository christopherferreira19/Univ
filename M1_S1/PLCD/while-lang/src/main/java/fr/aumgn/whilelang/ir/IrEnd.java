package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;

import java.util.List;

public enum IrEnd implements IrInstruction {

    INSTANCE;

    @Override
    public List<IrVar> getVars() {
        return ImmutableList.of();
    }

    @Override
    public void asString(StringBuilder builder) {
        builder.append("END");
    }
}
