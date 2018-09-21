package com.ebay.xcelite.sheet;

import com.ebay.xcelite.options.XceliteOptions;

/**
 * Generic base interface for configurable reader and writer classes,
 * extended by {@link com.ebay.xcelite.reader.SheetReader} and
 * {@link com.ebay.xcelite.writer.SheetWriter}
 *
 * @author Johannes
 * @since 1.2
 */
public interface DataMarshaller {

    /**
     * Does this reader or writer expect the spreadsheet to have
     * a header row containing column names?
     * @return Whether a header row is expected
     */
    boolean expectsHeaderRow();

    /**
     * In the current, concrete usage, has the user overridden
     * {@link #expectsHeaderRow()} via {@link XceliteOptions#setHasHeaderRow(boolean)}?
     * If yes, use the setting from options, if no, use the default
     * setting for the reader/writer
     * @return whether the current {@link XceliteSheet} has a header row
     */
    boolean hasHeaderRow();

    /**
     * Gets the {@link XceliteOptions} object used to configure the writer's
     * behavior
     * @return configuration object
     * @since 1.2
     */
    XceliteOptions getOptions();

    /**
     * Sets the {@link XceliteOptions} object used to configure the writer's
     * behavior
     * @param options configuration object
     * @since 1.2
     */
    void setOptions(XceliteOptions options);
}
