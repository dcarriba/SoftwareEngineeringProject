package com.dcarriba.bitpacking.versions;

import com.dcarriba.bitpacking.BitPacking;
import com.dcarriba.bitpacking.factory.BitPackingFactory;
import com.dcarriba.bitpacking.factory.CompressionVersion;

class BitPackingWithOverflowAreaTest extends BitPackingVersionsBaseTest {

    @Override
    protected BitPacking createBitPacking() {
        return BitPackingFactory.createBitPacking(CompressionVersion.WITH_OVERFLOW_AREA);
    }
}