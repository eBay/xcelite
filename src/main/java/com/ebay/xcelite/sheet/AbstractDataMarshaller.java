package com.ebay.xcelite.sheet;

import com.ebay.xcelite.options.XceliteOptions;

public abstract class AbstractDataMarshaller implements DataMarshaller {
    protected XceliteOptions options;

    public boolean hasHeaderRow() {
        if (null != options.isHasHeaderRow()) {
            return options.isHasHeaderRow();
        }
        return expectsHeaderRow();
    }

    @Override
    public XceliteOptions getOptions() {
        return options;
    }

    @Override
    public void setOptions(XceliteOptions options) {
        this.options = new XceliteOptions(options);
    }
}
