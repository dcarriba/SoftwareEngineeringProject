package com.dcarriba.bitpacking;

import com.dcarriba.bitpacking.factory.BitPackingFactory;
import com.dcarriba.bitpacking.factory.CompressionVersion;

/**
 * {@link BitPackingWithOverlapTest} provides unit tests for {@link BitPackingWithOverlap}
 * and extends {@link BitPackingVersionsBaseTest} for the test logic
 */
public class BitPackingWithOverlapTest extends BitPackingVersionsBaseTest {

    @Override
    BitPacking createBitPacking() {
        return BitPackingFactory.createBitPacking(CompressionVersion.WITH_OVERLAP);
    }
}
