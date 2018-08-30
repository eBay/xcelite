package com.ebay.xcelite;

import com.ebay.xcelite.exceptions.ColumnNotFoundException;
import com.ebay.xcelite.exceptions.EmptyCellException;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.UsStringCellDateConverter;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.reader.BeanSheetReader;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanReaderTests extends AbstractTestBaseForWriterTests {
    private SimpleDateFormat df = new SimpleDateFormat(UsStringCellDateConverter.DATE_PATTERN);

    private static String usTestData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"female"},
            {"Witch",	"Doctor",	"01/01/1990",	"male"}
    };

    @BeforeAll
    @SneakyThrows
    static void setup() {
    }

    @Test
    @DisplayName("Must correctly read a header row where some columns are not mapped to annotated properties")
    void mustReadHeaderRowWithMissingCellsOK() {
        XceliteOptions options = new XceliteOptions();
        options.setSkipRowsBeforeColumnDefinitionRow(3);

        List<CamelCase> upper = getData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
        validateData(upper);
    }

    @Test
    @DisplayName("Must correctly throw reading a header row where some columns are not mapped to annotated properties")
    public void mustThrowReadingHeaderRowWithMissingCellsOK() {
        XceliteOptions options = new XceliteOptions();
        options.setSkipRowsBeforeColumnDefinitionRow(3);
        options.setMissingCellPolicy(MissingCellPolicy.THROW);

        assertThrows(EmptyCellException.class, () -> {
            List<CamelCase> upper = getData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
            validateData(upper);
        });
    }

    @SneakyThrows
    private List<CamelCase> getData(XceliteOptions options, String filePath)  {
        Xcelite xcelite = new Xcelite(new File(filePath));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<CamelCase> beanReader = new BeanSheetReader<>(sheet, options, CamelCase.class);
        ArrayList<CamelCase> data = new ArrayList<>(beanReader.read());
        return data;
    }

    @SneakyThrows
    private void validateData(List<CamelCase> data) {
        CamelCase first = data.get(0);
        assertEquals(usTestData[0][0], first.getName(), "Name mismatch");
        assertEquals(usTestData[0][1], first.getSurname(), "Surname mismatch");
        assertEquals(df.parse(usTestData[0][2]), first.getBirthDate(), "Birthdate mismatch");

        CamelCase second = data.get(1);
        assertEquals(usTestData[1][0], second.getName(), "Name mismatch");
        assertEquals(usTestData[1][1], second.getSurname(), "Surname mismatch");
        assertEquals(df.parse(usTestData[1][2]), second.getBirthDate(), "Birthdate mismatch");
    }
}
