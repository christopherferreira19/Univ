package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.parser.WhileLangBaseVisitor;
import fr.aumgn.whilelang.parser.WhileLangParser;
import fr.aumgn.whilelang.type.WhileType;
import fr.aumgn.whilelang.utils.Counter;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class WhileParseTreeToAst extends WhileLangBaseVisitor<Ast> {

    private SymbolTable<WhileType> symbolTable;
    private BlockId blockId;
    private Counter blockCounter;

    public WhileParseTreeToAst() {
        this.symbolTable = null;
        this.blockId = null;
        this.blockCounter = null;
    }

    @Override
    public Ast visitProgram(WhileLangParser.ProgramContext ctx) {
        this.symbolTable = new SymbolTable<>();
        Ast block = visit(ctx.block());
        this.symbolTable = null;
        return block;
    }

    @Override
    public Ast visitBlock(WhileLangParser.BlockContext ctx) {
        BlockId previousBlockId = blockId;
        Counter previousBlockCounter = blockCounter;
        this.blockId = new BlockId(blockId, blockCounter);
        this.blockCounter = new Counter();

        List<Declaration> declarations = new ArrayList<>();
        for (WhileLangParser.DeclarationContext declarationContext : ctx.declaration()) {
            declarations.add((Declaration) visit(declarationContext));
        }

        List<Ast> statements = new ArrayList<>();
        for (WhileLangParser.DeclarationContext declaration : ctx.declaration()) {
            if (declaration instanceof WhileLangParser.DefinitionContext) {
                WhileLangParser.DefinitionContext definition = (WhileLangParser.DefinitionContext) declaration;
                String identifier = definition.Identifier().getText();
                Expression value = (Expression) visit(definition.expression());
                statements.add(new Assignement(blockId, identifier, value));
            }
        }

        for (WhileLangParser.StatementContext statement : ctx.statement()) {
            statements.add(visit(statement));
        }

        BlockId thisBlockId = blockId;
        this.blockId = previousBlockId;
        this.blockCounter = previousBlockCounter;
        return new Block(declarations, thisBlockId, statements);
    }

    @Override
    public Ast visitDeclarationType(WhileLangParser.DeclarationTypeContext ctx) {
        String identifier = ctx.Identifier().getText();
        String type = ctx.Type().getText();
        symbolTable.insert(blockId, identifier, WhileType.ofString(type));
        return new Declaration(blockId, identifier, WhileType.ofString(type));
    }

    @Override
    public Ast visitDefinition(WhileLangParser.DefinitionContext ctx) {
        String identifier = ctx.Identifier().getText();
        Expression value = (Expression) visit(ctx.expression());
        symbolTable.insert(blockId, identifier, value.type());
        return new Declaration(blockId, identifier, value.type());
    }

    @Override
    public Ast visitIf(WhileLangParser.IfContext ctx) {
        Ast condition = visit(ctx.expression());
        Block thenBody = (Block) visit(ctx.thenBody);
        Block elseBody = (Block) visit(ctx.elseBody);
        return new IfStatement(condition, thenBody, elseBody);
    }

    @Override
    public Ast visitWhile(WhileLangParser.WhileContext ctx) {
        Ast condition = visit(ctx.expression());
        Block body = (Block) visit(ctx.block());
        return new WhileStatement(condition, body);
    }

    @Override
    public Ast visitAssignement(WhileLangParser.AssignementContext ctx) {
        String name = ctx.Identifier().getText();
        BlockId blockId = symbolTable.findBlockId(this.blockId, name);
        Ast value = visit(ctx.expression());
        return new Assignement(blockId, name, value);
    }

    @Override
    public Ast visitPrint(WhileLangParser.PrintContext ctx) {
        Ast argument = visit(ctx.expression());
        return new Print(argument);
    }

    private BinaryOperator op(Token opToken) {
        switch (opToken.getText()) {
            case "or": return BinaryOperator.OR;
            case "and": return BinaryOperator.AND;
            case "==": return BinaryOperator.EQUAL_TO;
            case "!=": return BinaryOperator.DIFFERENT;
            case "<": return BinaryOperator.LESS_THAN;
            case "<=": return BinaryOperator.LESS_EQUAL_TO;
            case ">": return BinaryOperator.GREATER_THAN;
            case ">=": return BinaryOperator.GREATER_EQUAL_TO;
            case "+": return BinaryOperator.PLUS;
            case "-": return BinaryOperator.MINUS;
            case "*": return BinaryOperator.TIMES;
            case "/": return BinaryOperator.DIVIDE;
            case "%": return BinaryOperator.MODULO;
            default: throw new RuntimeException();
        }
    }

    private Ast binaryOp(Token opToken,
                         WhileLangParser.ExpressionContext leftToken,
                         WhileLangParser.ExpressionContext rightToken) {
        BinaryOperator operator = op(opToken);
        Ast left = visit(leftToken);
        Ast right = visit(rightToken);
        return new BinaryOp(operator, left, right);
    }

    @Override
    public Ast visitMultiplication(WhileLangParser.MultiplicationContext ctx) {
        return binaryOp(ctx.op, ctx.left, ctx.right);
    }

    @Override
    public Ast visitAddition(WhileLangParser.AdditionContext ctx) {
        return binaryOp(ctx.op, ctx.left, ctx.right);
    }

    @Override
    public Ast visitComparison(WhileLangParser.ComparisonContext ctx) {
        BinaryOperator operator = op(ctx.op);
        Ast left = visit(ctx.left);
        Ast right = visit(ctx.right);
        return new BinaryOp(operator, left, right);
    }

    @Override
    public Ast visitEquality(WhileLangParser.EqualityContext ctx) {
        return binaryOp(ctx.op, ctx.left, ctx.right);
    }

    @Override
    public Ast visitDisjunction(WhileLangParser.DisjunctionContext ctx) {
        return binaryOp(ctx.op, ctx.left, ctx.right);
    }

    @Override
    public Ast visitConjunction(WhileLangParser.ConjunctionContext ctx) {
        return binaryOp(ctx.op, ctx.left, ctx.right);
    }

    @Override
    public Ast visitUnaryMinus(WhileLangParser.UnaryMinusContext ctx) {
        return new UnaryOp(UnaryOperator.MINUS, visit(ctx.expression_operande()));
    }

    @Override
    public Ast visitUnaryPlus(WhileLangParser.UnaryPlusContext ctx) {
        return new UnaryOp(UnaryOperator.PLUS, visit(ctx.expression_operande()));
    }

    @Override
    public Ast visitUnaryNot(WhileLangParser.UnaryNotContext ctx) {
        return new UnaryOp(UnaryOperator.NOT, visit(ctx.expression_operande()));
    }

    @Override
    public Ast visitIdentifier(WhileLangParser.IdentifierContext ctx) {
        String name = ctx.getText();
        BlockId blockId = symbolTable.findBlockId(this.blockId, name);
        return new Identifier(blockId, name);
    }

    @Override
    public Ast visitBoolean(WhileLangParser.BooleanContext ctx) {
        return ctx.getText().equals("true") ? BooleanLiteral.TRUE : BooleanLiteral.FALSE;
    }

    @Override
    public Ast visitNumber(WhileLangParser.NumberContext ctx) {
        return new Numeral(Integer.parseInt(ctx.getText()));
    }
}
