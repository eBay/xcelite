package documentation_examples;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import documentation_examples.model.User;
import org.junit.jupiter.api.AfterAll;
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
public class QuickStart {

    @Test
    public void example1() {
        Xcelite xcelite = new Xcelite();
        XceliteSheet nativeSheet = xcelite.createSheet("data_sheet");
        SheetWriter<Collection<Object>> simpleWriter = nativeSheet.getSimpleWriter();
        List<Collection<Object>> data = new ArrayList<>();
        // ...fill up data
        simpleWriter.write(data);
        xcelite.write(new File("data.xlsx"));
    }


    @Test
    public void example2() {
    Xcelite xcelite = new Xcelite();
    XceliteSheet nativeSheet = xcelite.createSheet("users");
    SheetWriter<User> writer = nativeSheet.getBeanWriter(User.class);
    List<User> users = new ArrayList<>();
    // ...fill up users
    writer.write(users);
    xcelite.write(new File("users_doc.xlsx"));
    }

    @AfterAll
    static void after() {
        File f1 = new File("users_doc.xlsx");
        File f2 = new File("users_doc.xlsx");

        if (f1.exists())
            f1.delete();

        if (f2.exists())
            f2.delete();
    }
}
