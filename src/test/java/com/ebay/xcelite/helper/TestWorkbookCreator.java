package com.ebay.xcelite.helper;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates configurable Excel workbooks for testing
 * purposes.
 */

@Data
public class TestWorkbookCreator {
    private Workbook workbook;

    /**
     * Determines whether the Creator inserts a header row at
     * all. Default: true
     */
    private boolean createHeaderRow = true;

    /**
     * Determines whether the Creator inserts empty rows into
     * the sheet. Default: false
     */
    private boolean createRandomEmptyRows = false;

    /**
     * Determines whether the Creator inserts additional columns
     * that cannot be mapped to the annotated bean properties.
     * Default: true
     */
    private boolean createRandomAdditionalColumns = true;

    /**
     * Determines whether the Creator inserts Error cells.
     * Default: true
     */
    private boolean createRandomErrorCells = true;

    /**
     * Determines whether the Creator inserts blank cells.
     * Default: true
     */
    private boolean createRandomBlankCells = true;

    /**
     * Number of additional columns inserted left of (before)
     * the data area. Default 0
     */
    private int columnsOnLeft = 0;

    /**
     * Number of additional columns inserted right of (after)
     * the data area. Default 0
     */
    private int columnsOnRight = 0;

    /**
     * Number of rows inserted before header row. Default 0
     */
    private int rowsBeforeHeader = 0;

    /**
     * Number of rows inserted after header row. Default 0
     */
    private int rowsAfterHeader = 0;

    /**
     * Number of rows containing "data" that will get
     * created. Default 10
     */
    private int dataRows = 10;

    /**
     * Which of the created sheets will be filled with "data".
     * Default 1, so second sheet
     */
    private int testSheetIndex = 1;

    /**
     * Default sheet names for the created workbook
     */
    private String sheetNames[] = new String[]{
            "Sheet 1", "Test data", "Sheet 3", "Sheet 4"
    };

    /**
     * Default column names
     */
    private String columnNames[] = new String[]{
            "SURNAME", "SEXID", "BIRTHDATE", "NAME"
    };

    /**
     * Default column types
     */
    private CellType columnTypes[] = new CellType[]{
            CellType.STRING, CellType.NUMERIC, CellType.NUMERIC, CellType.STRING
    };

    private List<List<Object>> data = new ArrayList<>();

    public TestWorkbookCreator () {
        workbook = new XSSFWorkbook();
    }

    public Sheet getTestSheet() {
        return workbook.getSheetAt(testSheetIndex);
    }

    public Row getFirstDataRow() {
        Sheet sh = workbook.getSheetAt(testSheetIndex);
        int rowIdx = rowsBeforeHeader + rowsAfterHeader;
        if (createHeaderRow)
            rowIdx++;
        return sh.getRow(rowIdx);
    }

    public Row getHeaderRow() {
        Sheet sh = workbook.getSheetAt(testSheetIndex);
        int rowIdx = rowsBeforeHeader -1;
        if (createHeaderRow)
            rowIdx++;
        return sh.getRow(rowIdx);
    }

    public List<Object> getDataset(int index) {
        return data.get(index);
    }

    public void createAndFillSheets() {
        createSheets(workbook, sheetNames, testSheetIndex);
        Sheet sheet = getTestSheet();
        fillSheet(sheet);
    }

    public void fillSheet(Sheet sheet) {
        int numDataColumns = columnNames.length;
        int curRow = 0;

        for (short rowIdx = 0; rowIdx <= rowsBeforeHeader; rowIdx++){
            createEmptyRow (sheet, rowIdx);
            curRow = rowIdx;
        }

        createHeaderRow (sheet, curRow++, columnNames);

        for (short rowIdx = 0; rowIdx < rowsAfterHeader; rowIdx++){
            createEmptyRow (sheet, rowIdx + curRow);
            curRow++;
        }

        int firstRow = curRow;
        for (int rowIdx = curRow; rowIdx < (curRow + dataRows); rowIdx++){
            if (createRandomEmptyRows && (Math.random() > 0.9)) {
                createEmptyRow (sheet, rowIdx);
            }

            Row row = createRow (sheet, rowIdx, columnsOnLeft, numDataColumns, columnsOnRight);
            for (int colIdx = columnsOnLeft; colIdx < columnsOnLeft + numDataColumns; colIdx++) {
                Cell cell = row.getCell(colIdx);
                Object dataVal = fillCell(cell, colIdx, columnTypes, createRandomErrorCells, createRandomBlankCells);
                rememberDataValue((rowIdx - firstRow), dataVal);
            }
        }
    }

    private void rememberDataValue(int rowIdx, Object dataVal) {
        List<Object> rowData;
        if ((rowIdx >= data.size()) || (null == data.get(rowIdx))) {
            rowData = new ArrayList<>();
            data.add(rowData);
        }
        rowData = data.get(rowIdx);
        rowData.add(dataVal);
    }

    private static void createSheets (Workbook wb, String sheetNames[], int testSheetIndex) {
        if (testSheetIndex >= sheetNames.length) {
            throw new RuntimeException("testSheetIndex cannot be bigger than number of sheets");
        }
        for (String name : sheetNames) {
            wb.createSheet(name);
        }
    }

    private static Row createRow (
            Sheet sheet,
            int rowIdx,
            int columnsOnLeft,
            int numDataColumns,
            int columnsOnRight) {
        Row row = sheet.createRow(rowIdx);
        short curColIdx = 0;
        for (short colIdx = 0; colIdx < columnsOnLeft; colIdx++) {
            Cell cell = row.createCell(colIdx);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("#");
            curColIdx = colIdx;
        }

        short colOffset = 0;
        for (short colIdx = 0; colIdx < numDataColumns; colIdx++) {
            row.createCell(colIdx+curColIdx);
            colOffset = colIdx;
        }

        curColIdx = (short) (curColIdx + colOffset);
        for (short colIdx = 0; colIdx < columnsOnRight; colIdx++) {
            Cell cell = row.createCell(colIdx+curColIdx);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("#");
        }
        return row;
    }

    private static Row createEmptyRow (
            Sheet sheet,
            int rowIdx) {

        return sheet.createRow(rowIdx);
    }

    private static Row createHeaderRow (
            Sheet sheet,
            int rowIdx,
            String columnNames[]) {

        Row row = sheet.createRow(rowIdx);
        for (short colIdx = 0; colIdx < columnNames.length; colIdx++) {
            Cell cell = row.createCell(colIdx);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(columnNames[colIdx]);
        }
        return row;
    }

    private static Object fillCell(
            Cell cell,
            int colIdx,
            CellType columnTypes[],
            boolean randomErrorCells,
            boolean randomBlankCells) {
        if (null == cell)
            return null;
        CellType type = (colIdx <= columnTypes.length) ? columnTypes[colIdx] : CellType.STRING;

        if ((randomErrorCells) && (Math.random() > 0.7)) {
            type = CellType.ERROR;
        } else if ((randomBlankCells) && (Math.random() > 0.7)) {
            type = CellType._NONE;
        }
        if (type != CellType._NONE)
            cell.setCellType(type);

        Object retVal = null;
        switch (type) {
            case STRING:
                retVal = "asdf";
                cell.setCellValue((String)retVal);
                break;
            case NUMERIC:
                if (Math.random() > 0.5) {
                    retVal = Math.random();
                } else {
                    retVal = (double) Math.round(Math.random() * 100);
                }
                cell.setCellValue((Double)retVal);
                break;
            case BOOLEAN:
                retVal = Math.random() > 0.5;
                cell.setCellValue((Boolean)retVal);
                break;
            default:
                cell.setCellValue((String)null);
        }
        return retVal;
    }
}
