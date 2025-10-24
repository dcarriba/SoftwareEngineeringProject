package com.dcarriba.bitpacking;

/**
 * {@link BitPackingWithoutOverlap} is a {@link BitPacking} implementation where compressed values
 * do not overlap to the next integer, i.e. compressed values are never written on two consecutive
 * integers inside the compressed array.
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

        // Number of values that can fit in a single integer
        int valuesPerInt = 32 / bitSize;

        // Number of integers needed for all compressed values
        int compressedArrayLength = (array.length + valuesPerInt - 1) / valuesPerInt;

        int[] compressedArray = new int[compressedArrayLength];

        for (int i = 0; i < array.length; i++) {
            int value = array[i];

            // Determines which integer of compressedArray the current value should be in
            int intIndex = i / valuesPerInt;

            // Calculates the bit offset (i.e. the position) for the current value inside the integer
            int offset = (i % valuesPerInt) * bitSize;

            // Adds the compressed value to the integer without affecting previously compressed and added
            // values to this integer
            compressedArray[intIndex] |= (value & ((1 << bitSize) - 1)) << (32 - (offset + bitSize));
        }

        setCompressedArray(compressedArray);
    }

    @Override
    public void decompress(int[] array) {
        if (array == null || array.length != getOriginalLength()) {
            throw new IllegalArgumentException("Output array must have length " + getOriginalLength());
        }

        if (super.getCompressedArray() == null) {
            throw new IllegalStateException("Compressed data is not available. Ensure that compression has been" +
                                            "performed before decompression.");
        }

        int bitSize = getBitSize();
        int valuesPerInt = 32 / bitSize;
        int[] compressed = getCompressedArray();

        // We get all values from the compressed array and put then into the result array given as parameter
        for (int i = 0; i < array.length; i++) {
            int intIndex = i / valuesPerInt;
            int offset = (i % valuesPerInt) * bitSize;

            array[i] = (compressed[intIndex] >>> (32 - (offset + bitSize))) & ((1 << bitSize) - 1);
        }
    }

    @Override
    public int get(int i) {
        if (i < 0 || i >= getOriginalLength()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        int bitSize = getBitSize();
        int valuesPerInt = 32 / bitSize;
        int intIndex = i / valuesPerInt;
        int offset = (i % valuesPerInt) * bitSize;

        return (getCompressedArray()[intIndex] >>> (32 - (offset + bitSize))) & ((1 << bitSize) - 1);
    }
}
