package com.ebay.xcelite.helper;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.reader.AbstractSheetReader;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class TestBaseForReaderAndWriterTests {
    // set to true to look at the resulting spreadsheet files
    public static boolean writeToFile = false;
    public static XSSFWorkbook workbook;


    @SneakyThrows
    public static void setup(Object... inBeans) {
        setup(new XceliteOptions(), inBeans);
    }

    public List<Map<String, Object>>extractCellValues (XSSFWorkbook workbook) {
        return extractCellValues (workbook, 0,0);
    }

    public List<Map<String, Object>>extractCellValues (XSSFWorkbook workbook, int skipBeforeHeader, int skipAfterHeader) {
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
    public static void writeWorkbookToFile(XSSFWorkbook workbook) {
        long tm = System.currentTimeMillis();
        File f = new File("testresult_"+tm + ".xlsx");
        FileOutputStream st = new FileOutputStream(f);
        workbook.write(st);
        st.close();
    }
}
