package fr.aumgn.cours.as.grammar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.Iterator;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Verify.verify;

public class Symboles implements Iterable<Symbole> {

    public static Symboles epsilon() {
        return new Symboles(ImmutableList.of(Terminal.epsilon()));
    }

    public static Symboles of(Symbole... symboles) {
        return of(ImmutableList.copyOf(symboles));
    }

    public static Symboles of(Iterable<? extends Symbole> iterable) {
        verify(!Iterables.isEmpty(iterable));
        return new Symboles(ImmutableList.copyOf(iterable));
    }

    public static Symboles of(String str) {
        ImmutableList.Builder<Symbole> builder = ImmutableList.builder();
        for (char c : str.toCharArray()) {
            if (c == ' ') {
                continue;
            }
            else if (c == 'ε') {
                builder.add(Terminal.epsilon());
            }
            else if (Character.isUpperCase(c)) {
                builder.add(NonTerminal.of(c));
            }
            else {
                builder.add(Terminal.of(c));
            }
        }

        return of(builder.build());
    }

    private final ImmutableList<Symbole> list;
    private final int hashCode;

    private Symboles(ImmutableList<Symbole> list) {
        this.list = checkNotNull(list);
        this.hashCode = list.hashCode();
    }

    public Symbole get(int indice) {
        return list.get(indice);
    }

    public Symbole getOne() {
        checkState(size() == 1);
        return get(0);
    }

    public int size() {
        return list.size();
    }

    @Override
    public Iterator<Symbole> iterator() {
        return list.iterator();
    }

    public Stream<Symbole> stream() {
        return list.stream();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Symboles)) {
            return false;
        }

        return list.equals(((Symboles) obj).list);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Symbole symbole : list) {
            builder.append(symbole.toString());
        }
        return builder.toString();
    }
}
