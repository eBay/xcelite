package com.ebay.xcelite.compatibility.version_104.reader;
import com.ebay.xcelite.compatibility.version_104.model.Compat_UsStringCellDateConverter;
import org.junit.jupiter.api.DisplayName;

import java.text.SimpleDateFormat;

@DisplayName("Compatibility BeanReaderTests test compatibility with Version 1.0.4")
public class Compat_BeanReaderTests {
    private SimpleDateFormat df = new SimpleDateFormat(Compat_UsStringCellDateConverter.DATE_PATTERN);

    private static String usTestData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"female"},
            {"Witch",	"Doctor",	"01/01/1990",	"male"}
    };
      /*
    @Test
    void mustReadHeaderRowWithMissingCellsOK() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);

        List<Compat_CamelCase> upper = getData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
        validateData(upper);
    }


    @Test
    void mustReadHeaderRowWithMissingCellsWithMissingCellPolicy_RETURN_BLANK_AS_NULL_OK() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        options.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

        List<Compat_CamelCase> upper = getData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
        validateData(upper);
    }

    @Test
    void mustReadHeaderRowWithMissingCellsWithMissingCellPolicy_RETURN_NULL_AND_BLANK_OK() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        options.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);

        List<Compat_CamelCase> upper = getData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
        validateData(upper);
    }
    @Test
    public void mustThrowReadingHeaderRowWithMissingCellsOK() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        options.setMissingCellPolicy(Row.MissingCellPolicy.THROW);

        assertThrows(EmptyCellException.class, () -> {
            List<Compat_CamelCase> upper = getData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
            validateData(upper);
        });
    }


    private List<Compat_CamelCase> getData(XceliteOptions options, String filePath)  {
        Compat_Xcelite xcelite = new Compat_Xcelite(new File(filePath));
        Compat_XceliteSheet sheet = xcelite.getSheet(0);
        Compat_SheetReader<Compat_CamelCase> beanReader = new Compat_BeanSheetReader<Compat_CamelCase>(sheet, options, Compat_CamelCase.class);
        ArrayList<Compat_CamelCase> data = new ArrayList<Compat_CamelCase>(beanReader.read());
        return data;
    }

    private void validateData(List<Compat_CamelCase> data) {
        Compat_CamelCase first = data.get(0);
        assertEquals(usTestData[0][0], first.getName(), "Name mismatch");
        assertEquals(usTestData[0][1], first.getSurname(), "Surname mismatch");
        assertEquals(df.parse(usTestData[0][2]), first.getBirthdate(), "Birthdate mismatch");

        Compat_CamelCase second = data.get(1);
        assertEquals(usTestData[1][0], second.getName(), "Name mismatch");
        assertEquals(usTestData[1][1], second.getSurname(), "Surname mismatch");
        assertEquals(df.parse(usTestData[1][2]), second.getBirthdate(), "Birthdate mismatch");
    }
*/
}
