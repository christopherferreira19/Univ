package fr.aumgn.whilelang.cfg;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import fr.aumgn.whilelang.ir.*;

import java.util.List;
import java.util.Map;

public class IntermediateToCfg {

    public static WhileCfg create(WhileIr intermediate) {
        List<IrInstruction> instructions = intermediate.getInstructions();
        Map<IrLabel, Integer> labelsMap = intermediate.getLabelsMap();

        boolean[] tail = new boolean[instructions.size() + 1];
        tail[tail.length - 1] = true;

        for (int i = 0; i < instructions.size(); i++) {
            IrInstruction instruction = instructions.get(i);
            if (instruction instanceof IrJump) {
                IrLabel label = ((IrJump) instruction).getDest();
                int dest = labelsMap.get(label);

                tail[i + 1] = true;
                tail[dest] = true;
            }
        }

        int blockIndex = 0;
        int[] blocksMap = new int[instructions.size()];
        ImmutableList.Builder<BasicBlock> blocksBuilder = ImmutableList.builder();
        int currentFrom = 0;
        for (int i = 0; i < instructions.size(); i++) {
            if (tail[i + 1]) {
                BasicBlock block = new BasicBlock(blockIndex, instructions.subList(currentFrom, i + 1));
                blocksMap[currentFrom] = blockIndex;
                blocksBuilder.add(block);
                blockIndex++;

                currentFrom = i + 1;
            }
        }

        ImmutableList<BasicBlock> blocks = blocksBuilder.build();
        @SuppressWarnings({"unchecked", "rawtype"})
        ImmutableMap.Builder<String, BasicBlock>[] incoming =
                (ImmutableMap.Builder<String, BasicBlock>[]) new ImmutableMap.Builder[blocks.size()];
        @SuppressWarnings({"unchecked", "rawtype"})
        ImmutableMap.Builder<String, BasicBlock>[] outgoing =
                (ImmutableMap.Builder<String, BasicBlock>[]) new ImmutableMap.Builder[blocks.size()];
        for (int i = 0; i < blocks.size(); i++) {
            incoming[i] = ImmutableMap.builder();
            outgoing[i] = ImmutableMap.builder();
        }

        for (int i = 0; i < blocks.size(); i++) {
            BasicBlock block = blocks.get(i);
            boolean inconditionalJump = false;
            for (IrInstruction instruction : block.getInstructions()) {
                if (instruction instanceof IrJump) {
                    IrLabel label = ((IrJump) instruction).getDest();
                    int dest = labelsMap.get(label);

                    StringBuilder edgeLabelBuilder = new StringBuilder();
                    if (instruction instanceof IrJumpIf) {
                        ((IrJumpIf) instruction).getVar().asString(edgeLabelBuilder);
                    }

                    String edgeLabel = edgeLabelBuilder.toString();
                    incoming[blocksMap[dest]].put(edgeLabel, block);
                    outgoing[i].put(edgeLabel, blocks.get(blocksMap[dest]));

                    inconditionalJump = inconditionalJump || !(instruction instanceof IrJumpIf);
                }
            }

            if (!inconditionalJump && i + 1 < blocks.size()) {
                outgoing[i].put("", blocks.get(i + 1));
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            BasicBlock block = blocks.get(i);
            block.initIncomingOutgoing(incoming[i].build(), outgoing[i].build());
        }

        return new WhileCfg(blocks);
    }
}
