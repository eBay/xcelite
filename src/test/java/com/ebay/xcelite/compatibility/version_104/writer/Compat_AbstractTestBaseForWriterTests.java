package com.ebay.xcelite.compatibility.version_104.writer;

import compat.com.ebay.xcelite_104.Compat_Xcelite;
import compat.com.ebay.xcelite_104.reader.Compat_BeanSheetReader;
import compat.com.ebay.xcelite_104.reader.Compat_SheetReaderAbs;
import compat.com.ebay.xcelite_104.sheet.Compat_XceliteSheet;
import compat.com.ebay.xcelite_104.sheet.Compat_XceliteSheetImpl;
import compat.com.ebay.xcelite_104.writer.Compat_SheetWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.*;

class Compat_AbstractTestBaseForWriterTests {
    // set to true to look at the resulting spreadsheet files
    private static final boolean writeToFile = false;
    static XSSFWorkbook workbook;

    static void setup(Object bean) throws Exception {
        Compat_Xcelite xcelite = new Compat_Xcelite();
        ArrayList beans = new ArrayList();
        Compat_XceliteSheet sheet = xcelite.createSheet("Tests");
        beans.add(bean);
        Compat_SheetWriter bs = sheet.getBeanWriter(bean.getClass());
        bs.write(beans);
        workbook = new XSSFWorkbook(new ByteArrayInputStream(xcelite.getBytes()));
        if (writeToFile)
            writeWorkbookToFile(workbook);
    }

    Map<String, Object> extractCellValues (XSSFWorkbook workbook) throws Exception {
        List<String> columnNames = new ArrayList<String>();
        Map<String, Object> columnsMap = new LinkedHashMap<String, Object>();
        Sheet excelSheet = workbook.getSheet("Tests");
        Compat_XceliteSheetImpl xcSheet = new Compat_XceliteSheetImpl(excelSheet);
        Compat_BeanSheetReader rdr = new Compat_BeanSheetReader(xcSheet, Object.class);
        Iterator<Row> iter = excelSheet.rowIterator();
        // move to header row
        Row header = iter.next();
        Iterator<Cell> iter2 = header.cellIterator();
        while (iter2.hasNext()) {
            Cell cell = iter2.next();
            Object val = callReadValueFromCell(rdr,cell);
            columnNames.add(val.toString());
        }

        while (iter.hasNext()) {
            Row row = iter.next();
            Iterator<Cell> cellIter = row.cellIterator();
            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                columnsMap.put(columnNames.get(i), callReadValueFromCell(rdr, cellIter.next()));
            }
        }
        return columnsMap;
    }

    private static Object callReadValueFromCell(Compat_BeanSheetReader o, Cell cell) throws Exception {
        Class<?> clazz = Compat_SheetReaderAbs.class;
        Method retrieveItems = clazz.getDeclaredMethod("readValueFromCell", Cell.class);
        retrieveItems.setAccessible(true);
        Object retVal = retrieveItems.invoke(o, cell);
        return retVal;
    }

    private static void writeWorkbookToFile(XSSFWorkbook workbook) throws Exception {
        long tm = System.currentTimeMillis();
        File f = new File("testresult_"+tm + ".xlsx");
        FileOutputStream st = new FileOutputStream(f);
        workbook.write(st);
        st.close();
    }
}
