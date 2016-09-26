package org.getalp.metagrammar;

import com.google.common.base.Optional;
import com.google.common.collect.Queues;
import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.Symbol;
import org.getalp.ll1.LL1Table;
import org.getalp.metalexer.Lexer;

import java.io.Reader;
import java.util.Deque;
import java.util.List;

import static com.google.common.base.Verify.verify;

public class Parser {

    private final Lexer.Factory lexerFactory;
    private final Grammar grammar;
    private final LL1Table table;

    public Parser(Lexer.Factory lexerFactory, Grammar grammar, LL1Table table) {
        this.lexerFactory = lexerFactory;
        this.grammar = grammar;
        this.table = table;
    }

    public boolean reconnaitre(Reader reader) {
        Lexer lexer = lexerFactory.createLexer(reader);

        Deque<Symbol> pile = Queues.newArrayDeque();
        pile.push(grammar.source);

        String lexeme = lexer.next();

        while (!pile.isEmpty()) {
            if (pile.peek().isTerminal()) {
                Symbol symbolPile = pile.pop();
                if (!symbolPile.name.equals(lexeme)) {
                    return false;
                }

                lexeme = lexer.next();
            }
            else {
                NonTerminal nonTerminal = (NonTerminal) pile.pop();
                Optional<GrammarRule> ruleOpt = table.getOne(nonTerminal, lexeme);
                if (!ruleOpt.isPresent()) {
                    return false;
                }

                GrammarRule rule = ruleOpt.get();
                verify(rule.lhs.equals(nonTerminal));

                List<Symbol> rhs = rule.rhs;
                for (int i = rhs.size() - 1; i >= 0; i--) {
                    pile.push(rhs.get(i));
                }
            }
        }

        return lexeme.equals(Lexer.MARQUEUR_FIN) && pile.isEmpty();
    }
}
