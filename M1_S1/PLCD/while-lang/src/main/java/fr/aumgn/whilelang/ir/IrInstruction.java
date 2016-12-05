package fr.aumgn.whilelang.ir;

import java.util.List;

public interface IrInstruction {

    List<IrVar> getVars();

    void asString(StringBuilder builder);

    static String listToString(List<IrInstruction> instructions) {
        StringBuilder builder = new StringBuilder();
        for (IrInstruction instruction : instructions) {
            instruction.asString(builder);
            builder.append('\n');
        }
        return builder.toString();
    }
}
