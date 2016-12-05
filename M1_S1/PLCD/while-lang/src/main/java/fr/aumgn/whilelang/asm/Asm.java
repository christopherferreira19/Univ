package fr.aumgn.whilelang.asm;

import com.google.common.io.CharSink;
import fr.aumgn.whilelang.ir.*;
import fr.aumgn.whilelang.utils.Counter;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Verify.verify;

public class Asm {

    public static void generate(WhileIr intermediate, CharSink sink) {
        try {
            doGenerate(intermediate, sink);
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private static void doGenerate(WhileIr intermediate, CharSink sink) throws IOException {
        Map<IrVar, Integer> variables = variables(intermediate);
        int frame_size = align(variables.size() * 8, 16);

        Writer writer = sink.openBufferedStream();

        writer.write("\t.section\t.rodata\n");
        writer.write(".msg:\n");
        writer.write("\t.string\t\"%ld\\n\"\n");
        writer.write("\t.text\n");
        writer.write("\t.globl\tmain\n");
        writer.write("main:\n");
        writer.write("\tpushq\t%rbp\n");
        writer.write("\tmovq\t%rsp, %rbp\n");
        writer.write("\tsubq\t$" + frame_size + ", %rsp\n");

        for (IrInstruction instruction : intermediate.getInstructions()) {
            writeInstruction(writer, variables, instruction);
        }

        writer.write("\tmovl\t$0, %eax\n");
        writer.write("\taddq\t$" + frame_size + ", %rsp\n");
        writer.write("\tpopq\t%rbp\n");
        writer.write("\tret\n");

        writer.close();
    }

    private static Map<IrVar, Integer> variables(WhileIr intermediate) {
        Map<IrVar, Integer> variables = new HashMap<>();

        Counter counter = new Counter();
        for (IrInstruction instruction : intermediate.getInstructions()) {
            for (IrVar var : instruction.getVars()) {
                variables.computeIfAbsent(var, (v) -> counter.next());
            }
        }
        return variables;
    }

    private static int align(int value, int alignment) {
        int surplus = value % alignment;
        if (surplus == 0) {
            return value;
        }

        return value + (alignment - surplus);
    }

    public static String var(int varIndex) {
        return "-" + (varIndex * 8) + "(%rbp)";
    }

    private static void instr(Writer writer, String mnemo, String... opde) throws IOException {
        writer.write("\t" + mnemo);
        writer.write("\t");
        writer.write(opde[0]);
        for (int i = 1; i < opde.length; i++) {
            writer.write(", ");
            writer.write(opde[i]);
        }
        writer.write("\n");
    }

    private static void writeInstruction(Writer writer, Map<IrVar, Integer> variables, IrInstruction instruction) throws IOException {
        if (instruction instanceof IrEnd) {
            // Nothing to do
        }
        else if (instruction instanceof IrLabel) {
            int number = ((IrLabel) instruction).getNumber();
            writer.write("l" + number + ":\n");
        }
        else if (instruction instanceof IrLiteral) {
            IrLiteral literal = ((IrLiteral) instruction);
            int varOffset = variables.get(literal.getVar());
            int value = literal.getLiteralValue().intValue();
            instr(writer, "movq", "$" + value, var(varOffset));
        }
        else if (instruction instanceof IrMove) {
            IrMove move = ((IrMove) instruction);
            int toOffset = variables.get(move.getTo());
            int fromOffset = variables.get(move.getFrom());
            instr(writer, "movq", var(fromOffset), "%rax");
            instr(writer, "movq", "%rax", var(toOffset));
        }
        else if (instruction instanceof IrJumpIf) {
            IrJumpIf jump = (IrJumpIf) instruction;
            int number = jump.getDest().getNumber();
            int varOffset = variables.get(jump.getVar());
            instr(writer, "movq", var(varOffset), "%rax");
            instr(writer, "testq", "%rax", "%rax");
            instr(writer, "jnz ", "l" + number);
        }
        else if (instruction instanceof IrJump) {
            IrJump jump = (IrJump) instruction;
            int number = jump.getDest().getNumber();
            instr(writer, "jmp ", "l" + number);
        }
        else if (instruction instanceof IrUnaryOp) {
            IrUnaryOp op = (IrUnaryOp) instruction;
            verify(op.getOperator() == IrUnaryOperator.NOT);
            int toIndex = variables.get(op.getTo());
            int opdeIndex = variables.get(op.getOpde());
            instr(writer, "movq", "$1", "%rax");
            instr(writer, "subq", var(opdeIndex), "%rax");
            instr(writer, "movq", "%rax", var(toIndex));
        }
        else if (instruction instanceof IrPrint) {
            IrPrint print = (IrPrint) instruction;
            int varIndex = variables.get(print.getVar());
            instr(writer, "movq", var(varIndex), "%rsi");
            instr(writer, "movl", "$.msg", "%edi");
            instr(writer, "call", "printf");
        }
        else if (instruction instanceof IrBinaryOp) {
            IrBinaryOp op = (IrBinaryOp) instruction;
            switch (op.getOperator()) {
                case OR:
                    simpleBinary(variables, op, "orq ", writer);
                    break;
                case AND:
                    simpleBinary(variables, op, "andq", writer);
                    break;
                case ADD:
                    simpleBinary(variables, op, "addq", writer);
                    break;
                case SUB:
                    simpleBinary(variables, op, "subq", writer);
                    break;
                case MUL:
                    break;
                case DIV:
                    break;
                case MOD:
                    break;

                case EQ:
                    cmpBinary(variables, op, "sete", writer);
                    break;
                case NE:
                    cmpBinary(variables, op, "setne", writer);
                    break;
                case LT:
                    cmpBinary(variables, op, "setl", writer);
                    break;
                case LE:
                    cmpBinary(variables, op, "setle", writer);
                    break;
                case GT:
                    cmpBinary(variables, op, "setg", writer);
                    break;
                case GE:
                    cmpBinary(variables, op, "setge", writer);
                    break;
            }
        }
        else {
            throw new RuntimeException("Unknown type : " + instruction.getClass());
        }
    }

    private static void simpleBinary(Map<IrVar, Integer> variables, IrBinaryOp op, String mnemo, Writer writer) throws IOException {
        int toIndex = variables.get(op.getTo());
        int leftIndex = variables.get(op.getLeft());
        int rightIndex = variables.get(op.getRight());
        instr(writer, "movq", var(leftIndex), "%rax");
        instr(writer, "movq", var(rightIndex), "%rbx");
        instr(writer, mnemo, "%rbx", "%rax");
        instr(writer, "movq", "%rax", var(toIndex));
    }

    private static void cmpBinary(Map<IrVar, Integer> variables, IrBinaryOp op, String mnemo, Writer writer) throws IOException {
        int toIndex = variables.get(op.getTo());
        int leftIndex = variables.get(op.getLeft());
        int rightIndex = variables.get(op.getRight());
        instr(writer, "movq", var(leftIndex), "%rax");
        instr(writer, "movq", var(rightIndex), "%rbx");
        instr(writer, "cmp ", "%rbx", "%rax");
        instr(writer, "movq", "$0", "%rax");
        instr(writer, mnemo, "%al");
        instr(writer, "movq", "%rax", var(toIndex));
    }
}

