package com.dcarriba.bitpacking;

import com.dcarriba.bitpacking.factory.BitPackingFactory;
import com.dcarriba.bitpacking.factory.CompressionVersion;

/**
 * {@link BitPackingWithoutOverlapTest} provides unit tests for {@link BitPackingWithoutOverlap}
 * and extends {@link BitPackingVersionsBaseTest} for the test logic
 */
public class BitPackingWithoutOverlapTest extends BitPackingVersionsBaseTest {

    @Override
    BitPacking createBitPacking() {
        return BitPackingFactory.createBitPacking(CompressionVersion.WITHOUT_OVERLAP);
    }
}
