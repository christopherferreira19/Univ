package fr.aumgn.whilelang.cfg;

import com.google.common.collect.ImmutableList;
import fr.aumgn.whilelang.ir.IrInstruction;
import fr.aumgn.whilelang.ir.IrJump;
import fr.aumgn.whilelang.ir.IrLabel;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class BasicBlock {

    private final int index;
    private final List<IrInstruction> instructions;
    private Map<String, BasicBlock> incoming;
    private Map<String, BasicBlock> outgoing;

    public BasicBlock(int index, List<IrInstruction> instructions) {
        this.index = index;
        this.instructions = instructions;
        this.incoming = null;
        this.outgoing = null;
    }

    void initIncomingOutgoing(Map<String, BasicBlock> incoming, Map<String, BasicBlock> outcoming) {
        checkState(this.incoming == null);
        this.incoming = incoming;
        this.outgoing = outcoming;
    }

    public int getIndex() {
        return index;
    }

    public List<IrInstruction> getInstructions() {
        return instructions;
    }

    public List<IrInstruction> getInstructionsWithoutLabelAndJump() {
        int from = 0;
        int to = instructions.size();

        while (from < to && instructions.get(from) instanceof IrLabel) from++;
        while (from < to && instructions.get(to - 1) instanceof IrJump) to--;

        return from == to
                ? ImmutableList.of()
                : instructions.subList(from, to);
    }

    public Map<String, BasicBlock> getIncoming() {
        checkState(incoming != null);
        return incoming;
    }

    public Map<String, BasicBlock> getOutgoing() {
        checkState(outgoing != null);
        return outgoing;
    }

    public String toStringWithoutLabelAndJump() {
        return IrInstruction.listToString(getInstructionsWithoutLabelAndJump());
    }

    @Override
    public String toString() {
        return IrInstruction.listToString(instructions);
    }
}
