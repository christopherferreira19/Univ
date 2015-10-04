package fr.aumgn.cours.as;

import fr.aumgn.cours.as.algos.*;
import fr.aumgn.cours.as.grammar.Grammar;

// → ε
public class AS {

    public static void main(String[] args) {
        Grammar needCleanup = Grammar.builder()
                .axiome("S")
                .production("S → AB | xAyB")
                .production("A → a | ε")
                .production("B → CC | b")
                .production("C → D | c")
                .production("D → E | d | ε")
                .production("E → eF")
                .production("F → fE")
                .production("G → gE")
                .production("G → gH")
                .production("H → hA")
                .build();
        describeGrammar("NeedCleanup", needCleanup);

        Grammar withoutEpsilons = RemoveEpsilons.apply(needCleanup);
        describeGrammar("WithoutEpsilons", withoutEpsilons);

        Grammar withoutSimples = RemoveSimples.apply(withoutEpsilons);
        describeGrammar("WithoutSimples", withoutSimples);

        Grammar withoutUnproductives = RemoveUnproductives.apply(withoutSimples);
        describeGrammar("WithoutUnproductives", withoutUnproductives);

        Grammar withoutUnreachables = RemoveUnreachables.apply(withoutUnproductives);
        describeGrammar("WithoutUnreachables", withoutUnreachables);

        Grammar cnf = ChomskyNormalForm.apply(withoutUnreachables);
        describeGrammar("CNF", cnf);
    }

    private static void describeGrammar(String name, Grammar grammar) {
        System.out.println("[" + name + "]");
        System.out.print("NonTerminals : ");
        System.out.println(grammar.nonTerminals());
        System.out.print("Terminals : ");
        System.out.println(grammar.terminals());
        System.out.println("Productions : ");
        grammar.toStrings().forEach((production -> System.out.println("  " + production)));
        System.out.println();
    }
}
