package fr.aumgn.whilelang.ir;

import fr.aumgn.whilelang.ast.Ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhileIr {

    private final List<IrInstruction> instructions;
    private final Map<IrLabel, Integer> labelsMap;

    WhileIr(List<IrInstruction> instructions) {
        this.instructions = instructions;
        this.labelsMap = new HashMap<>();
        for (int index = 0; index < instructions.size(); index++) {
            IrInstruction instruction = instructions.get(index);
            if (instruction instanceof IrLabel) {
                labelsMap.put((IrLabel) instruction, index);
            }
        }
    }

    public List<IrInstruction> getInstructions() {
        return instructions;
    }

    public Map<IrLabel, Integer> getLabelsMap() {
        return labelsMap;
    }

    @Override
    public String toString() {
        return IrInstruction.listToString(instructions);
    }

    public static WhileIr of(Ast ast) {
        WhileIrAstVisitor visitor = new WhileIrAstVisitor();
        visitor.visit(ast);
        visitor.builder.add(IrEnd.INSTANCE);
        return new WhileIr(visitor.builder.build());
    }
}
