package com.ebay.xcelite;

import com.ebay.xcelite.helper.TestUtil;
import com.ebay.xcelite.helper.TestWorkbookCreator;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Tests for our @TestWorkbookCreator test helper class.
 *
 * This does NOT test xcelite functionality
 */
public class WorkbookCreatorTests {

    // set to true to see the created test workbooks
    private static final boolean WRITE_TO_TEST_FILES = true;
    TestWorkbookCreator creator;
    private String fileName=null;

    @BeforeEach
    public void setup() {
        creator = new TestWorkbookCreator();
    }


    /*
         Create a workbook using default values
    */
    @Test
    @DisplayName("Test default values")
    public void testDefault1 () {
        fileName = "wbTest-defaults.xlsx";
        creator.createAndFillSheets();

        Row headerRow = creator.getHeaderRow();
        TestUtil.testColumnsForPoiRow(headerRow, Arrays.asList(creator.getColumnNames()));
    }

    /*
        Create a workbook with one header row
        and zero rows before
     */
    @Test
    @DisplayName("Test with header row")
    public void testWithHeaderRow() {
        fileName = "wbTest-headline.xlsx";
        creator.setCreateHeaderRow(true);
        creator.setRowsBeforeHeader(0);
        creator.createAndFillSheets();

        Row headerRow = creator.getHeaderRow();
        TestUtil.testColumnsForPoiRow(headerRow, Arrays.asList(creator.getColumnNames()));
    }

    /*
     Create a workbook with one header row
     and 3 rows before
    */
    @Test
    @DisplayName("Test with header row and 3 rows before")
    public void testWithHeaderRowAnd3RowsBefore() {
        fileName = "wbTest-headline3.xlsx";
        creator.setCreateHeaderRow(true);
        creator.setRowsBeforeHeader(3);
        creator.createAndFillSheets();

        Row headerRow = creator.getHeaderRow();
        TestUtil.testColumnsForPoiRow(headerRow, Arrays.asList(creator.getColumnNames()));
    }

    /*
      Create a workbook with one header row
      and zero rows before, and 3 rows after
     */
    @Test
    @DisplayName("Test with header row and 3 rows after")
    public void testWithHeaderRowAnd3RowsAfter() {
        fileName = "wbTest-headline4.xlsx";
        creator.setCreateHeaderRow(true);
        creator.setRowsBeforeHeader(0);
        creator.setRowsAfterHeader(3);
        creator.createAndFillSheets();

        Row headerRow = creator.getHeaderRow();
        TestUtil.testColumnsForPoiRow(headerRow, Arrays.asList(creator.getColumnNames()));
        Row dataRow = creator.getFirstDataRow();
        List<String> testData = creator
                .getDataset(0)
                .stream()
                .map(d -> (null == d) ? null : d.toString())
                .collect(Collectors.toList());
        Assertions.assertEquals(dataRow.getPhysicalNumberOfCells(), testData.size(),
                "number of cells doesn't match data size");
        TestUtil.testColumnsForPoiRow(dataRow, testData);
    }


    // #################### Helpers ###################

    @AfterEach
    @SneakyThrows
    public void removeFile() {
        if (!WRITE_TO_TEST_FILES)
            return;
        if (null == fileName)
            return;

        File workbookFile = new File (fileName);
        if (!workbookFile.exists()) {
            workbookFile.createNewFile();
        }
        try (OutputStream outStream = new FileOutputStream(workbookFile)) {
            Workbook workbook = creator.getWorkbook();
            workbook.write(outStream);
            workbook.close();
            outStream.flush();
        }
    }


}
