package com.ebay.xcelite.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static File getTestDataFile(String filePath) throws Exception {
        String lPath = filePath;
        if (!lPath.startsWith("/"))
            lPath = "/" + lPath;
        URL u = TestUtil.class.getResource(lPath);
        Path path = Paths.get(u.toURI());
        return path.toFile();
    }

    public static void testColumnsForPoiRow(Row r, List<String> columnNames) {
        List<String> lColNames = new ArrayList<>(columnNames);
        int numCells = r.getPhysicalNumberOfCells();
        Assertions.assertEquals(lColNames.size(), numCells, "mismatching number of columns");
        for (int i = r.getFirstCellNum(); i < r.getLastCellNum(); i++) {
            String testVal = getCellValue(r.getCell(i));
            Assertions.assertTrue(lColNames.contains(testVal), "data in cell "+ i +" doesn't match test data");
            lColNames.remove(testVal);
        }
        Assertions.assertTrue(lColNames.isEmpty(), "names of columns do not match");
    }


    public static String getCellValue(Cell cell) {
        CellType type = cell.getCellType();
        String retVal = null;
        switch (type) {
            case STRING:
                retVal = cell.getStringCellValue();
                break;
            case NUMERIC:
                retVal = ((Double)cell.getNumericCellValue()).toString();
                break;
            case BOOLEAN:
                retVal = ((Boolean)cell.getBooleanCellValue()).toString();
                break;
            case ERROR:
                retVal = "#ERROR";
                break;
            default:
        }
        return retVal;
    }
}
