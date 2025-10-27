package com.dcarriba.bitpacking.versions;

import com.dcarriba.bitpacking.BitPacking;
import com.dcarriba.bitpacking.factory.BitPackingFactory;
import com.dcarriba.bitpacking.factory.CompressionVersion;

/**
 * {@link BitPackingWithoutOverlapTest} provides unit tests for {@link BitPackingWithoutOverlap}
 * and extends {@link BitPackingVersionsBaseTest} for the test logic
 */
public class BitPackingWithoutOverlapTest extends BitPackingVersionsBaseTest {

    @Override
    protected BitPacking createBitPacking() {
        return BitPackingFactory.createBitPacking(CompressionVersion.WITHOUT_OVERLAP);
    }
}
