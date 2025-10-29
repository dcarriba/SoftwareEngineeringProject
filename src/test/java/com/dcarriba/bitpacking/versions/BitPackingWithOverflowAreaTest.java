package com.dcarriba.bitpacking.versions;

import com.dcarriba.bitpacking.BitPacking;
import com.dcarriba.bitpacking.factory.BitPackingFactory;
import com.dcarriba.bitpacking.factory.CompressionVersion;

/**
 * {@link BitPackingWithOverflowAreaTest} provides unit tests for {@link BitPackingWithOverflowArea}
 * and extends {@link BitPackingVersionsBaseTest} for the test logic
 */
class BitPackingWithOverflowAreaTest extends BitPackingVersionsBaseTest {

    @Override
    protected BitPacking createBitPacking() {
        return BitPackingFactory.createBitPacking(CompressionVersion.WITH_OVERFLOW_AREA);
    }
}