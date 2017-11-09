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
package com.ebay.xcelite.styles;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

public final class CellStyles {
  
//  private final String DEFAULT_DATE_FORMAT = "ddd mmm dd hh:mm:ss yyy";
  private final String DEFAULT_DATE_FORMAT = "dd/mm/yyyy hh:mm:ss";
  
  private final Workbook wb;
  private CellStyle boldStyle;
  private CellStyle dateStyle;

  public CellStyles(Workbook wb) {
    this.wb = wb;
    initStyles();
  }

  private void initStyles() {
    createBoldStyle();
    createDateFormatStyle();
  }

  private void createBoldStyle() {
    boldStyle = wb.createCellStyle();
    Font font = wb.createFont();
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    boldStyle.setFont(font);    
  }
  
  private void createDateFormatStyle() {
    dateStyle = wb.createCellStyle();
    DataFormat df = wb.createDataFormat();
    dateStyle.setDataFormat(df.getFormat(DEFAULT_DATE_FORMAT));
  }
  
  public CellStyle getBoldStyle() {
    return boldStyle;
  }

  public CellStyle getDateStyle() {
    return dateStyle;
  }
  
  public CellStyle getCustomDataFormatStyle(String dataFormat) {
    CellStyle cellStyle = wb.createCellStyle();
    DataFormat df = wb.createDataFormat();
    cellStyle.setDataFormat(df.getFormat(dataFormat));
    return cellStyle;
  }

  public Workbook getWorkbook() {
    return wb;
  }
}
