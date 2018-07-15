package fr.aumgn.nilang.ast.statement;

import com.google.common.collect.ImmutableList;
import fr.aumgn.nilang.ast.Statement;

public class StmtBlock implements Statement {

    private final ImmutableList<Statement> statements;

    public StmtBlock(ImmutableList<Statement> statements) {
        this.statements = statements;
    }

    public ImmutableList<Statement> getStatements() {
        return statements;
    }
}
