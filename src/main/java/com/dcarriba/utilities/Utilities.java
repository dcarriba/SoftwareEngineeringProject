package com.dcarriba.utilities;

import com.dcarriba.bitpacking.BitPacking;

public class Utilities {

    /**
     * Method to print original and compressed arrays in binary.
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
     * Method to print the binary representation of each integer of an array.
     *
     * @param array array to print
     */
    public static void printArrayInBinary(int[] array) {
        for (int i = 0; i < array.length; i++) {
            String binaryString = String.format("%32s", Integer.toBinaryString(array[i])).replace(' ', '0');
            System.out.println("Integer " + i + ": " + binaryString);
        }
    }
}
