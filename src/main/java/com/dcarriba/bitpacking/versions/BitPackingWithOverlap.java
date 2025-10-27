package com.dcarriba.bitpacking.versions;

import com.dcarriba.bitpacking.BitPacking;

/**
 * {@link BitPackingWithOverlap} is a {@link BitPacking} implementation where compressed values can overlap to
 * the next integer, i.e. compressed values always fill the 32-bit integers of the compressed array and therefore
 * compressed values may be written on two consecutive integers.
 */
public class BitPackingWithOverlap extends BitPacking {

    @Override
    public void compress(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array can't be null or empty.");
        }

        // Number of bits needed to represent the largest value of the array
        int bitSize = calculateBitSize(array);

        setBitSize(bitSize);
        setOriginalLength(array.length);

        // Number of 32-bit integers needed for all compressed values
        int compressedArrayLength = (array.length * bitSize + 31) / 32;

        int[] compressedArray = new int[compressedArrayLength];

        // Current position in the compressed array (in bits)
        int currentBit = 0;

        for (int value : array) {
            int remainingBits = bitSize;

            // Determines which integer of compressedArray the current value should be in
            int intIndex = currentBit / 32;

            // Calculates the bit offset (i.e. the position) for the current value inside the integer
            int bitOffset = currentBit % 32;

            while (remainingBits > 0) {
                int bitsToWrite = Math.min(remainingBits, 32 - bitOffset);

                // Adds the compressed value to the integer without affecting previously compressed values
                int valueToWrite = (value >> (remainingBits - bitsToWrite)) & ((1 << bitsToWrite) - 1);
                compressedArray[intIndex] |= valueToWrite << (32 - (bitOffset + bitsToWrite));

                // Updates the remaining bits of the current value, as well as the current bit position
                remainingBits -= bitsToWrite;
                currentBit += bitsToWrite;

                // Moves to the next integer if the value didn't fit completely into the current integer
                if (remainingBits >= 0) {
                    intIndex++;
                    bitOffset = 0;
                }
            }
        }

        setCompressedArray(compressedArray);
    }

    @Override
    public int get(int i) {
        if (i < 0 || i >= getOriginalLength()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        int bitSize = getBitSize();
        int[] compressedArray = getCompressedArray();

        // Current position in the compressed array (in bits)
        int currentBit = i * bitSize;

        // Determines in which integer and at what position inside of it the value is stored in
        int intIndex = currentBit / 32;
        int bitOffset = currentBit % 32;

        // Initialize the value
        int value = 0;
        int remainingBits = bitSize;

        while (remainingBits > 0) {
            // Number of bits available to read in the current integer
            int bitsToRead = Math.min(remainingBits, 32 - bitOffset);

            // Extracts the bits of the wanted value and combines them to the initialized value variable
            int valueToRead = (compressedArray[intIndex] >>> (32 - bitOffset - bitsToRead)) & ((1 << bitsToRead) - 1);
            value |= valueToRead << (remainingBits - bitsToRead);

            // Updates the remaining bits to read, as well as the current bit position
            remainingBits -= bitsToRead;
            currentBit += bitsToRead;

            // Moves to the next integer if not all bits of the wanted value were read in the current integer
            if (remainingBits >= 0) {
                intIndex++;
                bitOffset = 0;
            }
        }

        return value;
    }
}
