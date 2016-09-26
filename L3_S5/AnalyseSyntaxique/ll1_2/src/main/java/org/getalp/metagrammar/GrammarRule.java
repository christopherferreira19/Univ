package org.getalp.metagrammar;


import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.Symbol;

import java.util.Iterator;
import java.util.List;

public class GrammarRule {

    public final NonTerminal lhs;
    public final List<Symbol> rhs;

    public GrammarRule(NonTerminal l, List<Symbol> r) {
        this.lhs = l;
        this.rhs = r;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(lhs).append(" -> ");
        if (rhs.isEmpty()) {
            builder.append("-eps- ");
        }
        else {
            for (Iterator<Symbol> iterator = rhs.iterator(); iterator.hasNext(); ) {
                builder.append(iterator.next());
                builder.append(" ");
            }
        }
        builder.append("; ");

        return builder.toString();
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    // Handles equality and hashCode so that to different objects representing
    // the same grammar rule are considered equal
    public boolean equals(Object o) {
        if (!(o instanceof GrammarRule)) return false;
        GrammarRule gr = (GrammarRule) o;
        return gr.lhs.equals(this.lhs) && gr.rhs.equals(this.rhs);
    }
}
