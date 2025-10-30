package com.dcarriba.bitpacking.versions;

import com.dcarriba.bitpacking.BitPacking;

import java.util.ArrayList;
import java.util.List;

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

        List<Integer> regularValues = new ArrayList<>();
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

        overflowValueIndexBitSize = calculateBitSize(new int[]{overflowAreaValues.size() - 1});

        int totalBits = array.length                                        // 1 leading bit per value
                + (regularValues.size() * regularValueBitSize)              // bits for the regular values
                + (overflowAreaValues.size() * overflowValueIndexBitSize)   // bits for the overflow indices
                + (overflowAreaValues.size() * overflowAreaValueBitSize);   // bits for the overflow area

        // Round up to fit the total bits inside 32-bit integers
        int compressedArrayLength = ((totalBits + 31) / 32);

        int[] compressedArray = new int[compressedArrayLength];

        // Current position in the compressed array (in bits)
        int currentBit = 0;

        // Used to create the overflow references later (in form 1-overflowIndex)
        int overflowIndex = 0;

        // Pass 1: compresses the regular values and overflow references
        for (int value : array) {
            int remainingBits;

            // Determines which integer of compressedArray the current value should be in
            int intIndex = currentBit / 32;

            // Calculates the bit offset (i.e. the position) for the current value inside the integer
            int bitOffset = currentBit % 32;

            // Process regular values
            if (regularValues.contains(value)) {
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

            // Process overflow reference
            else if (overflowAreaValues.contains(value)) {
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
        for (int value : overflowAreaValues) {
            int remainingBits = overflowAreaValueBitSize;

            int intIndex = currentBit / 32;
            int bitOffset = currentBit % 32;

            // Compresses and adds the overflow value to the compressed array
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

        setCompressedArray(compressedArray);
    }

    @Override
    public void decompress(int[] array) {
        if (array == null || array.length != getOriginalLength()) {
            throw new IllegalArgumentException("The array must be of the correct size to hold the decompressed data.");
        }

        int[] compressedArray = getCompressedArray();

        if (compressedArray == null) {
            throw new IllegalStateException("Compressed data is not available. Ensure that compression has been" +
                    "performed before decompression.");
        }

        // List to contain the indexes where an overflow value is located
        List<Integer> indexesForOverflowValues = new ArrayList<>();

        int currentBit = 0;

        // Pass 1: decompresses the regular values and saves the indexes for the overflow values
        for (int i = 0; i < array.length; i++) {

            // Determines in which integer (of the compressed array) and at what position, inside of that integer,
            // the wanted value is stored in
            int intIndex = currentBit / 32;
            int bitOffset = currentBit % 32;

            // Get the leading bit (0 or 1)
            int leadingBit = (compressedArray[intIndex] >>> (32 - (bitOffset + 1))) & 1;

            // Update current bit by +1 after getting the leading bit
            currentBit += 1;

            // Update intIndex and bitOffset since currentBit just changed
            intIndex = currentBit / 32;
            bitOffset = currentBit % 32;

            // If it is a regular value
            if (leadingBit == 0) {
                int remainingBits = regularValueBitSize;
                int value = 0;

                // Decompresses the regular value
                while (remainingBits > 0) {
                    int bitsToRead = Math.min(remainingBits, 32 - bitOffset);

                    int valueToRead = (compressedArray[intIndex] >>> (32 - bitOffset - bitsToRead))
                            & ((1 << bitsToRead) - 1);
                    value |= valueToRead << (remainingBits - bitsToRead);

                    remainingBits -= bitsToRead;
                    currentBit += bitsToRead;

                    if (remainingBits >= 0) {
                        intIndex++;
                        bitOffset = 0;
                    }
                }

                array[i] = value;
            }

            // Else it is an overflow reference
            else {
                int remainingBits = overflowValueIndexBitSize;

                // Updates the currentBit variable, needed to continue the decompression
                while (remainingBits > 0) {
                    int bitsToRead = Math.min(remainingBits, 32 - bitOffset);
                    remainingBits -= bitsToRead;
                    currentBit += bitsToRead;
                    if (remainingBits >= 0) bitOffset = 0;
                }

                // We save the index at which an overflow value is located
                indexesForOverflowValues.add(i);
            }
        }

        // Pass 2: Decompress the overflow values inside the overflow area, and places them in the correct index
        // (which is given by overflowValueIndex)
        for (int overflowValueIndex : indexesForOverflowValues) {
            int remainingBits = overflowAreaValueBitSize;
            int overflowValue = 0;

            int intIndex = currentBit / 32;
            int bitOffset = currentBit % 32;

            // Decompresses the overflow value
            while (remainingBits > 0) {
                int bitsToRead = Math.min(remainingBits, 32 - bitOffset);

                int valueToRead = (compressedArray[intIndex] >>> (32 - bitOffset - bitsToRead))
                        & ((1 << bitsToRead) - 1);
                overflowValue |= valueToRead << (remainingBits - bitsToRead);

                remainingBits -= bitsToRead;
                currentBit += bitsToRead;

                if (remainingBits >= 0) {
                    intIndex++;
                    bitOffset = 0;
                }
            }

            // Places the overflow value at the right index of the array
            array[overflowValueIndex] = overflowValue;
        }
    }

    @Override
    public int get(int i){
        int[] decompressedArray = new int[getOriginalLength()];
        decompress(decompressedArray);

        return decompressedArray[i];
    }
}
