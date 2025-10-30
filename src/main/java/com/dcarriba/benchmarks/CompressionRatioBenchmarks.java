package com.dcarriba.benchmarks;

import com.dcarriba.bitpacking.BitPacking;
import com.dcarriba.bitpacking.factory.BitPackingFactory;
import com.dcarriba.bitpacking.factory.CompressionVersion;

/**
 * {@link CompressionRatioBenchmarks} is a class providing benchmarks in form the compression ratio
 * (i.e. the percentage of how much the array got compressed) for {@link BitPacking} implementations.
 */
public class CompressionRatioBenchmarks {
    /** BitPacking object used for the benchmarks */
    private final BitPacking bitPacking;

    /**
     * Constructor for {@link CompressionRatioBenchmarks}
     *
     * @param compressionVersion compression version used for the benchmarks
     */
    public CompressionRatioBenchmarks(CompressionVersion compressionVersion) {
        this.bitPacking = BitPackingFactory.createBitPacking(compressionVersion);
    }

    /**
     * Measures the compression ratio, i.e. the percentage of how much the array got compressed
     *
     * @param array input array
     * @return the compression ratio
     */
    public double compressionRatio(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array can't be null or empty.");
        }

        bitPacking.compress(array);

        return (double) bitPacking.getCompressedArray().length / array.length;
    }
}
