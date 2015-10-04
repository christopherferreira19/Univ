package fr.aumgn.cours.as.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class ImmutableListCollector<E> implements Collector<E, ImmutableList.Builder<E>, ImmutableList<E>> {

    static final ImmutableListCollector<Object> INSTANCE = new ImmutableListCollector<>();

    @Override
    public Supplier<ImmutableList.Builder<E>> supplier() {
        return ImmutableList::builder;
    }

    @Override
    public BiConsumer<ImmutableList.Builder<E>, E> accumulator() {
        return ImmutableList.Builder::add;
    }

    @Override
    public BinaryOperator<ImmutableList.Builder<E>> combiner() {
        return (left, right) -> left.addAll(right.build());
    }

    @Override
    public Function<ImmutableList.Builder<E>, ImmutableList<E>> finisher() {
        return ImmutableList.Builder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return ImmutableSet.of();
    }
}
