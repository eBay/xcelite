package com.ebay.xcelite;

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

class AbstractTestBaseForWriterTests{
    static final boolean writeToFile = false;
    static XSSFWorkbook workbook;

    @SneakyThrows
    static void setup(Object bean, Class beanClass) {
        Xcelite xcelite = new Xcelite();
        ArrayList beans = new ArrayList();
        XceliteSheet sheet = xcelite.createSheet("Tests");
        beans.add(bean);
        SheetWriter bs = sheet.getBeanWriter(beanClass);
        bs.write(beans);
        workbook = new XSSFWorkbook(new ByteArrayInputStream(xcelite.getBytes()));
        if (writeToFile)
            writeWorkbookToFile(workbook);
    }




    Map<String, Object> extractCellValues (XSSFWorkbook workbook) {
        List<String> columnNames = new ArrayList<>();
        Map<String, Object> columnsMap = new LinkedHashMap<>();
        Sheet excelSheet = workbook.getSheet("Tests");
        Iterator<Row> iter = excelSheet.rowIterator();
        // move to header row
        Row header = iter.next();
        header.cellIterator().forEachRemaining(cell -> {
            Object val = AbstractSheetReader.readValueFromCell(cell);
            columnNames.add(val.toString());
        });
        iter.forEachRemaining(row -> {
            Iterator<Cell> cellIter = row.cellIterator();
            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                columnsMap.put(columnNames.get(i), AbstractSheetReader.readValueFromCell(cellIter.next()));
            }
        });
        return columnsMap;
    }

    @SneakyThrows
    static void writeWorkbookToFile(XSSFWorkbook workbook) {
        File f = new File("gotcha.xlsx");
        FileOutputStream st = new FileOutputStream(f);
        workbook.write(st);
        st.close();
    }
}
