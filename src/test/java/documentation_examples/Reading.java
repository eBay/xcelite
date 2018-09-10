package documentation_examples;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import documentation_examples.model.User;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Examples from the "QuickStart" section of the README.md
 *
 * Not real unit-tests, just to ensure the example code
 * is valid
 */
public class Reading {

    @Test
    public void example1() {
        Xcelite xcelite = new Xcelite(new File("data.xlsx"));
        XceliteSheet nativeSheet = xcelite.getSheet("data_sheet");
        SheetReader<Collection<Object>> simpleReader = nativeSheet.getSimpleReader();
        Collection<Collection<Object>> data = simpleReader.read();
    }


    @Test
    public void example2() {
        Xcelite xcelite = new Xcelite(new File("users_doc.xlsx"));
        XceliteSheet nativeSheet = xcelite.getSheet("users");
        SheetReader<User> reader = nativeSheet.getBeanReader(User.class);
        Collection<User> users = reader.read();
    }
}
