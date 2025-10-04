package com.dcarriba.bitpacking;

/**
 * Abstract {@link BitPacking} class, a compression method based the number of bits used.
 * Compression and decompression methods need to be implemented.
 */
public abstract class BitPacking {
    /** Array containing the compressed data */
    private int[] compressedArray;
    /** Original number of elements in the array */
    private int originalLength;
    /** New number of bits for each element */
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
     * @return Original number of elements in the array
     */
    public int getOriginalLength() {
        return originalLength;
    }

    /**
     * @param originalLength Original number of elements in the array
     */
    protected void setOriginalLength(int originalLength) {
        this.originalLength = originalLength;
    }

    /**
     * @return New number of bits for each element
     */
    public int getBitSize() {
        return bitSize;
    }

    /**
     * @param bitSize New number of bits for each element
     */
    protected void setBitSize(int bitSize) {
        this.bitSize = bitSize;
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
     * @param array Array to get the results
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
