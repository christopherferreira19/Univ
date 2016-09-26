package fr.aumgn.cours.as.utils;

import com.google.common.collect.ImmutableSet;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class ImmutableSetCollector<E> implements Collector<E, ImmutableSet.Builder<E>, ImmutableSet<E>> {

    static final ImmutableSetCollector<Object> INSTANCE = new ImmutableSetCollector<>();

    @Override
    public Supplier<ImmutableSet.Builder<E>> supplier() {
        return ImmutableSet::builder;
    }

    @Override
    public BiConsumer<ImmutableSet.Builder<E>, E> accumulator() {
        return ImmutableSet.Builder::add;
    }

    @Override
    public BinaryOperator<ImmutableSet.Builder<E>> combiner() {
        return (left, right) -> left.addAll(right.build());
    }

    @Override
    public Function<ImmutableSet.Builder<E>, ImmutableSet<E>> finisher() {
        return ImmutableSet.Builder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return ImmutableSet.of();
    }
}
