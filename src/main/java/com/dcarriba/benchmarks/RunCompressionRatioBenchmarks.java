package com.dcarriba.benchmarks;

import com.dcarriba.bitpacking.BitPacking;
import com.dcarriba.bitpacking.factory.CompressionVersion;
import com.dcarriba.config.Config;
import com.dcarriba.utilities.Utilities;

/**
 * {@link RunCompressionRatioBenchmarks} is a class to run the {@link CompressionRatioBenchmarks} of {@link BitPacking}
 * objects (i.e. {@link BitPacking} compression versions).
 */
public class RunCompressionRatioBenchmarks {
    /**
     * Defines the array size used for the compression ratio benchmarks
     * (The compression ratio is never affected by the array size)
     */
    private static final int arraySize = 10000;

    /** Defines the different maximum values for the benchmarks */
    private static final int[] maxValues = {
            1,
            10,
            100,
            256,
            1000,
            5000,
            10000,
            Integer.MAX_VALUE / 1024,
            Integer.MAX_VALUE / 128,
            Integer.MAX_VALUE / 2,
            Integer.MAX_VALUE
    };

    /**
     * Static method to run the benchmarks
     */
    public static void run() {
        System.out.println("*** Compression Ratio Benchmarks ***\n");
        // Initializes benchmarks for all bitpacking compression versions
        CompressionRatioBenchmarks withOverlapBenchmarks = new CompressionRatioBenchmarks(CompressionVersion.WITH_OVERLAP);
        CompressionRatioBenchmarks withoutOverlapBenchmarks = new CompressionRatioBenchmarks(CompressionVersion.WITHOUT_OVERLAP);

        // Runs the actual benchmarks
        System.out.println("Results of the compression ratio benchmarks, using randomly generated arrays\nwith different possible maximum values:\n");
        runCompressionRatioBenchmarks(withOverlapBenchmarks, withoutOverlapBenchmarks);
    }

    /**
     * Runs the compression ratio benchmarks
     *
     * @param withOverlapBenchmarks CompressionRatioBenchmarks object for the BitPackingWithOverlap version
     * @param withoutOverlapBenchmarks CompressionRatioBenchmarks object for the BitPackingWithoutOverlap version
     */
    private static void runCompressionRatioBenchmarks(CompressionRatioBenchmarks withOverlapBenchmarks, CompressionRatioBenchmarks withoutOverlapBenchmarks) {
        System.out.println("=== Average Compression Ratio (average over " + Config.RUN_BENCHMARKS_REPETITIONS + " repetitions) ===");
        System.out.printf("%-23s %-18s %-18s%n", "Max Possible Value", "With Overlap", "Without Overlap");

        for (int maxValue : maxValues) {
            int[] array = new int[arraySize];

            double totalCompressWithOverlap = 0;
            double totalCompressWithoutOverlap = 0;

            // Runs the benchmarks multiple times for better accuracy
            for (int i = 0; i < Config.RUN_BENCHMARKS_REPETITIONS; i++) {
                Utilities.initializeArrayWithRandomPositiveValues(array, maxValue);
                totalCompressWithOverlap += withOverlapBenchmarks.compressionRatio(array);
                totalCompressWithoutOverlap += withoutOverlapBenchmarks.compressionRatio(array);
            }

            // Computes the average ratio from all repetitions
            double avgCompressWithOverlap = totalCompressWithOverlap / (double) Config.RUN_BENCHMARKS_REPETITIONS;
            double avgCompressWithoutOverlap = totalCompressWithoutOverlap / (double) Config.RUN_BENCHMARKS_REPETITIONS;

            System.out.printf("%-23d %-18.2f %-18.2f%n", maxValue, avgCompressWithOverlap, avgCompressWithoutOverlap);
        }
    }
}
