package fr.aumgn.nilang.ast;

import com.google.common.collect.ImmutableList;

public class Program implements Ast {

    private final ImmutableList<Statement> statements;

    public Program(ImmutableList<Statement> statements) {
        this.statements = statements;
    }
}
