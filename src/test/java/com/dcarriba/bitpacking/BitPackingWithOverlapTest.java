package com.dcarriba.bitpacking;

import com.dcarriba.config.Config;
import com.dcarriba.utilities.Utilities;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BitPackingWithOverlapTest} provides unit tests for {@link BitPackingWithOverlap}
 */
public class BitPackingWithOverlapTest {

    @Test
    void testCompressionDecompressionSmallValueArray() {
        BitPacking bitPacking = new BitPackingWithOverlap();

        int[] inputArray = {1, 2, 3, 4, 5};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        if (Config.UNIT_TEST_PRINT_DEBUG) {
            System.out.println("Test case: small value array:");
            Utilities.printOriginalAndCompressed(inputArray, bitPacking);
        }

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionLargeValueArray() {
        BitPacking bitPacking = new BitPackingWithOverlap();

        int[] inputArray = {100, 200, 300, 400, 500, 600, 700, 800, 900};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        if (Config.UNIT_TEST_PRINT_DEBUG) {
            System.out.println("Test case: large value array:");
            Utilities.printOriginalAndCompressed(inputArray, bitPacking);
        }

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionSingleValueArray() {
        BitPacking bitPacking = new BitPackingWithOverlap();

        int[] inputArray = {15};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        if (Config.UNIT_TEST_PRINT_DEBUG) {
            System.out.println("Test case: single value array:");
            Utilities.printOriginalAndCompressed(inputArray, bitPacking);
        }

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionArrayWithZero() {
        BitPacking bitPacking = new BitPackingWithOverlap();

        int[] inputArray = {0, 1, 2, 3};
        int[] decompressedArray = new int[inputArray.length];

        bitPacking.compress(inputArray);
        bitPacking.decompress(decompressedArray);

        if (Config.UNIT_TEST_PRINT_DEBUG) {
            System.out.println("Test case: array with 0:");
            Utilities.printOriginalAndCompressed(inputArray, bitPacking);
        }

        assertArrayEquals(inputArray, decompressedArray);
    }

    @Test
    void testCompressionDecompressionEmptyArray() {
        BitPacking bitPacking = new BitPackingWithOverlap();

        int[] emptyArray = {};
        assertThrows(IllegalArgumentException.class, () -> bitPacking.compress(emptyArray));
    }

    @Test
    void testCompressionDecompressionNullArray() {
        BitPacking bitPacking = new BitPackingWithOverlap();

        assertThrows(IllegalArgumentException.class, () -> bitPacking.compress(null));
    }

    @Test
    void testCompressionDecompressionNegativeValueArray() {
        BitPacking bitPacking = new BitPackingWithOverlap();

        int[] negativeArray = {-1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> bitPacking.compress(negativeArray));
    }

    @Test
    void testDecompressionWithoutCompression(){
        BitPacking bitPacking = new BitPackingWithOverlap();

        int[] array = new int[0];
        assertThrows(IllegalStateException.class, () -> bitPacking.decompress(array));
    }

    @Test
    void testGetMethod() {
        BitPacking bitPacking = new BitPackingWithOverlap();

        int[] inputArray = {100, 200, 300, 400, 500};
        bitPacking.compress(inputArray);

        // Test for each element
        for (int i = 0; i < inputArray.length; i++) {
            assertEquals(inputArray[i], bitPacking.get(i), "Failed to get the element at index " + i);
        }
    }
}
