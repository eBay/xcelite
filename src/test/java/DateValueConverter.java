import com.ebay.xcelite.converters.ColumnValueConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-10-21 下午12:31
 */
public class DateValueConverter implements ColumnValueConverter<String, Long> {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public String serialize(Long value) {
        Date date = new Date();
        date.setTime(value);
        return sdf.format(date);
    }

    @Override
    public Long deserialize(String value) {
        try {
            return sdf.parse(value).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0l;
        }
    }
}
