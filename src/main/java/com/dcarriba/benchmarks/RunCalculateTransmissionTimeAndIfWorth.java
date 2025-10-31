package com.dcarriba.benchmarks;

import com.dcarriba.bitpacking.factory.CompressionVersion;
import com.dcarriba.utilities.Utilities;

/**
 * {@link RunCalculateTransmissionTimeAndIfWorth} runs the calculations of {@link CalculateTransmissionTimeAndIfWorth}
 * for different arrays
 */
public class RunCalculateTransmissionTimeAndIfWorth {
    /** Defines the different array sizes */
    private static final int[] arraySizes = {1000, 15000};
    /** Defines the different maximum values */
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
     * Static method to run the calculations for all array sizes, max values and compression versions.
     */
    public static void run() {
        System.out.println("***     Transmission Time calculation      ***\n" +
                           "*** and if the compression is worth or not ***\n");

        for (int arraySize : arraySizes) {

            System.out.println("=== Using an array with size " + arraySize + " ===");
            System.out.printf("%-20s %-12s %-21s %-21s %-21s%n", "Max Possible Value", "T_without", "With Overlap", "Without Overlap", "With Overflow Area");
            System.out.printf("%-20s %-12s %-10s %-10s %-10s %-10s %-10s %-10s%n", "", "", "T_time", "is worth?", "T_time", "is worth?", "T_time", "is worth?");

            for (int maxValue : maxValues) {

                int[] array = new int[arraySize];
                Utilities.initializeArrayWithRandomPositiveValues(array, maxValue);

                CalculateTransmissionTimeAndIfWorth withOverlap = new CalculateTransmissionTimeAndIfWorth(CompressionVersion.WITH_OVERLAP);
                CalculateTransmissionTimeAndIfWorth withoutOverlap = new CalculateTransmissionTimeAndIfWorth(CompressionVersion.WITHOUT_OVERLAP);
                CalculateTransmissionTimeAndIfWorth withOverflowArea = new CalculateTransmissionTimeAndIfWorth(CompressionVersion.WITH_OVERFLOW_AREA);

                // Calculate transmission times for each compression version
                double transmissionTimeWithoutCompression = withOverlap.getTransmissionTimeWithoutCompression(array);
                double transmissionTimeWithCompressionWithOverlap = withOverlap.getTransmissionTimeWithCompression(array);
                double transmissionTimeWithCompressionWithoutOverlap = withoutOverlap.getTransmissionTimeWithCompression(array);
                double transmissionTimeWithCompressionWithOverflowArea = withOverflowArea.getTransmissionTimeWithCompression(array);

                // Check if compression is worthwhile for each version
                boolean isCompressionWorthWithOverlap = withOverlap.isCompressionWorth(array);
                boolean isCompressionWorthWithoutOverlap = withoutOverlap.isCompressionWorth(array);
                boolean isCompressionWorthWithOverflowArea = withOverflowArea.isCompressionWorth(array);

                System.out.printf("%-20d %-12.6f %-10.6f %-10b %-10.6f %-10b %-10.6f %-10b%n",
                        maxValue,
                        transmissionTimeWithoutCompression,
                        transmissionTimeWithCompressionWithOverlap,
                        isCompressionWorthWithOverlap,
                        transmissionTimeWithCompressionWithoutOverlap,
                        isCompressionWorthWithoutOverlap,
                        transmissionTimeWithCompressionWithOverflowArea,
                        isCompressionWorthWithOverflowArea);
            }
            System.out.println();
        }
        System.out.println(
                "Notice:\n" +
                "- \"T_without\" is the Transmission Time (in seconds) of the array without compression\n" +
                "- for each compression version \"T_time\" is the Transmission Time (in seconds) of the array, compressed using that compression version\n" +
                "- for each compression version \"is worth?\" indicates if the compression was worth, i.e. uif it improved the Transmission Time\n" +
                "- in the calculations the average transmission rate of 10 Mbps is used");
    }
}
