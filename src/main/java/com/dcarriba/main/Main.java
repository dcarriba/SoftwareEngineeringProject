package com.dcarriba.main;

import com.dcarriba.benchmarks.RunCompressionRatioBenchmarks;
import com.dcarriba.benchmarks.RunTimeBenchmarks;
import com.dcarriba.benchmarks.RunCalculateTransmissionTimeAndIfWorth;

/**
 * {@link Main} class of the project
 */
public class Main {

    /**
     * Main function of the project
     */
    public static void main(String[] args) {
        runBenchmarks();
    }

    /**
     * Run all the benchmarks
     */
    private static void runBenchmarks() {
        System.out.println("__________________________________");
        System.out.println("| *** BIT PACKING BENCHMARKS *** |");
        System.out.println("__________________________________\n");
        RunCompressionRatioBenchmarks.run();
        System.out.println("\n______________________________\n");
        RunTimeBenchmarks.run();
        System.out.println("\n______________________________\n");
        RunCalculateTransmissionTimeAndIfWorth.run();
    }
}
