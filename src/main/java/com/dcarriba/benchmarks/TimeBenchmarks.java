package com.dcarriba.benchmarks;

import com.dcarriba.bitpacking.BitPacking;
import com.dcarriba.bitpacking.factory.BitPackingFactory;
import com.dcarriba.bitpacking.factory.CompressionVersion;

/**
 * {@link TimeBenchmarks} is a class providing benchmarks in form of time measurements for {@link BitPacking}
 * implementations
 */
public class TimeBenchmarks {
    /** BitPacking object used for the benchmarks */
    private final BitPacking bitPacking;

    /**
     * Constructor for {@link TimeBenchmarks}
     *
     * @param compressionVersion compression version used for the benchmarks
     */
    public TimeBenchmarks(CompressionVersion compressionVersion) {
        this.bitPacking = BitPackingFactory.createBitPacking(compressionVersion);
    }

    /**
     * Measures the time needed for the compression
     *
     * @param array input array for the measurement
     * @return time needed for the compression
     */
    public long compressionTime(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array can't be null or empty.");
        }

        long startTime = System.nanoTime();
        bitPacking.compress(array);
        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    /**
     * Measures the time needed for the decompression
     *
     * @param array input array for the measurement
     * @return time needed for the decompression
     */
    public long decompressionTime(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array can't be null or empty.");
        }

        bitPacking.compress(array);
        int[] decompressedArray = new int[array.length];

        long startTime = System.nanoTime();
        bitPacking.decompress(decompressedArray);
        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    /**
     * Measures the time needed for the get method
     *
     * @param array input array for the measurement
     * @return time needed for the get method
     */
    public long getTime(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array can't be null or empty.");
        }

        bitPacking.compress(array);

        long startTime = System.nanoTime();
        for (int i = 0; i < array.length; i++) {
            bitPacking.get(i);
        }
        long endTime = System.nanoTime();

        return (endTime - startTime) / array.length;
    }
}
