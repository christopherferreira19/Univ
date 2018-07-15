package fr.aumgn.nilang.parser;

import com.google.common.collect.ImmutableList;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import fr.aumgn.nilang.ast.Ast;
import fr.aumgn.nilang.ast.Expression;
import fr.aumgn.nilang.ast.Program;
import fr.aumgn.nilang.ast.Statement;
import fr.aumgn.nilang.ast.expression.*;
import fr.aumgn.nilang.ast.statement.*;
import org.antlr.v4.runtime.Token;

class ParseTreeToAst extends NiLangBaseVisitor<Ast> {

    @Override
    public Ast visitNi(NiLangParser.NiContext ctx) {
        ImmutableList.Builder<Statement> statements = ImmutableList.builder();
        for (NiLangParser.StatementContext statementContext : ctx.statements().statement()) {
            statements.add(visitStatement(statementContext));
        }

        return new Program(statements.build());
    }

    public Expression visitExpression(NiLangParser.AtomContext ctx) {
        return (Expression) visit(ctx);
    }

    public Expression visitExpression(NiLangParser.ExpressionContext ctx) {
        return (Expression) visit(ctx);
    }

    public Statement visitStatement(NiLangParser.StatementContext ctx) {
        return (Statement) visit(ctx);
    }

    @Override
    public ExprBool visitTrue(NiLangParser.TrueContext ctx) {
        return ExprBool.TRUE;
    }

    @Override
    public ExprBool visitFalse(NiLangParser.FalseContext ctx) {
        return ExprBool.FALSE;
    }

    @Override
    public ExprChar visitChar(NiLangParser.CharContext ctx) {
        char value = ctx.CHAR().getSymbol().getText().charAt(1);
        return new ExprChar(value);
    }

    @Override
    public ExprInt visitInteger(NiLangParser.IntegerContext ctx) {
        long value = Long.parseLong(ctx.INTEGER().getSymbol().getText());
        return new ExprInt(value);
    }

    @Override
    public ExprFloat visitFloat(NiLangParser.FloatContext ctx) {
        double value = Double.parseDouble(ctx.FLOAT().getSymbol().getText());
        return new ExprFloat(value);
    }

    @Override
    public ExprIdentifier visitVariable(NiLangParser.VariableContext ctx) {
        return new ExprIdentifier();
    }

    @Override
    public Expression visitTuple(NiLangParser.TupleContext ctx) {
        if (ctx.expressions().expression().size() == 1) {
            return visitExpression(ctx.expressions().expression(0));
        }

        ImmutableList.Builder<Expression> expressions = ImmutableList.builder();
        for (NiLangParser.ExpressionContext expressionContext : ctx.expressions().expression()) {
            expressions.add(visitExpression(expressionContext));
        }
        return new ExprTuple(expressions.build());
    }

    @Override
    public ExprCall visitCall(NiLangParser.CallContext ctx) {
        Expression fn = visitExpression(ctx.fn);
        ImmutableList.Builder<Expression> parameters = ImmutableList.builder();
        for (NiLangParser.ExpressionContext expressionContext : ctx.expressions().expression()) {
            parameters.add(visitExpression(expressionContext));
        }
        return new ExprCall(fn, parameters.build());
    }

    @Override
    public ExprFn visitFnExpression(NiLangParser.FnExpressionContext ctx) {
        return new ExprFn();
    }

    private ExprUnary.Operator unaryOperator(Token token) {
        switch (token.getType()) {
            case NiLangParser.NOT: return ExprUnary.Operator.NOT;
            case NiLangParser.PLUS: return ExprUnary.Operator.PLUS;
            case NiLangParser.MINUS: return ExprUnary.Operator.MINUS;
            default: throw new RuntimeException();
        }
    }

    private ExprBinary.Operator binaryOperator(Token token) {
        switch (token.getType()) {
            case NiLangParser.AND: return ExprBinary.Operator.AND;
            case NiLangParser.OR: return ExprBinary.Operator.OR;
            case NiLangParser.EQUAL: return ExprBinary.Operator.EQUAL;
            case NiLangParser.DIFFERENT: return ExprBinary.Operator.DIFFERENT;
            case NiLangParser.LESS_THAN: return ExprBinary.Operator.LESS_THAN;
            case NiLangParser.LESS_EQUAL: return ExprBinary.Operator.LESS_EQUAL;
            case NiLangParser.GREATER_THAN: return ExprBinary.Operator.GREATER_THAN;
            case NiLangParser.GREATER_EQUAL: return ExprBinary.Operator.GREATER_EQUAL;
            case NiLangParser.PLUS: return ExprBinary.Operator.PLUS;
            case NiLangParser.MINUS: return ExprBinary.Operator.MINUS;
            case NiLangParser.TIMES: return ExprBinary.Operator.TIMES;
            case NiLangParser.DIVIDE: return ExprBinary.Operator.DIVIDE;
            case NiLangParser.MODULO: return ExprBinary.Operator.MODULO;
            default: throw new RuntimeException();
        }
    }

    @Override
    public ExprUnary visitUnary(NiLangParser.UnaryContext ctx) {
        ExprUnary.Operator operator = unaryOperator(ctx.operator);
        Expression operande = visitExpression(ctx.operande);
        return new ExprUnary(operator, operande);
    }

    @Override
    public ExprBinary visitBinary(NiLangParser.BinaryContext ctx) {
        ExprBinary.Operator operator = binaryOperator(ctx.operator);
        Expression left = visitExpression(ctx.left);
        Expression right = visitExpression(ctx.right);
        return new ExprBinary(operator, left, right);
    }

    @Override
    public ExprBinary visitComparison(NiLangParser.ComparisonContext ctx) {
        ExprBinary.Operator operator = binaryOperator(ctx.compare().getStart());
        Expression left = visitExpression(ctx.left);
        Expression right = visitExpression(ctx.right);
        return new ExprBinary(operator, left, right);
    }

    @Override
    public StmtExpression visitExpressionStatement(NiLangParser.ExpressionStatementContext ctx) {
        Expression expression = visitExpression(ctx.expression());
        return new StmtExpression(expression);
    }

    @Override
    public StmtBlock visitBlock(NiLangParser.BlockContext ctx) {
        ImmutableList.Builder<Statement> statements = ImmutableList.builder();
        for (NiLangParser.StatementContext statementContext : ctx.statements().statement()) {
            statements.add(visitStatement(statementContext));
        }
        return new StmtBlock(statements.build());
    }

    @Override
    public StmtAssign visitFnStatement(NiLangParser.FnStatementContext ctx) {
        return new StmtAssign(new ExprFn());
    }

    @Override
    public StmtLoop visitLoop(NiLangParser.LoopContext ctx) {
        Expression condition = visitExpression(ctx.cond);
        StmtBlock body = visitBlock(ctx.block());
        return new StmtLoop(condition, body);
    }

    @Override
    public StmtWhile visitWhile(NiLangParser.WhileContext ctx) {
        Expression condition = visitExpression(ctx.cond);
        StmtBlock body = visitBlock(ctx.block());
        return new StmtWhile(condition, body);
    }

    @Override
    public StmtIf visitIf(NiLangParser.IfContext ctx) {
        Expression condition = visitExpression(ctx.cond);
        StmtBlock thenBody = visitBlock(ctx.thenBlock);
        StmtBlock elseBody = visitBlock(ctx.elseBlock);
        return new StmtIf(condition, thenBody, elseBody);
    }

    @Override
    public StmtReturn visitReturn(NiLangParser.ReturnContext ctx) {
        Expression expression = visitExpression(ctx.expression());
        return new StmtReturn(expression);
    }

    @Override
    public Ast visitAssignement(NiLangParser.AssignementContext ctx) {
        Expression expression = visitExpression(ctx.expression());
        ExprIdentifier variable = new ExprIdentifier();
        switch (ctx.assign().getStart().getType()) {
            case NiLangParser.ASSIGN: break;
            case NiLangParser.ASSIGN_PLUS:
                expression = new ExprBinary(ExprBinary.Operator.PLUS, variable, expression);
                break;
            case NiLangParser.ASSIGN_MINUS:
                expression = new ExprBinary(ExprBinary.Operator.MINUS, variable, expression);
                break;
            case NiLangParser.ASSIGN_TIMES:
                expression = new ExprBinary(ExprBinary.Operator.TIMES, variable, expression);
                break;
            case NiLangParser.ASSIGN_DIVIDE:
                expression = new ExprBinary(ExprBinary.Operator.DIVIDE, variable, expression);
                break;
            case NiLangParser.ASSIGN_MODULO:
                expression = new ExprBinary(ExprBinary.Operator.MODULO, variable, expression);
                break;
            case NiLangParser.ASSIGN_AND:
                expression = new ExprBinary(ExprBinary.Operator.AND, variable, expression);
                break;
            case NiLangParser.ASSIGN_OR:
                expression = new ExprBinary(ExprBinary.Operator.OR, variable, expression);
                break;
            default: throw new RuntimeException();
        }

        return new StmtAssign(expression);
    }

    @Override
    public Ast visitDeclaration(NiLangParser.DeclarationContext ctx) {
        return new StmtDeclaration();
    }

    @Override
    public Ast visitDefinition(NiLangParser.DefinitionContext ctx) {
        return new StmtDeclaration();
    }
}
