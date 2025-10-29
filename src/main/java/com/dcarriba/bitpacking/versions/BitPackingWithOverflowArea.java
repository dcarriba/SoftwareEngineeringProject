package com.dcarriba.bitpacking.versions;

import com.dcarriba.bitpacking.BitPacking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link BitPackingWithOverflowArea} is a {@link BitPacking} compression with overflow areas.
 * <p>
 * "[...] If a single number in the initial array requires a large number of bits k and the other numbers require
 *  k' bits with k' < k, it is a waste to represent all numbers with k bits. In this case, we can assign a special
 *  value to a compressed integer that indicates that the true value is located elsewhere at a certain position in
 *  my table, called the overflow area. <br>
 *  For example, if we want to encode the numbers 1, 2, 3, 1024, 4, 5, and 2048.
 *  We can encode 1, 2, 3, and 4 using 3 bits and the other numbers using 11 bits at the end. Since we don't want to
 *  lose direct access, we must precalculate the number of integers in the overflow area and then integrate this into
 *  our encoding. Here, we have 2 integers in the overflow area, which requires 1 bit to be encoded. We will use 1 bit
 *  of the encoding to express the fact that we are not directly representing a number but a position in the overflow
 *  area. If 1 corresponds to the overflow area, and x-y means that the first bit is x and the others are y, we will
 *  represent the sequence of numbers 1, 2, 3, 1024, 4, 5, 2048 as 0-1, 0-2, 0-3, 1-0, 0-4, 0-5, 1-1, 1024, 2048"
 *  - Jean-Charles Régin
 *  </p>
 */
public class BitPackingWithOverflowArea extends BitPacking {
    /** New number of bits each regular value will be coded on */
    private int regularValueBitSize;
    /** New number of bits each value inside the overflow area will be coded on */
    private int overflowAreaValueBitSize;
    /** New number of bits each index of overflow values will be coded on */
    private int overflowValueIndexBitSize;

    @Override
    public void compress(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array can't be null or empty.");
        }

        setOriginalLength(array.length);

        int maxValue = 0;
        for (int value : array) maxValue = Math.max(maxValue, value);

        overflowAreaValueBitSize = calculateBitSize(new int[]{maxValue});

        // A set to have a faster .contains() method
        Set<Integer> regularValues = new HashSet<>();
        // A list is needed to create the overflow area with values in order
        List<Integer> overflowAreaValues = new ArrayList<>();

        int maxRegularValueBitSize = 0;
        for (int value : array) {
            int valueBitSize = calculateBitSize(new int[]{value});
            if (valueBitSize < overflowAreaValueBitSize) {
                maxRegularValueBitSize = Math.max(maxRegularValueBitSize, valueBitSize);
                regularValues.add(value);
            } else {
                overflowAreaValues.add(value);
            }
        }

        regularValueBitSize = maxRegularValueBitSize;

        overflowValueIndexBitSize = calculateBitSize(new int[]{overflowAreaValues.size()-1});

        // Total bits needed for all regular values, overflow references, and overflow values
        int totalBits = regularValues.size() * (regularValueBitSize + 1) +
                overflowAreaValues.size() * (1 + overflowValueIndexBitSize) +
                overflowAreaValues.size() * overflowAreaValueBitSize;

        // Round up to fit the total bits inside 32-bit integers
        int compressedArrayLength = (totalBits + 31) / 32;

        int[] compressedArray = new int[compressedArrayLength];

        int currentBit = 0;
        int overflowIndex = 0;

        // Pass 1: compresses the regular values and overflow references
        for (int value : array) {
            int remainingBits;

            // Determines which integer of compressedArray the current value should be in
            int intIndex = currentBit / 32;

            // Calculates the bit offset (i.e. the position) for the current value inside the integer
            int bitOffset = currentBit % 32;

            if (regularValues.contains(value)) { // Process regular values
                remainingBits = regularValueBitSize;

                // Update current bit by +1 to leave place for the leading 0
                currentBit += 1;

                // Update intIndex and bitOffset since currentBit just changed
                intIndex = currentBit / 32;
                bitOffset = currentBit % 32;

                // Compresses the regular value and adds it to the compressed array
                while (remainingBits > 0) {
                    int bitsToWrite = Math.min(remainingBits, 32 - bitOffset);
                    int valueToWrite = (value >> (remainingBits - bitsToWrite)) & ((1 << bitsToWrite) - 1);
                    compressedArray[intIndex] |= valueToWrite << (32 - (bitOffset + bitsToWrite));

                    remainingBits -= bitsToWrite;
                    currentBit += bitsToWrite;

                    if (remainingBits > 0) {
                        intIndex++;
                        bitOffset = 0;
                    }
                }
            }

            else if (overflowAreaValues.contains(value)) { // Process overflow reference
                remainingBits = overflowValueIndexBitSize;

                // Inserts the leading 1 for overflow reference
                compressedArray[intIndex] |= (1 << (32 - (bitOffset + 1)));
                currentBit += 1;

                // Update intIndex and bitOffset since currentBit just changed
                intIndex = currentBit / 32;
                bitOffset = currentBit % 32;

                // Compresses the overflow index and adds it to the compressed array
                while (remainingBits > 0) {
                    int bitsToWrite = Math.min(remainingBits, 32 - bitOffset);
                    int valueToWrite = (overflowIndex >> (remainingBits - bitsToWrite)) & ((1 << bitsToWrite) - 1);
                    compressedArray[intIndex] |= valueToWrite << (32 - (bitOffset + bitsToWrite));

                    remainingBits -= bitsToWrite;
                    currentBit += bitsToWrite;

                    if (remainingBits > 0) {
                        intIndex++;
                        bitOffset = 0;
                    }
                }

                overflowIndex++;
            }

        }

        // Pass 2: adds the overflow area at the end of the compressed array by compressing the overflow values
        for (int overflowValue : overflowAreaValues) {
            int remainingBits = overflowAreaValueBitSize;

            int intIndex = currentBit / 32;
            int bitOffset = currentBit % 32;

            // Compresses and adds the overflow value to the compressed array
            while (remainingBits > 0) {
                int bitsToWrite = Math.min(remainingBits, 32 - bitOffset);
                int valueToWrite = (overflowValue >> (remainingBits - bitsToWrite)) & ((1 << bitsToWrite) - 1);
                compressedArray[intIndex] |= valueToWrite << (32 - (bitOffset + bitsToWrite));

                remainingBits -= bitsToWrite;
                currentBit += bitsToWrite;

                if (remainingBits > 0) {
                    intIndex++;
                    bitOffset = 0;
                }
            }
        }

        setCompressedArray(compressedArray);
    }

    @Override
    public int get(int i){
        return 0;
    }
}
