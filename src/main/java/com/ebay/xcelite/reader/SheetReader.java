/*
  Copyright [2013-2014] eBay Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.ebay.xcelite.reader;

import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;

import java.util.Collection;

/**
 * Generic interface for reader classes that can deserialize Excel workbooks
 * to collections of Java objects.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 */

public interface SheetReader<T> {

  /**
   * Reads the sheet and returns a collection of the specified type.
   *
   * @return collection of the specified type
   * @since 1.0
   */
  Collection<T> read();

  /**
   * Whether to skip the first row or not when reading the sheet.
   * @deprecated since 1.2. Use {@link #getOptions()} instead and set
   * {@link XceliteOptions#setSkipRowsBeforeColumnDefinitionRow(Integer) setSkipLinesBeforeHeader}
   * to 1
   *
   * @param skipHeaderRow true to skip the header row, false otherwise
   */
  @Deprecated
  void skipHeaderRow(boolean skipHeaderRow);

  /**
   * Gets the {@link XceliteOptions} object used to configure the reader's
   * behavior
   * @return configuration object
   * @since 1.2
   */
  XceliteOptions getOptions();

  /**
   * Sets the {@link XceliteOptions} object used to configure the reader's
   * behavior
   * @param options configuration object
   * @since 1.2
   */
  void setOptions(XceliteOptions options);


  /**
   * Gets the {@link XceliteSheet} object this reader is operating on
   * @return sheet this reader reads from
   */
  XceliteSheet getSheet();

  /**
   * Adds a row post processor. The row post processors will be executed in
   * insertion order.
   *
   * @param rowPostProcessor the post row processor to add
   */
  void addRowPostProcessor(RowPostProcessor<T> rowPostProcessor);

  /**
   * Removes a row post processor.
   *
   * @param rowPostProcessor the post row processor to remove
   */
  void removeRowPostProcessor(RowPostProcessor<T> rowPostProcessor);
}
