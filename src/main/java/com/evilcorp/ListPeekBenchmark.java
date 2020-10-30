package com.evilcorp;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ListPeekBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        @Param({"3", "10", "100"})
        public int size;

        public List<Integer> list;

        public Integer needle;

        @Setup(Level.Trial)
        public void setup() {
            list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(ThreadLocalRandom.current().nextInt());
            }
        }

        @Setup(Level.Invocation)
        public void beforeEach() {
            needle = list.get(ThreadLocalRandom.current().nextInt(list.size()));
        }
    }

    @Benchmark
    public boolean testCycle(BenchmarkState state, Blackhole blackhole) {
        for (int i = 0; i < state.list.size(); i++) {
            if (Objects.equals(state.list.get(i), state.needle)) {
                return false;
            }
        }
        return true;
    }

    @Benchmark
    public boolean testStream(BenchmarkState state, Blackhole blackhole) {
        return state.list.stream().noneMatch(e -> Objects.equals(e, state.needle));
    }
}
