package fr.aumgn.cours.as.algos;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import fr.aumgn.cours.as.grammar.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cocke {

    public static class Table {

        public static class Cell {
            private final Map<NonTerminal, List<Source>> sourcesMap;

            public Cell() {
                this.sourcesMap = Maps.newHashMap();
            }

            public Set<NonTerminal> nonTerminals() {
                return sourcesMap.keySet();
            }

            public List<Source> get(NonTerminal axiome) {
                return sourcesMap.get(axiome);
            }

            private List<Source> sources(NonTerminal nonTerminal) {
                List<Source> sources = sourcesMap.get(nonTerminal);
                if (sources == null) {
                    sources = Lists.newArrayList();
                    sourcesMap.put(nonTerminal, sources);
                }
                return sources;
            }

            public void init(NonTerminal preTerminal, Symbole symbole) {
                sources(preTerminal).add(new UnarySource(symbole));
            }

            public void add(NonTerminal nonTerminal, NonTerminal left, NonTerminal right, int size) {
                sources(nonTerminal).add(new BinarySource(left, right, size));
            }
        }

        public interface Source {
        }

        public static class UnarySource implements Source {
            private final Symbole symbole;

            public UnarySource(Symbole symbole) {
                this.symbole = symbole;
            }
        }

        public static class BinarySource implements Source {
            private final NonTerminal left;
            private final NonTerminal right;
            private final int size;

            public BinarySource(NonTerminal left, NonTerminal right, int size) {
                this.left = left;
                this.right = right;
                this.size = size;
            }
        }

        private final int n;
        private final Cell[] table;

        public Table(int n) {
            this.n = n;
            this.table = new Cell[n*n];
        }

        public Cell at(int i, int j) {
            if (i+j > n) {
                throw new RuntimeException("Invalid Index");
            }

            Cell cell = table[i + j * n];
            if (cell == null) {
                cell = new Cell();
                table[i + j * n] = cell;
            }

            return cell;
        }
    }

    public static Result apply(Grammar grammar, Symboles s) {
        if (s.isEpsilon()) {
            return new DumbResult(grammar.productions(grammar.axiome())
                    .rightSides()
                    .contains(Symboles.epsilon()));
        }

        int n = s.size();
        Table table = new Table(n);
        Map<Symbole, NonTerminal> preTerminals = Maps.newHashMap();
        SetMultimap<Symboles, NonTerminal> nonTerminals = HashMultimap.create();

        for (Productions productions : grammar.allProductions()) {
            for (Symboles symboles : productions.rightSides()) {
                NonTerminal nonTerminal = productions.leftSide();
                if (symboles.size() == 1) {
                    preTerminals.put(symboles.getOne(), nonTerminal);
                }
                else {
                    nonTerminals.put(symboles, nonTerminal);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            Symbole symbole = s.get(i);
            NonTerminal preTerminal = preTerminals.get(symbole);
            if (preTerminal == null) {
                return new DumbResult(false);
            }

            table.at(i, 0).init(preTerminal, symbole);
        }

        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j + 1; i++) {
                for (int k = 0; k < j; k++) {
                    int leftI = i;
                    int leftJ = k;
                    int rightI = i + k + 1;
                    int rightJ = j - k - 1;

                    Iterable<NonTerminal> leftSet = table.at(leftI, leftJ).nonTerminals();
                    Iterable<NonTerminal> rightSet = table.at(rightI, rightJ).nonTerminals();
                    for (NonTerminal left : leftSet) {
                        for (NonTerminal right : rightSet) {
                            Symboles symboles = Symboles.of(left, right);
                            for (NonTerminal nonTerminal : nonTerminals.get(symboles)) {
                                table.at(i, j).add(nonTerminal, left, right, k);
                            }
                        }
                    }
                }
            }
        }

        return new TableResult(grammar, s, table);
    }

    public interface Result {

        boolean matches();

        void printTree();
    }

    private static class DumbResult implements Result {
        private final boolean match;

        public DumbResult(boolean match) {
            this.match = match;
        }

        @Override
        public boolean matches() {
            return match;
        }

        @Override
        public void printTree() {
        }
    }

    private static class TableResult implements Result {
        private final Grammar grammar;
        private final Symboles symboles;
        private final Table table;

        public TableResult(Grammar grammar, Symboles symboles, Table table) {
            this.grammar = grammar;
            this.symboles = symboles;
            this.table = table;
        }

        public boolean matches() {
            return table.at(0, symboles.size() - 1).nonTerminals().contains(grammar.axiome());
        }

        public void printTree() {
            if (!matches()) {
                return;
            }

            printSubtree(0, symboles.size() - 1, grammar.axiome(), "");
            System.out.println();
        }

        private void printSubtree(int i, int j, NonTerminal nonTerminal, String indent) {
            System.out.print(indent + nonTerminal.toString());

            Table.Source source = table.at(i, j).get(nonTerminal).get(0);
            if (source instanceof Table.BinarySource) {
                Table.BinarySource binarySource = (Table.BinarySource) source;
                int k = binarySource.size;
                int leftI = i;
                int leftJ = k;
                int rightI = i + k + 1;
                int rightJ = j - k - 1;

                System.out.println();
                printSubtree(leftI, leftJ, binarySource.left, indent + "  ");
                printSubtree(rightI, rightJ, binarySource.right, indent + "  ");
            }
            else {
                Table.UnarySource unarySource = (Table.UnarySource) source;
                System.out.println(" " + unarySource.symbole);
            }
        }
    }
}
