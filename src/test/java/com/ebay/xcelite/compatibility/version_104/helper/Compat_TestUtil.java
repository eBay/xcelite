package com.ebay.xcelite.compatibility.version_104.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;


public class Compat_TestUtil {

    public static void testColumnsForPoiRow(Row r, List<String> columnNames) {
        List<String> lColNames = new ArrayList<String>(columnNames);
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
        CellType type = cell.getCellTypeEnum();
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
