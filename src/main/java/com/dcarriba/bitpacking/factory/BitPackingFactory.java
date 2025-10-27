package com.dcarriba.bitpacking.factory;

import com.dcarriba.bitpacking.BitPacking;
import com.dcarriba.bitpacking.versions.BitPackingWithOverlap;
import com.dcarriba.bitpacking.versions.BitPackingWithoutOverlap;

/**
 * {@link BitPackingFactory} is a factory to create a {@link BitPacking} object.
 */
public class BitPackingFactory {

    /**
     * Creates a new Bit Packing compression object based on the given parameter
     *
     * @param compressionVersion specifies with version of the Bit Packing compression should be created
     * @return the wanted Bit Packing compression object
     */
    public static BitPacking createBitPacking(CompressionVersion compressionVersion) {
        if (compressionVersion == null) {
            throw new IllegalArgumentException("compressionVersion can't be null");
        }

        switch (compressionVersion){
            case WITH_OVERLAP -> {
                return new BitPackingWithOverlap();
            }
            case WITHOUT_OVERLAP -> {
                return new BitPackingWithoutOverlap();
            }
            default -> throw new IllegalArgumentException("compressionVersion is not correct");
        }
    }
}
