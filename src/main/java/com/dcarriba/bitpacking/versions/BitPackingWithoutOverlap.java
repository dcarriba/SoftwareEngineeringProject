package com.dcarriba.bitpacking.versions;

import com.dcarriba.bitpacking.BitPacking;

/**
 * {@link BitPackingWithoutOverlap} is a {@link BitPacking} implementation where compressed values do not overlap to
 * the next integer, i.e. compressed values are never written on two consecutive integers inside the compressed array.
 */
public class BitPackingWithoutOverlap extends BitPacking {

    @Override
    public void compress(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array can't be null or empty.");
        }

        // Number of bits needed to represent the largest value of the array
        int bitSize = calculateBitSize(array);

        setBitSize(bitSize);
        setOriginalLength(array.length);

        // Number of values that can fit in a single 32-bit integer
        int valuesPerInt = 32 / bitSize;

        // Number of 32-bit integers needed for all compressed values
        int compressedArrayLength = (array.length + valuesPerInt - 1) / valuesPerInt;

        int[] compressedArray = new int[compressedArrayLength];

        for (int i = 0; i < array.length; i++) {
            int value = array[i];

            // Determines which integer of compressedArray the current value should be in
            int intIndex = i / valuesPerInt;

            // Calculates the bit offset (i.e. the position) for the current value inside the integer
            int bitOffset = (i % valuesPerInt) * bitSize;

            // Adds the compressed value to the integer without affecting previously compressed values
            compressedArray[intIndex] |= (value & ((1 << bitSize) - 1)) << (32 - (bitOffset + bitSize));
        }

        setCompressedArray(compressedArray);
    }

    @Override
    public int get(int i) {
        if (i < 0 || i >= getOriginalLength()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        int bitSize = getBitSize();
        int valuesPerInt = 32 / bitSize;

        // Calculates in which integer of the compressed array the i-th value is contained in
        int intIndex = i / valuesPerInt;

        // Calculates the bit offset at which the wanted value is
        int bitOffset = (i % valuesPerInt) * bitSize;

        // Returns the wanted value as a normal 32-bit integer
        return (getCompressedArray()[intIndex] >>> (32 - (bitOffset + bitSize))) & ((1 << bitSize) - 1);
    }
}
