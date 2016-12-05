package fr.aumgn.whilelang.ir;

import com.google.common.collect.ImmutableList;
import fr.aumgn.whilelang.ast.*;
import fr.aumgn.whilelang.ast.BinaryOp;
import fr.aumgn.whilelang.ast.UnaryOp;
import fr.aumgn.whilelang.utils.Generator;

import java.util.List;

class WhileIrAstVisitor implements AstVisitor<Object> {

    final ImmutableList.Builder<IrInstruction> builder;

    private final Generator<IrLabel> labels;
    private final Generator<IrVar> vars;
    private final SymbolTable<IrVar> idToVar;

    private IrVar assign;

    WhileIrAstVisitor() {
        this.labels = IrLabel.generator();
        this.vars = IrVar.generator();
        this.idToVar = new SymbolTable<>();
        this.builder = ImmutableList.builder();
    }

    @Override
    public Object visitBlock(Block block) {
        block.getDeclarations().forEach(this::visit);
        block.getStatements().forEach(this::visit);
        return null;
    }

    @Override
    public Object visitDeclaration(Declaration declaration) {
        IrVar var = IrVar.of(declaration.getBlockId(), declaration.getName());
        idToVar.insert(declaration.getBlockId(), declaration.getName(), var);
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement) {
        IrLabel ltrue = labels.generate();
        IrLabel lend = labels.generate();

        IrVar cond = (IrVar) visit(ifStatement.getCondition());
        builder.add(new IrJumpIf(ltrue, cond));
        visit(ifStatement.getElseBody());
        builder.add(new IrJump(lend));
        builder.add(ltrue);
        visit(ifStatement.getThenBody());
        builder.add(lend);
        return null;
    }

    @Override
    public Object visitWhileStatement(WhileStatement whileStatement) {
        IrLabel lcond = labels.generate();
        IrLabel lbody = labels.generate();

        builder.add(new IrJump(lcond));
        builder.add(lbody);
        visit(whileStatement.getBody());
        builder.add(lcond);
        IrVar cond = (IrVar) visit(whileStatement.getCondition());
        builder.add(new IrJumpIf(lbody, cond));
        return null;
    }

    @Override
    public Object visitAssignement(Assignement assignement) {
        this.assign = idToVar.lookUp(assignement.getBlockId(), assignement.getName());
        assignement.getValue().accept(this);
        return null;
    }

    @Override
    public Object visitPrint(Print print) {
        IrVar var = (IrVar) visit(print.getArgument());
        builder.add(new IrPrint(var));
        return null;
    }

    private IrVar assignOrGenerate() {
        if (assign == null) {
            return vars.generate();
        }

        IrVar var = assign;
        assign = null;
        return var;
    }

    @Override
    public Object visitBinaryOp(BinaryOp op) {
        IrBinaryOperator operator = IrBinaryOperator.of(op.getOperator());
        IrVar to = assignOrGenerate();

        IrVar left = (IrVar) op.getLeft().accept(this);
        IrVar right = (IrVar) op.getRight().accept(this);
        builder.add(new IrBinaryOp(operator, to, left, right));
        return to;
    }

    @Override
    public Object visitUnaryOp(UnaryOp op) {
        switch (op.getOperator()) {
            case PLUS:
                return op.getAst().accept(this);
            case MINUS: {
                IrVar to = assignOrGenerate();
                IrVar zero = vars.generate();
                builder.add(new IrLiteral(zero, IrLiteralValue.ZERO));

                IrVar opde = (IrVar) op.getAst().accept(this);
                builder.add(new IrBinaryOp(IrBinaryOperator.SUB, to, zero, opde));
                return to;
            }
        }

        IrVar to = assignOrGenerate();
        IrUnaryOperator operator = IrUnaryOperator.of(op.getOperator());
        IrVar opde = (IrVar) op.getAst().accept(this);
        builder.add(new IrUnaryOp(operator, to, opde));
        return to;
    }

    @Override
    public Object visitIdentifier(Identifier identifier) {
        IrVar var = idToVar.lookUp(identifier.getBlockId(), identifier.getName());
        if (var == null) {
            throw new RuntimeException("Unknown identifier '" + identifier.getName() + "'");
        }

        if (assign == null) {
            return var;
        }

        IrVar to = assign;
        assign = null;
        builder.add(new IrMove(to, var));
        return assign;
    }

    @Override
    public Object visitNumeral(Numeral numeral) {
        IrVar to = assignOrGenerate();
        builder.add(new IrLiteral(to, new IrLiteralValue.Numeral(numeral.getValue())));
        return to;
    }

    @Override
    public Object visitBoolean(BooleanLiteral literal) {
        IrVar to = assignOrGenerate();
        builder.add(new IrLiteral(to, IrLiteralValue.Boolean.of(literal)));
        return to;
    }
}
