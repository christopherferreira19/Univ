package fr.aumgn.cours.as;

import com.google.common.base.Strings;
import fr.aumgn.cours.as.algos.*;
import fr.aumgn.cours.as.grammar.Grammar;
import fr.aumgn.cours.as.grammar.Symboles;

// → ε
public class AS {

    public static void main(String[] args) {
        testAnBn();
    }

    private static void testAnBn() {
        Grammar anbn = Grammar.builder()
                .axiome("S")
                .production("S → aSb")
                .production("S → ε")
                .build();

        testCocke("AnBn", anbn, "aabb", "aab", "aaabbbb");
    }

    private static void testCocke(String name, Grammar grammar, String... strings) {
        Grammar clean = grammar;
        clean = RemoveEpsilons.apply(clean);
        clean = RemoveSimples.apply(clean);
        clean = RemoveUnproductives.apply(clean);
        clean = RemoveUnreachables.apply(clean);
        Grammar cnf = ChomskyNormalForm.apply(clean);
        describeGrammar(name + " CNF", cnf);

        for (String str : strings) {
            Cocke.Result result = Cocke.apply(cnf, Symboles.of(str));
            System.out.println(Strings.padEnd(str, 15, ' ') + " => " + result.matches());
            if (result.matches()) {
                result.printTree();
            }
        }
    }

    private static void testParenthesis() {
        Grammar parenthesis = Grammar.builder()
                .axiome("S")
                .production("S → (S)S")
                .production("S → ε")
                .build();

        Grammar parenthesisClean = parenthesis;
        parenthesisClean = RemoveEpsilons.apply(parenthesisClean);
        parenthesisClean = RemoveSimples.apply(parenthesisClean);
        parenthesisClean = RemoveUnproductives.apply(parenthesisClean);
        parenthesisClean = RemoveUnreachables.apply(parenthesisClean);
        describeGrammar("Parenthesis Clean", parenthesisClean);
        describeGrammar("Parenthesis CNF", ChomskyNormalForm.apply(parenthesisClean));
    }

    private static void testCleanup() {
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
