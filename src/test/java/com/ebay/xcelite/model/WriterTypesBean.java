package com.ebay.xcelite.model;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.*;

@Data
public class WriterTypesBean {

    @Column(name = "shortSimpleType")
    short   shortSimpleType     = 1231;

    @Column(name = "integerSimpleType")
    int     integerSimpleType   = 1232;

    @Column(name = "longSimpleType")
    long    longSimpleType      = 1233;

    @Column(name = "shortObjectType")
    Short   shortObjectType     = 1234;

    @Column(name = "integerObjectType")
    Integer integerObjectType   = 1235;

    @Column(name = "longObjectType")
    Long    longObjectType      = 1236L;

    @Column(name = "bigIntegerType")
    BigInteger bigIntegerType   = BigInteger.valueOf(1237);

    @Column(name = "integerAtomicType")
    AtomicInteger integerAtomicType = new AtomicInteger(1238);

    @Column(name = "longAtomicType")
    AtomicLong longAtomicType   = new AtomicLong(1239);

    @Column(name = "longAccumulatorType")
    LongAccumulator longAccumulatorType = new LongAccumulator(Long::sum, 345);

    @Column(name = "longAdderType")
    LongAdder longAdderType     = new LongAdder();

    @Column(name = "floatSimpleType")
    float   floatSimpleType     = 123.456F;

    @Column(name = "doubleSimpleType")
    double  doubleSimpleType    = 123.234;

    @Column(name = "floatObjectType")
    Float   floatObjectType     = 123.345F;

    @Column(name = "doubleObjectType")
    Double  doubleObjectType    = 123.345;

    @Column(name = "bigDecimalType")
    BigDecimal bigDecimalType   = BigDecimal.valueOf(123.567);

    @Column(name = "doubleAccumulatorType")
    DoubleAccumulator doubleAccumulatorType = new DoubleAccumulator(Double::sum, 123.789);

    @Column(name = "doubleAdderType")
    DoubleAdder doubleAdderType = new DoubleAdder();

    @Column(name = "numberType")
    Number numberType   = BigDecimal.valueOf(123.678);

    public LongAdder getLongAdderType() {
        longAdderType.add(1240L);
        return longAdderType;
    }

    public DoubleAdder getDoubleAdderType() {
        doubleAdderType.add(123.89);
        return doubleAdderType;
    }
}
