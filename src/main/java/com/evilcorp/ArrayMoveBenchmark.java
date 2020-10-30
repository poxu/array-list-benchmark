package com.evilcorp;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.ThreadLocalRandom;

public class ArrayMoveBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        public int[] array;

        @Setup(Level.Iteration)
        public void setup() {
            array = new int[1000];
            for (int i = 0; i < 1000; i++) {
                array[i] = ThreadLocalRandom.current().nextInt();
            }
        }

    }

    @Benchmark
    public void testArrayCopy(BenchmarkState state, Blackhole blackhole) {
        System.arraycopy(state.array, 0, state.array, 1, state.array.length - 1);
        blackhole.consume(state.array);
    }

    @Benchmark
    public void testCyclicMove(BenchmarkState state, Blackhole blackhole) {
        final int[] array = state.array;
        for (int i = 1; i < array.length; i++) {
            array[i] = array[i - 1];
        }
        blackhole.consume(array);
    }
}
