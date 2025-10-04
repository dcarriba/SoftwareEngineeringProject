package com.dcarriba.bitpacking;

/**
 * Abstract {@link BitPacking} class, a compression method based the number of bits used.
 * Compression and decompression methods need to be implemented.
 */
public abstract class BitPacking {
    /** Array containing the compressed data */
    private int[] compressedArray;
    /** Original number of integers in the array before compression */
    private int originalLength;
    /** New number of bits each number will be coded on */
    private int bitSize;

    /**
     * @return Array containing the compressed data
     */
    public int[] getCompressedArray() {
        return compressedArray;
    }

    /**
     * @param compressedArray Array containing the compressed data
     */
    protected void setCompressedArray(int[] compressedArray) {
        this.compressedArray = compressedArray;
    }

    /**
     * @return Original number of integers in the array before compression
     */
    public int getOriginalLength() {
        return originalLength;
    }

    /**
     * @param originalLength Original number of integers in the array before compression
     */
    protected void setOriginalLength(int originalLength) {
        this.originalLength = originalLength;
    }

    /**
     * @return New number of bits each number will be coded on
     */
    public int getBitSize() {
        return bitSize;
    }

    /**
     * @param bitSize New number of bits each number will be coded on
     */
    protected void setBitSize(int bitSize) {
        this.bitSize = bitSize;
    }

    /**
     * Calculates the number of bits needed to represent the largest integer of the array.
     *
     * @param array The input array with only non-negative integers
     * @return The number of bits required to represent the largest value
     * @throws IllegalArgumentException if the array is null or contains negative numbers
     */
    protected int calculateBitSize(int[] array) {
        if (array == null) throw new IllegalArgumentException("Input array can't be null.");
        int max = 1; // At least 1 bit, needed if all values are "0"
        for (int value : array) {
            if (value < 0) throw new IllegalArgumentException("Values can't be negative.");
            max = Math.max(max, value);
        }
        return 32 - Integer.numberOfLeadingZeros(max);
    }

    /**
     * Compresses the array using the BitPacking compression method.
     *
     * @param array Array to be compressed
     */
    public abstract void compress(int[] array);

    /**
     * Decompresses the compressed array into the array given as parameter.
     *
     * @param array Array receiving the decompressed array
     */
    public abstract void decompress(int[] array);

    /**
     * Returns the value of the i-th element in the compressed array.
     *
     * @param i index of the wanted element
     * @return value of the i-th element
     */
    public abstract int get(int i);
}
