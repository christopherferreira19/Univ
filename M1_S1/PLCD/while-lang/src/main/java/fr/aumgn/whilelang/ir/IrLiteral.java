package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class IrLiteral implements IrInstruction {

    private final IrVar var;
    private final IrLiteralValue literalValue;

    IrLiteral(IrVar var, IrLiteralValue literalValue) {
        this.var = var;
        this.literalValue = literalValue;
    }

    public IrVar getVar() {
        return var;
    }

    public IrLiteralValue getLiteralValue() {
        return literalValue;
    }

    @Override
    public List<IrVar> getVars() {
        return ImmutableList.of(var);
    }

    @Override
    public void asString(StringBuilder builder) {
        builder.append("LIT ");
        var.asString(builder);
        builder.append(' ');
        literalValue.asString(builder);
    }
}
