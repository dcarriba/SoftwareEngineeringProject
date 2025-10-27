package com.dcarriba.utilities;

import com.dcarriba.bitpacking.BitPacking;

/**
 * The {@link Utilities} class contains many useful utility functions
 */
public class Utilities {

    /**
     * Utility function to print original and compressed arrays in binary.
     *
     * @param originalArray the original array to print
     * @param bitPacking contains the compressed array to print
     */
    public static void printOriginalAndCompressed(int[] originalArray, BitPacking bitPacking) {
        System.out.println("Original array in binary:");
        printArrayInBinary(originalArray);
        System.out.println("Compressed array in binary:");
        printArrayInBinary(bitPacking.getCompressedArray());
        System.out.println();
    }

    /**
     * Utility unction to print the binary representation of each integer of an array.
     *
     * @param array array to print
     */
    public static void printArrayInBinary(int[] array) {
        for (int i = 0; i < array.length; i++) {
            String binaryString = String.format("%32s", Integer.toBinaryString(array[i])).replace(' ', '0');
            System.out.println("Integer " + i + ": " + binaryString);
        }
    }

    /**
     * Utility function to initialize an array with random integers between 0 and Integer.MAX_VALUE.
     *
     * @param array array to initialize
     */
    public static void initializeArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * Integer.MAX_VALUE);
        }
    }

    /**
     * Utility function to initialize an array with random integers between minValue and maxValue.
     *
     * @param array array to initialize
     * @param minValue the minimum value of the random integers (inclusive)
     * @param maxValue the maximum value of the random integers (inclusive)
     */
    public static void initializeArray(int[] array, int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue must be less than or equal to maxValue.");
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * (maxValue - minValue + 1)) + minValue;
        }
    }
}
