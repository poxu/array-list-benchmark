package com.evilcorp;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayVsLinkedListBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        @Param({"array", "linked"})
        String type;

        public int size = 5_000_000;

        public List<Integer> list;

        @Setup(Level.Iteration)
        public void setup() {
            switch (type) {
                case "array":
                    list = new ArrayList();
                    break;
                case "linked":
                    list = new LinkedList<>();
                    break;
            }
            for (int i = 0; i < size; i++) {
                list.add(ThreadLocalRandom.current().nextInt());
            }
        }
    }

    @Benchmark
    public List<Integer> testAddMany(BenchmarkState state, Blackhole blackhole) {
        final int middle = state.list.size() / 2;
        final ListIterator<Integer> iterator = state.list.listIterator(middle);
        for (int i = 0; i < 1_000_000; i++) {
            iterator.add(i);
        }
        return state.list;
    }
}
