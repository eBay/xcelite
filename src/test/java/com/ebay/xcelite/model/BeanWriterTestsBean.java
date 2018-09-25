package com.ebay.xcelite.model;

import com.ebay.xcelite.annotations.Column;
import lombok.Data;

@Data
public class BeanWriterTestsBean {

    @Column(name = "LONG_STRING")
    private String longString;
}
