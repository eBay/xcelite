package documentation_examples;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import documentation_examples.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

/**
 * Examples from the "QuickStart" section of the README.md
 *
 * Not real unit-tests, just to ensure the example code
 * is valid
 */
public class Reading {

    @BeforeAll
    public static void setup() {
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet1 = xcelite.createSheet("data_sheet");
        SheetWriter<Collection<Object>> simpleWriter = sheet1.getSimpleWriter();
        List<Collection<Object>> data = new ArrayList<>();

        Set<Object> usr1 = new LinkedHashSet<>();
        usr1.add(1L);
        usr1.add("Max");
        usr1.add("Busch");
        data.add(usr1);

        Set<Object> usr2 = new LinkedHashSet<>();
        usr2.add(2L);
        usr2.add("Moritz");
        usr2.add("Busch");
        data.add(usr2);

        simpleWriter.write(data);
        xcelite.write(new File("data.xlsx"));

        XceliteSheet sheet2 = xcelite.createSheet("users");
        SheetWriter<User> writer = sheet2.getBeanWriter(User.class);
        List<User> users = new ArrayList<>();

        User usr3 = new User();
        usr3.setId(1L);
        usr3.setFirstName("Max");
        usr3.setLastName("Busch");
        users.add(usr3);

        User usr4 = new User();
        usr4.setId(2L);
        usr4.setFirstName("Moritz");
        usr4.setLastName("Busch");
        users.add(usr4);

        writer.write(users);
        xcelite.write(new File("users_doc.xlsx"));
    }

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

    @AfterAll
    static void after() {
        File f1 = new File("data.xlsx");
        File f2 = new File("users_doc.xlsx");

        if (f1.exists())
            f1.delete();

        /*if (f2.exists())
            f2.delete();*/
    }
}
