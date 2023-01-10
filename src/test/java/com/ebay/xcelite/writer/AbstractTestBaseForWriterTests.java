package com.ebay.xcelite.writer;

import com.ebay.xcelite.TestSettings;
import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.reader.AbstractSheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class AbstractTestBaseForWriterTests{
    static XSSFWorkbook workbook;

    @SneakyThrows
    public static void setup(XceliteOptions options, Object... inBeans) {
        Xcelite xcelite = new Xcelite();
        ArrayList beans = new ArrayList();
        XceliteSheet sheet = xcelite.createSheet("Tests");
        Class clazz = null;
        for (Object bean : inBeans) {
            beans.add(bean);
            if (null != bean)
                clazz = bean.getClass();
        }
        SheetWriter bs = sheet.getBeanWriter(clazz);
        bs.setOptions(options);
        bs.write(beans);
        workbook = new XSSFWorkbook(new ByteArrayInputStream(xcelite.getBytes()));
        if (TestSettings.WRITE_TO_TEST_FILES)
            writeWorkbookToFile(workbook);
    }

    List<Map<String, Object>>extractCellValues (XSSFWorkbook workbook) {
        return extractCellValues (workbook, 0,0);
    }

    List<Map<String, Object>>extractCellValues (XSSFWorkbook workbook, int skipBeforeHeader, int skipAfterHeader) {
        List<Map<String, Object>> rowVals = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        Sheet excelSheet = workbook.getSheet("Tests");
        Iterator<Row> iter = excelSheet.rowIterator();
        int rowId = 0;
        // move to header row
        while (--skipBeforeHeader >= 0) {
            rowId++;
            iter.next();
        }
        Row header = iter.next();
        rowId++;
        header.cellIterator().forEachRemaining(cell -> {
            Object val = AbstractSheetReader.readValueFromCell(cell);
            columnNames.add(val.toString());
        });
        while (--skipAfterHeader >= 0) {
            rowId++;
            iter.next();
        }
        for (int i = rowId; i <= excelSheet.getLastRowNum(); i++) {
            Row row = excelSheet.getRow(i);
            Map<String, Object> columnsMap = new LinkedHashMap<>();
            rowVals.add(columnsMap);
            if (null != row) {
                Iterator<Cell> cellIter = row.cellIterator();
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    columnsMap.put(columnNames.get(j), AbstractSheetReader.readValueFromCell(cellIter.next()));
                }
            }
        };
        return rowVals;
    }

    @SneakyThrows
    private static void writeWorkbookToFile(XSSFWorkbook workbook) {
        long tm = System.currentTimeMillis();
        File f = new File("testresult_"+tm + ".xlsx");
        FileOutputStream st = new FileOutputStream(f);
        workbook.write(st);
        st.close();
    }
}
