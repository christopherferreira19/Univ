package fr.aumgn.whilelang.utils;

import java.util.function.Function;

public class Generator<E> {

    private final Function<Integer, E> fn;
    private int number;

    public Generator(Function<Integer, E> fn) {
        this.fn = fn;
        this.number = 1;
    }

    public E generate() {
        return fn.apply(number++);
    }
}
