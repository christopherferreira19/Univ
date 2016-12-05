package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;
import fr.aumgn.whilelang.utils.Generator;

import java.util.List;

public class IrLabel implements IrInstruction {

    private final int number;

    IrLabel(int number) {
        this.number = number;
    }

    @Override
    public List<IrVar> getVars() {
        return ImmutableList.of();
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof IrLabel)) {
            return false;
        }

        IrLabel that = (IrLabel) object;
        return this.number == that.number;
    }

    @Override
    public void asString(StringBuilder builder) {
        builder.append('l').append(number).append(':');
    }

    public static Generator<IrLabel> generator() {
        return new Generator<>(IrLabel::new);
    }
}
