package com.dcarriba.bitpacking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BitPackingTest} provides unit tests for the calculateBitSize method of the abstract {@link BitPacking} class
 */
public class BitPackingTest {

    /**
     * Subclass needed to instantiate the abstract {@link BitPacking} class
     */
    private static class BitPackingSub extends BitPacking {

        @Override
        public void compress(int[] array) {}

        @Override
        public int get(int i) {
            return 0;
        }
    }

    @Test
    void testCalculateBitSizeWithOnlyZeros() {
        BitPackingSub bitPackingSub = new BitPackingSub();

        int[] array = {0, 0, 0};
        assertEquals(1, bitPackingSub.calculateBitSize(array));
    }

    @Test
    void testCalculateBitSizeWithSmallValue() {
        BitPackingSub bitPackingSub = new BitPackingSub();

        int[] array = {1, 2, 3, 7};
        assertEquals(3, bitPackingSub.calculateBitSize(array));
    }

    @Test
    void testCalculateBitSizeWithLargeValue() {
        BitPackingSub bitPackingSub = new BitPackingSub();

        int[] array = {0, 256, 512, 1023};
        assertEquals(10, bitPackingSub.calculateBitSize(array));
    }

    @Test
    void testCalculateBitSizeThrowsOnNegativeValue() {
        BitPackingSub bitPackingSub = new BitPackingSub();

        int[] array = {5, -3, 8};
        assertThrows(IllegalArgumentException.class, () -> bitPackingSub.calculateBitSize(array));
    }

    @Test
    void testCalculateBitSizeThrowsOnNullArray() {
        BitPackingSub bitPackingSub = new BitPackingSub();

        assertThrows(IllegalArgumentException.class, () -> bitPackingSub.calculateBitSize(null));
    }
}
