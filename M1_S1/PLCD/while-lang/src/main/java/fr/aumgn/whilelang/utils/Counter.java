package fr.aumgn.whilelang.utils;

public class Counter {

    private int count;

    public Counter() {
        this.count = 1;
    }

    public int next() {
        return count++;
    }
}
