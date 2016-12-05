package fr.aumgn.whilelang.ast;

public class Print implements Statement {

    private final Ast argument;

    Print(Ast argument) {
        this.argument = argument;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitPrint(this);
    }

    public Ast getArgument() {
        return argument;
    }
}
