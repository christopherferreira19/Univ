package fr.aumgn.whilelang.ast;

public interface AstVisitor<T> {

    default T visit(Ast ast) {
        return ast.accept(this);
    }

    T visitBlock(Block block);

    T visitDeclaration(Declaration declaration);

    T visitIfStatement(IfStatement ifStatement);

    T visitWhileStatement(WhileStatement whileStatement);

    T visitAssignement(Assignement assignement);

    T visitPrint(Print print);

    T visitBinaryOp(BinaryOp op);

    T visitUnaryOp(UnaryOp op);

    T visitIdentifier(Identifier identifier);

    T visitNumeral(Numeral numeral);

    T visitBoolean(BooleanLiteral booleanLiteral);
}
