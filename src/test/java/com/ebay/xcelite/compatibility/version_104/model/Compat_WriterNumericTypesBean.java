package com.ebay.xcelite.compatibility.version_104.model;

import compat.com.ebay.xcelite_104.annotations.Compat_Column;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Compat_WriterNumericTypesBean {

    @Compat_Column(name = "shortSimpleType")
    short   shortSimpleType     = 1231;

    @Compat_Column(name = "integerSimpleType")
    int     integerSimpleType   = 1232;

    @Compat_Column(name = "longSimpleType")
    long    longSimpleType      = 1233;

    @Compat_Column(name = "shortObjectType")
    Short   shortObjectType     = 1234;

    @Compat_Column(name = "integerObjectType")
    Integer integerObjectType   = 1235;

    @Compat_Column(name = "longObjectType")
    Long    longObjectType      = 1236L;

    @Compat_Column(name = "bigIntegerType")
    BigInteger bigIntegerType   = BigInteger.valueOf(1237);

    @Compat_Column(name = "integerAtomicType")
    AtomicInteger integerAtomicType = new AtomicInteger(1238);

    @Compat_Column(name = "longAtomicType")
    AtomicLong longAtomicType   = new AtomicLong(1239);

    @Compat_Column(name = "floatSimpleType")
    float   floatSimpleType     = 123.456F;

    @Compat_Column(name = "doubleSimpleType")
    double  doubleSimpleType    = 123.234;

    @Compat_Column(name = "floatObjectType")
    Float   floatObjectType     = 123.345F;

    @Compat_Column(name = "doubleObjectType")
    Double  doubleObjectType    = 123.345;

    @Compat_Column(name = "bigDecimalType")
    BigDecimal bigDecimalType   = BigDecimal.valueOf(123.567);

    @Compat_Column(name = "numberType")
    Number numberType   = BigDecimal.valueOf(123.678);

    public short getShortSimpleType() {
        return shortSimpleType;
    }

    public void setShortSimpleType(short shortSimpleType) {
        this.shortSimpleType = shortSimpleType;
    }

    public int getIntegerSimpleType() {
        return integerSimpleType;
    }

    public void setIntegerSimpleType(int integerSimpleType) {
        this.integerSimpleType = integerSimpleType;
    }

    public long getLongSimpleType() {
        return longSimpleType;
    }

    public void setLongSimpleType(long longSimpleType) {
        this.longSimpleType = longSimpleType;
    }

    public Short getShortObjectType() {
        return shortObjectType;
    }

    public void setShortObjectType(Short shortObjectType) {
        this.shortObjectType = shortObjectType;
    }

    public Integer getIntegerObjectType() {
        return integerObjectType;
    }

    public void setIntegerObjectType(Integer integerObjectType) {
        this.integerObjectType = integerObjectType;
    }

    public Long getLongObjectType() {
        return longObjectType;
    }

    public void setLongObjectType(Long longObjectType) {
        this.longObjectType = longObjectType;
    }

    public BigInteger getBigIntegerType() {
        return bigIntegerType;
    }

    public void setBigIntegerType(BigInteger bigIntegerType) {
        this.bigIntegerType = bigIntegerType;
    }

    public AtomicInteger getIntegerAtomicType() {
        return integerAtomicType;
    }

    public void setIntegerAtomicType(AtomicInteger integerAtomicType) {
        this.integerAtomicType = integerAtomicType;
    }

    public AtomicLong getLongAtomicType() {
        return longAtomicType;
    }

    public void setLongAtomicType(AtomicLong longAtomicType) {
        this.longAtomicType = longAtomicType;
    }

    public float getFloatSimpleType() {
        return floatSimpleType;
    }

    public void setFloatSimpleType(float floatSimpleType) {
        this.floatSimpleType = floatSimpleType;
    }

    public double getDoubleSimpleType() {
        return doubleSimpleType;
    }

    public void setDoubleSimpleType(double doubleSimpleType) {
        this.doubleSimpleType = doubleSimpleType;
    }

    public Float getFloatObjectType() {
        return floatObjectType;
    }

    public void setFloatObjectType(Float floatObjectType) {
        this.floatObjectType = floatObjectType;
    }

    public Double getDoubleObjectType() {
        return doubleObjectType;
    }

    public void setDoubleObjectType(Double doubleObjectType) {
        this.doubleObjectType = doubleObjectType;
    }

    public BigDecimal getBigDecimalType() {
        return bigDecimalType;
    }

    public void setBigDecimalType(BigDecimal bigDecimalType) {
        this.bigDecimalType = bigDecimalType;
    }

    public Number getNumberType() {
        return numberType;
    }

    public void setNumberType(Number numberType) {
        this.numberType = numberType;
    }
}
