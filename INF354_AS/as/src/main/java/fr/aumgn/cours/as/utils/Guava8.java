package fr.aumgn.cours.as.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.stream.Collector;

public class Guava8 {

    public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        @SuppressWarnings("unchecked")
        ImmutableListCollector<E> safeCast = (ImmutableListCollector<E>) ImmutableListCollector.INSTANCE;
        return safeCast;
    }

    public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        @SuppressWarnings("unchecked")
        ImmutableSetCollector<E> safeCast = (ImmutableSetCollector<E>) ImmutableSetCollector.INSTANCE;
        return safeCast;
    }
}
