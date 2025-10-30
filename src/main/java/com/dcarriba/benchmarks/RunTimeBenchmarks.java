package com.dcarriba.benchmarks;

import com.dcarriba.bitpacking.BitPacking;
import com.dcarriba.bitpacking.factory.CompressionVersion;
import com.dcarriba.config.Config;
import com.dcarriba.utilities.Utilities;

/**
 * {@link RunTimeBenchmarks} is a class to run the {@link TimeBenchmarks} of {@link BitPacking} objects (i.e.
 * {@link BitPacking} compression versions).
 */
public class RunTimeBenchmarks {
    /** Defines the different array sizes for the benchmarks */
    private static final int[] arraySizes = {1000, 5000, 10000, 50000, 100000};

    /**
     * Static method to run the benchmarks
     */
    public static void run() {
        System.out.println("*** Time Benchmarks ***\n");

        // Initializes benchmarks for all bitpacking compression versions
        TimeBenchmarks withOverlapBenchmarks = new TimeBenchmarks(CompressionVersion.WITH_OVERLAP);
        TimeBenchmarks withoutOverlapBenchmarks = new TimeBenchmarks(CompressionVersion.WITHOUT_OVERLAP);

        // We do a warm-up phase before doing the real measurements
        warmUpBenchmark(withOverlapBenchmarks, withoutOverlapBenchmarks);

        // Runs the actual benchmarks
        System.out.println("Results of the time measurements benchmarks, using randomly generated arrays\nat different sizes:\n");
        runCompressionTimeBenchmarks(withOverlapBenchmarks, withoutOverlapBenchmarks);
        runDecompressionTimeBenchmarks(withOverlapBenchmarks, withoutOverlapBenchmarks);
        runGetTimeBenchmarks(withOverlapBenchmarks, withoutOverlapBenchmarks);
    }

    /**
     * Performs a warm-up for the JVM, for more accurate time measurements
     *
     * @param withOverlapBenchmarks TimeBenchmarks object for the BitPackingWithOverlap version
     * @param withoutOverlapBenchmarks TimeBenchmarks object for the BitPackingWithoutOverlap version
     */
    private static void warmUpBenchmark(TimeBenchmarks withOverlapBenchmarks, TimeBenchmarks withoutOverlapBenchmarks) {
        System.out.println("Warming up the JVM for better results...");


        for (int size : arraySizes) {
            int[] array = new int[size];
            for (int i = 0; i < Config.RUN_BENCHMARKS_REPETITIONS; i++) {
                Utilities.initializeArrayWithRandomPositiveValues(array);

                withOverlapBenchmarks.compressionTime(array);
                withoutOverlapBenchmarks.compressionTime(array);
                withOverlapBenchmarks.decompressionTime(array);
                withoutOverlapBenchmarks.decompressionTime(array);
                withOverlapBenchmarks.getTime(array);
                withoutOverlapBenchmarks.getTime(array);
            }
        }

        System.out.println("Warm-up completed.\n");
    }

    /**
     * Runs the compression benchmarks
     *
     * @param withOverlapBenchmarks TimeBenchmarks object for the BitPackingWithOverlap version
     * @param withoutOverlapBenchmarks TimeBenchmarks object for the BitPackingWithoutOverlap version
     */
    private static void runCompressionTimeBenchmarks(TimeBenchmarks withOverlapBenchmarks, TimeBenchmarks withoutOverlapBenchmarks) {
        System.out.println("=== Average Compression Time (in micro-seconds) (average over " + Config.RUN_BENCHMARKS_REPETITIONS + " repetitions) ===");
        System.out.printf("%-12s %-18s %-18s%n", "Array Size", "With Overlap", "Without Overlap");

        for (int size : arraySizes) {
            int[] array = new int[size];

            long totalCompressWithOverlap = 0;
            long totalCompressWithoutOverlap = 0;

            // Runs the benchmarks multiple times for better accuracy
            for (int i = 0; i < Config.RUN_BENCHMARKS_REPETITIONS; i++) {
                Utilities.initializeArrayWithRandomPositiveValues(array);
                totalCompressWithOverlap += withOverlapBenchmarks.compressionTime(array);
                totalCompressWithoutOverlap += withoutOverlapBenchmarks.compressionTime(array);
            }

            // Computes average time (and converts from ns to µs)
            double avgCompressWithOverlap = totalCompressWithOverlap / (double) Config.RUN_BENCHMARKS_REPETITIONS / 1000;
            double avgCompressWithoutOverlap = totalCompressWithoutOverlap / (double) Config.RUN_BENCHMARKS_REPETITIONS / 1000;

            System.out.printf("%-12d %-18.2f %-18.2f%n", size, avgCompressWithOverlap, avgCompressWithoutOverlap);
        }
    }

    /**
     * Runs the decompression benchmarks
     *
     * @param withOverlapBenchmarks TimeBenchmarks object for the BitPackingWithOverlap version
     * @param withoutOverlapBenchmarks TimeBenchmarks object for the BitPackingWithoutOverlap version
     */
    private static void runDecompressionTimeBenchmarks(TimeBenchmarks withOverlapBenchmarks, TimeBenchmarks withoutOverlapBenchmarks) {
        System.out.println("\n=== Average Decompression Time (in micro-seconds) (average over " + Config.RUN_BENCHMARKS_REPETITIONS + " repetitions) ===");
        System.out.printf("%-12s %-18s %-18s%n", "Array Size", "With Overlap", "Without Overlap");

        for (int size : arraySizes) {
            int[] array = new int[size];

            long totalDecompressWithOverlap = 0;
            long totalDecompressWithoutOverlap = 0;

            // Runs the benchmarks multiple times for better accuracy
            for (int i = 0; i < Config.RUN_BENCHMARKS_REPETITIONS; i++) {
                Utilities.initializeArrayWithRandomPositiveValues(array);
                totalDecompressWithOverlap += withOverlapBenchmarks.decompressionTime(array);
                totalDecompressWithoutOverlap += withoutOverlapBenchmarks.decompressionTime(array);
            }

            // Computes average time (and converts from ns to µs)
            double avgDecompressWithOverlap = totalDecompressWithOverlap / (double) Config.RUN_BENCHMARKS_REPETITIONS / 1000;
            double avgDecompressWithoutOverlap = totalDecompressWithoutOverlap / (double) Config.RUN_BENCHMARKS_REPETITIONS / 1000;

            System.out.printf("%-12d %-18.2f %-18.2f%n", size, avgDecompressWithOverlap, avgDecompressWithoutOverlap);
        }
    }

    /**
     * Runs the benchmarks for the get method
     *
     * @param withOverlapBenchmarks TimeBenchmarks object for the BitPackingWithOverlap version
     * @param withoutOverlapBenchmarks TimeBenchmarks object for the BitPackingWithoutOverlap version
     */
    private static void runGetTimeBenchmarks(TimeBenchmarks withOverlapBenchmarks, TimeBenchmarks withoutOverlapBenchmarks) {
        System.out.println("\n=== Average Get Time (in nano-seconds) (average over " + Config.RUN_BENCHMARKS_REPETITIONS + " repetitions) ===");
        System.out.printf("%-12s %-18s %-18s%n", "Array Size", "With Overlap", "Without Overlap");

        for (int size : arraySizes) {
            int[] array = new int[size];

            long totalGetWithOverlap = 0;
            long totalGetWithoutOverlap = 0;

            // Runs the benchmarks multiple times for better accuracy
            for (int i = 0; i < Config.RUN_BENCHMARKS_REPETITIONS; i++) {
                Utilities.initializeArrayWithRandomPositiveValues(array);
                totalGetWithOverlap += withOverlapBenchmarks.getTime(array);
                totalGetWithoutOverlap += withoutOverlapBenchmarks.getTime(array);
            }

            // Computes average time (and keeps the result in ns)
            double avgGetWithOverlap = totalGetWithOverlap / (double) Config.RUN_BENCHMARKS_REPETITIONS;
            double avgGetWithoutOverlap = totalGetWithoutOverlap / (double) Config.RUN_BENCHMARKS_REPETITIONS;

            System.out.printf("%-12d %-18.2f %-18.2f%n", size, avgGetWithOverlap, avgGetWithoutOverlap);
        }
    }
}
