package com.dcarriba.bitpacking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BitPackingWithoutOverlapTest {

    @Test
    void testCompressionDecompressionSmallValueArray() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] inputArray = {1, 2, 3, 4, 5};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        System.out.println("Test case: small value array:");
        printOriginalAndCompressed(inputArray, bitPacking);

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionLargeValueArray() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] inputArray = {10, 20, 30, 40, 50, 60, 70, 80, 90};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        System.out.println("LTest case: large value array:");
        printOriginalAndCompressed(inputArray, bitPacking);

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionSingleValueArray() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] inputArray = {15};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        System.out.println("Test case: single value array:");
        printOriginalAndCompressed(inputArray, bitPacking);

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionArrayWithZero() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] inputArray = {0, 1, 2, 3};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        System.out.println("Test case: array with 0:");
        printOriginalAndCompressed(inputArray, bitPacking);

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionEmptyArray() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] emptyArray = {};
        assertThrows(IllegalArgumentException.class, () -> bitPacking.compress(emptyArray));
    }

    @Test
    void testCompressionDecompressionNullArray() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        assertThrows(IllegalArgumentException.class, () -> bitPacking.compress(null));
    }

    @Test
    void testCompressionDecompressionNegativeValueArray() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] negativeArray = {-1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> bitPacking.compress(negativeArray));
    }

    @Test
    void testGetMethod() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        // Test case: Retrieve specific elements after compression
        int[] inputArray = {100, 200, 300, 400, 500};
        bitPacking.compress(inputArray);

        // Test for each element
        for (int i = 0; i < inputArray.length; i++) {
            assertEquals(inputArray[i], bitPacking.get(i), "Failed to get the element at index " + i);
        }
    }

    /**
     * Method to print original and compressed arrays in binary.
     *
     * @param originalArray the original array to print
     * @param bitPacking contains the compressed array to print
     */
    private void printOriginalAndCompressed(int[] originalArray, BitPacking bitPacking) {
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
    private void printArrayInBinary(int[] array) {
        for (int i = 0; i < array.length; i++) {
            String binaryString = String.format("%32s", Integer.toBinaryString(array[i])).replace(' ', '0');
            System.out.println("Integer " + i + ": " + binaryString);
        }
    }
}
