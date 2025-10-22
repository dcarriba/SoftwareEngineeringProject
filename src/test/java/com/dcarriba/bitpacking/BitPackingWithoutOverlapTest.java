package com.dcarriba.bitpacking;

import com.dcarriba.utilities.Utilities;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BitPackingWithoutOverlapTest {
    private static final boolean DEBUG_PRINT = false;

    @Test
    void testCompressionDecompressionSmallValueArray() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] inputArray = {1, 2, 3, 4, 5};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        if (DEBUG_PRINT) {
            System.out.println("Test case: small value array:");
            Utilities.printOriginalAndCompressed(inputArray, bitPacking);
        }

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionLargeValueArray() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] inputArray = {10, 20, 30, 40, 50, 60, 70, 80, 90};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        if (DEBUG_PRINT) {
            System.out.println("LTest case: large value array:");
            Utilities.printOriginalAndCompressed(inputArray, bitPacking);
        }

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionSingleValueArray() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] inputArray = {15};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        if (DEBUG_PRINT) {
            System.out.println("Test case: single value array:");
            Utilities.printOriginalAndCompressed(inputArray, bitPacking);
        }

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionArrayWithZero() {
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] inputArray = {0, 1, 2, 3};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        if (DEBUG_PRINT) {
            System.out.println("Test case: array with 0:");
            Utilities.printOriginalAndCompressed(inputArray, bitPacking);
        }

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
    void testDecompressionWithoutCompression(){
        BitPacking bitPacking = new BitPackingWithoutOverlap();

        int[] array = new int[0];
        assertThrows(IllegalStateException.class, () -> bitPacking.decompress(array));
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
}
