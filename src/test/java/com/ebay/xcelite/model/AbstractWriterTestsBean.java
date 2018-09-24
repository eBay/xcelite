package com.ebay.xcelite.model;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

@Data
public class AbstractWriterTestsBean {

    @Column(name = "booleanSimpleType")
    boolean   booleanSimpleType = true;

    @Column(name = "booleanObjectType")
    Boolean   booleanObjectType = true;

    @Column(name = "stringType")
    String   stringType         = "abcde";

}
