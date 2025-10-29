package com.dcarriba.benchmarks;

import com.dcarriba.bitpacking.BitPacking;
import com.dcarriba.bitpacking.factory.CompressionVersion;
import com.dcarriba.utilities.Utilities;

/**
 * {@link RunTimeBenchmarks} is a class to run the {@link TimeBenchmarks} of {@link BitPacking} objects (i.e.
 * {@link BitPacking} compression versions).
 */
public class RunTimeBenchmarks {

    /** Number of repetitions inside the benchmarks methods for more accurate measurements */
    private static final int REPETITIONS_N = 50;

    /**
     * Static method to run the benchmarks
     */
    public static void run() {
        // Defines the different array sizes for the benchmarks
        int[] arraySizes = {1000, 5000, 10000, 50000, 100000};

        // Initializes benchmarks for all bitpacking compression versions
        TimeBenchmarks withOverlapBenchmarks = new TimeBenchmarks(CompressionVersion.WITH_OVERLAP);
        TimeBenchmarks withoutOverlapBenchmarks = new TimeBenchmarks(CompressionVersion.WITHOUT_OVERLAP);

        // We do a warm-up phase before doing the real measurements
        warmUpBenchmark(arraySizes, withOverlapBenchmarks, withoutOverlapBenchmarks);

        // Runs the actual benchmarks
        System.out.println("Results of the time measurements benchmarks, using randomly generated arrays at different sizes:\n");
        runCompressionTimeBenchmarks(arraySizes, withOverlapBenchmarks, withoutOverlapBenchmarks);
        runDecompressionTimeBenchmarks(arraySizes, withOverlapBenchmarks, withoutOverlapBenchmarks);
        runGetTimeBenchmarks(arraySizes, withOverlapBenchmarks, withoutOverlapBenchmarks);
    }

    /**
     * Performs a warm-up for the JVM, for more accurate time measurements
     *
     * @param arraySizes the different array sizes for the benchmarks
     * @param withOverlapBenchmarks TimeBenchmarks object for the BitPackingWithOverlap version
     * @param withoutOverlapBenchmarks TimeBenchmarks object for the BitPackingWithoutOverlap version
     */
    private static void warmUpBenchmark(int[] arraySizes, TimeBenchmarks withOverlapBenchmarks, TimeBenchmarks withoutOverlapBenchmarks) {
        System.out.println("Warming up the JVM for better results...");


        for (int size : arraySizes) {
            int[] array = new int[size];
            for (int i = 0; i < REPETITIONS_N; i++) {
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
     * @param arraySizes the different array sizes for the benchmarks
     * @param withOverlapBenchmarks TimeBenchmarks object for the BitPackingWithOverlap version
     * @param withoutOverlapBenchmarks TimeBenchmarks object for the BitPackingWithoutOverlap version
     */
    private static void runCompressionTimeBenchmarks(int[] arraySizes, TimeBenchmarks withOverlapBenchmarks, TimeBenchmarks withoutOverlapBenchmarks) {
        System.out.println("=== Average Compression Time (in micro-seconds) (average over " + REPETITIONS_N + " repetitions) ===");
        System.out.printf("%-12s %-20s %-20s%n", "Array Size", "With Overlap", "Without Overlap");

        for (int size : arraySizes) {
            int[] array = new int[size];

            long totalCompressWithOverlap = 0;
            long totalCompressWithoutOverlap = 0;

            // Runs the benchmarks multiple times for better accuracy
            for (int i = 0; i < REPETITIONS_N; i++) {
                Utilities.initializeArrayWithRandomPositiveValues(array);
                totalCompressWithOverlap += withOverlapBenchmarks.compressionTime(array);
                totalCompressWithoutOverlap += withoutOverlapBenchmarks.compressionTime(array);
            }

            // Computes average time (and converts from ns to µs)
            double avgCompressWithOverlap = totalCompressWithOverlap / (double) REPETITIONS_N / 1000;
            double avgCompressWithoutOverlap = totalCompressWithoutOverlap / (double) REPETITIONS_N / 1000;

            System.out.printf("%-12d %-20.2f %-20.2f%n", size, avgCompressWithOverlap, avgCompressWithoutOverlap);
        }
    }

    /**
     * Runs the decompression benchmarks
     *
     * @param arraySizes the different array sizes for the benchmarks
     * @param withOverlapBenchmarks TimeBenchmarks object for the BitPackingWithOverlap version
     * @param withoutOverlapBenchmarks TimeBenchmarks object for the BitPackingWithoutOverlap version
     */
    private static void runDecompressionTimeBenchmarks(int[] arraySizes, TimeBenchmarks withOverlapBenchmarks, TimeBenchmarks withoutOverlapBenchmarks) {
        System.out.println("\n=== Average Decompression Time (in micro-seconds) (average over " + REPETITIONS_N + " repetitions) ===");
        System.out.printf("%-12s %-20s %-20s%n", "Array Size", "With Overlap", "Without Overlap");

        for (int size : arraySizes) {
            int[] array = new int[size];

            long totalDecompressWithOverlap = 0;
            long totalDecompressWithoutOverlap = 0;

            // Runs the benchmarks multiple times for better accuracy
            for (int i = 0; i < REPETITIONS_N; i++) {
                Utilities.initializeArrayWithRandomPositiveValues(array);
                totalDecompressWithOverlap += withOverlapBenchmarks.decompressionTime(array);
                totalDecompressWithoutOverlap += withoutOverlapBenchmarks.decompressionTime(array);
            }

            // Computes average time (and converts from ns to µs)
            double avgDecompressWithOverlap = totalDecompressWithOverlap / (double) REPETITIONS_N / 1000;
            double avgDecompressWithoutOverlap = totalDecompressWithoutOverlap / (double) REPETITIONS_N / 1000;

            System.out.printf("%-12d %-20.2f %-20.2f%n", size, avgDecompressWithOverlap, avgDecompressWithoutOverlap);
        }
    }

    /**
     * Runs the benchmarks for the get method
     *
     * @param arraySizes the different array sizes for the benchmarks
     * @param withOverlapBenchmarks TimeBenchmarks object for the BitPackingWithOverlap version
     * @param withoutOverlapBenchmarks TimeBenchmarks object for the BitPackingWithoutOverlap version
     */
    private static void runGetTimeBenchmarks(int[] arraySizes, TimeBenchmarks withOverlapBenchmarks, TimeBenchmarks withoutOverlapBenchmarks) {
        System.out.println("\n=== Average Get Time (in nano-seconds) (average over " + REPETITIONS_N + " repetitions) ===");
        System.out.printf("%-12s %-20s %-20s%n", "Array Size", "With Overlap", "Without Overlap");

        for (int size : arraySizes) {
            int[] array = new int[size];

            long totalGetWithOverlap = 0;
            long totalGetWithoutOverlap = 0;

            // Runs the benchmarks multiple times for better accuracy
            for (int i = 0; i < REPETITIONS_N; i++) {
                Utilities.initializeArrayWithRandomPositiveValues(array);
                totalGetWithOverlap += withOverlapBenchmarks.getTime(array);
                totalGetWithoutOverlap += withoutOverlapBenchmarks.getTime(array);
            }

            // Computes average time (and keeps the result in ns)
            double avgGetWithOverlap = totalGetWithOverlap / (double) REPETITIONS_N;
            double avgGetWithoutOverlap = totalGetWithoutOverlap / (double) REPETITIONS_N;

            System.out.printf("%-12d %-20.2f %-20.2f%n", size, avgGetWithOverlap, avgGetWithoutOverlap);
        }
    }
}
