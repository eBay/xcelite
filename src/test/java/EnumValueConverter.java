import com.ebay.xcelite.converters.ColumnValueConverter;

/**
 * @author heyunxia
 * @version 1.0
 * @since 2015-10-21 上午11:54
 */
public class EnumValueConverter implements ColumnValueConverter<EnumOrderStatus, String> {
    @Override
    public String deserialize(EnumOrderStatus value) {
        return null;
    }

    @Override
    public EnumOrderStatus serialize(String value) {
        return EnumOrderStatus.fromStatusCode(Integer.valueOf(value));
    }
}
