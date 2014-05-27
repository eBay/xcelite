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
package com.ebay.xcelite.sheet;

import java.io.File;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Sheet;

import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.reader.BeanSheetReader;
import com.ebay.xcelite.reader.SimpleSheetReader;
import com.ebay.xcelite.writer.SheetWriter;
import com.ebay.xcelite.writer.BeanSheetWriter;
import com.ebay.xcelite.writer.SimpleSheetWriter;

/**
 * Class description...
 * 
 * @author kharel (kharel@ebay.com)
 * @creation_date Nov 9, 2013
 * 
 */
public class XceliteSheetImpl implements XceliteSheet {

  private final Sheet sheet;
  private final File file;

  public XceliteSheetImpl(Sheet sheet) {
    this(sheet, null);
  }
  
  public XceliteSheetImpl(Sheet sheet, File file) {
    this.sheet = sheet;
    this.file = file;
  }
  
  @Override
  public Sheet getNativeSheet() {
    return sheet;
  }

  @Override
  public File getFile() {
    return file;
  }

  @Override
  public <T> SheetWriter<T> getBeanWriter(Class<T> type) {
    return new BeanSheetWriter<T>(this, type);
  }

  @Override
  public <T> SheetReader<T> getBeanReader(Class<T> type) {
    return new BeanSheetReader<T>(this, type);
  } 

  @Override
  public SimpleSheetWriter getSimpleWriter() {
    return new SimpleSheetWriter(this);
  }

  @Override
  public SheetReader<Collection<Object>> getSimpleReader() {
    return new SimpleSheetReader(this);
  }
}
