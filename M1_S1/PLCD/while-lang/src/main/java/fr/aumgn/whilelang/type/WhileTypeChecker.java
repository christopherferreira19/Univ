package fr.aumgn.whilelang.type;

import fr.aumgn.whilelang.ast.*;

public class WhileTypeChecker implements AstVisitor<Void> {

    private SymbolTable<WhileType> symbolTable = new SymbolTable<>();

    private void verify(WhileType expected, Ast actual) {
        visit(actual);
        verify(expected, actual.type());
    }

    private void verify(WhileType expected, WhileType actual) {
        if (expected != actual) {
            throw new RuntimeException("Expected " + expected + ", got " + actual);
        }
    }

    @Override
    public Void visitBlock(Block block) {
        block.getDeclarations().forEach(this::visit);
        block.getStatements().forEach(this::visit);
        return null;
    }

    @Override
    public Void visitDeclaration(Declaration declaration) {
        symbolTable.insert(declaration.getBlockId(), declaration.getName(), declaration.type());
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement ifStatement) {
        verify(WhileType.BOOLEAN, ifStatement.getCondition());

        visit(ifStatement.getThenBody());
        visit(ifStatement.getElseBody());

        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement whileStatement) {
        verify(WhileType.BOOLEAN, whileStatement.getCondition());
        visit(whileStatement.getBody());
        return null;
    }

    @Override
    public Void visitAssignement(Assignement assignement) {
        WhileType expected = symbolTable.lookUp(assignement.getBlockId(), assignement.getName());
        verify(expected, assignement.getValue());
        return null;
    }

    @Override
    public Void visitPrint(Print print) {
        visit(print.getArgument());
        return null;
    }

    @Override
    public Void visitBinaryOp(BinaryOp op) {
        switch (op.getOperator()) {
            case EQUAL_TO:
            case DIFFERENT:
                visit(op.getLeft());
                verify(op.getLeft().type(), op.getRight());
                break;
            case OR:
            case AND:
                verify(WhileType.BOOLEAN, op.getLeft());
                verify(WhileType.BOOLEAN, op.getRight());
                break;
            case LESS_THAN:
            case LESS_EQUAL_TO:
            case GREATER_THAN:
            case GREATER_EQUAL_TO:
                verify(WhileType.INTEGER, op.getLeft());
                verify(WhileType.INTEGER, op.getRight());
                break;
            case PLUS:
            case MINUS:
            case TIMES:
            case DIVIDE:
            case MODULO:
                verify(WhileType.INTEGER, op.getLeft());
                verify(WhileType.INTEGER, op.getRight());
                break;
            default:
                throw new RuntimeException();
        }

        return null;
    }

    @Override
    public Void visitUnaryOp(UnaryOp op) {
        switch (op.getOperator()) {
            case NOT:
                verify(WhileType.BOOLEAN, op.getAst());
            case MINUS:
            case PLUS:
                verify(WhileType.INTEGER, op.getAst());
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public Void visitIdentifier(Identifier identifier) {
        identifier.initType(symbolTable.lookUp(identifier.getBlockId(), identifier.getName()));
        return null;
    }

    @Override
    public Void visitNumeral(Numeral numeral) {
        return null;
    }

    @Override
    public Void visitBoolean(BooleanLiteral booleanLiteral) {
        return null;
    }
}
