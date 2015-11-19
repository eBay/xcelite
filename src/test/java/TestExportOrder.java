import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

/**
 * @author heyunxia
 * @version 1.0
 * @since 2015-10-21 上午11:39
 */
public class TestExportOrder {
    public static void main(String[] args) {
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet("users");
        SheetWriter<WashOrder> writer = sheet.getBeanWriter(WashOrder.class);
        List<WashOrder> users = Lists.newArrayList();
        WashOrder washOrder = new WashOrder("01", "姓名", 1111, "11", 1, 1444910884496l);
        users.add(washOrder);

        washOrder = new WashOrder("02", "姓名1",111, "12", 2, 1444900884496l);
        users.add(washOrder);

        washOrder = new WashOrder("03", "姓名2", 111, "14", 3, 1444911884496l);
        users.add(washOrder);

        washOrder = new WashOrder("04", "姓名3", 1911, "13", 4, 1444930884496l);
        users.add(washOrder);

        washOrder = new WashOrder("05", "姓名4", 1811, "17", 5, 1444940884496l);
        users.add(washOrder);

        washOrder = new WashOrder("06", "姓名5", 1981, "19", 7, 1444910584496l);
        users.add(washOrder);


        writer.write(users);
        xcelite.write(new File("users_doc.xlsx"));

    }
}


