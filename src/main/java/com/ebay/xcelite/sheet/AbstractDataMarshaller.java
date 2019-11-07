package com.ebay.xcelite.sheet;

import com.ebay.xcelite.options.XceliteOptions;

public abstract class AbstractDataMarshaller implements DataMarshaller {
    protected XceliteOptions options;

    /**
     * Returns whether a header row should be written.
     * Works by checking whether the current valid options
     * mandate or forbid a header row. If the options aren't
     * configured in that regard (null return value), then
     * ask the SheetReader/SheetWriter whether a header row
     * should be written (Beanreader/writer: yes,
     * Simplereader/writer: no)
     *
     * @return Whether a header row is expected
     */
    @Override
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
