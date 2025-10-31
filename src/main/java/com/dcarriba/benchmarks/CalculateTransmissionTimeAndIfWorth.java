package com.dcarriba.benchmarks;

import com.dcarriba.bitpacking.factory.CompressionVersion;

/**
 * {@link CalculateTransmissionTimeAndIfWorth} calculates the transmission time for a latency t at which compression becomes worthwhile
 */
public class CalculateTransmissionTimeAndIfWorth {
    /** BitPacking object used for the time benchmarks to get the compression and decompression times */
    private final CompressionVersion compressionVersion;
    /** Assuming average transmission rate of 10 Mbps (10 million bits per second) */
    double transmissionRate = 10_000_000; // bits per second

    /**
     * Constructor for {@link CalculateTransmissionTimeAndIfWorth}
     *
     * @param compressionVersion compression version used
     */
    public CalculateTransmissionTimeAndIfWorth(CompressionVersion compressionVersion) {
        this.compressionVersion = compressionVersion;
    }

    /**
     * Measures the time needed for the compression
     *
     * @param compressionVersion the compression version used for the TimeBenchmarks
     * @param array the input array
     * @return time needed for the compression
     */
    private long getCompressionTime(CompressionVersion compressionVersion, int[] array) {
        TimeBenchmarks timeBenchmarks = new TimeBenchmarks(compressionVersion);
        return timeBenchmarks.compressionTime(array);
    }

    /**
     * Measures the time needed for the decompression
     *
     * @param compressionVersion the compression version used for the TimeBenchmarks
     * @param array the input array
     * @return time needed for the decompression
     */
    private long getDecompressionTime(CompressionVersion compressionVersion, int[] array) {
        TimeBenchmarks timeBenchmarks = new TimeBenchmarks(compressionVersion);
        return timeBenchmarks.decompressionTime(array);
    }

    /**
     * Calculates the Transmission Time for the array, without any compression
     *
     * @param array the input array
     * @return the Transmission Time for the array, without any compression
     */
    public double getTransmissionTimeWithoutCompression(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array can't be null or empty.");
        }

        double originalSize = array.length * 32;

        return (originalSize / transmissionRate);
    }

    /**
     * Calculates the Transmission Time for the array, compressed using the compression version given as attribute
     *
     * @param array the input array
     * @return the Transmission Time for the array, with compression
     */
    public double getTransmissionTimeWithCompression(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array can't be null or empty.");
        }

        double originalSize = array.length * 32;
        double compressedSize = originalSize * new CompressionRatioBenchmarks(compressionVersion).compressionRatio(array);

        return (getCompressionTime(compressionVersion, array) / 1_000_000_000.0)
                + (compressedSize / transmissionRate)
                + (getDecompressionTime(compressionVersion, array) / 1_000_000_000.0);
    }

    /**
     * Checks if the compression was worth regarding the Transmission Times without and with compression
     *
     * @param array the input array
     * @return if the compression is worth
     */
    public boolean isCompressionWorth(int[] array){
        return (getTransmissionTimeWithoutCompression(array) - getTransmissionTimeWithCompression(array)) >= 0;
    }
}
