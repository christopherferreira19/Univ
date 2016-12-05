package fr.aumgn.whilelang.ir;

import fr.aumgn.whilelang.ast.BlockId;
import fr.aumgn.whilelang.utils.Generator;

import java.util.Objects;

public interface IrVar {

    static IrVar of(BlockId blockId, String name) {
        return new UserIrVar(blockId, name);
    }

    static Generator<IrVar> generator() {
        return new Generator<>(TempIrVar::new);
    }

    void asString(StringBuilder builder);

}

class UserIrVar implements IrVar {

    private final BlockId blockId;
    private final String name;

    public UserIrVar(BlockId blockId, String name) {
        this.blockId = blockId;
        this.name = name;
    }

    @Override
    public void asString(StringBuilder builder) {
        builder.append(name);
        if (!blockId.isEmpty()) {
            builder.append('{');
            blockId.asString(builder);
            builder.append('}');
        }
    }

    @Override
    public int hashCode() {
        return (Objects.hash(true, blockId, name) << 1) + 1;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof UserIrVar)) {
            return false;
        }

        UserIrVar that = (UserIrVar) object;
        return this.blockId.equals(that.blockId)
                && this.name.equals(that.name);
    }
}

class TempIrVar implements IrVar {

    private final int number;

    public TempIrVar(int number) {
        this.number = number;
    }

    @Override
    public void asString(StringBuilder builder) {
        builder.append('%').append(number);
    }

    @Override
    public int hashCode() {
        return number << 1;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof TempIrVar)) {
            return false;
        }

        TempIrVar that = (TempIrVar) object;
        return this.number == that.number;
    }
}
