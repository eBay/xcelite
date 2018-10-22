import com.ebay.xcelite.converters.ColumnValueConverter;

import java.math.BigDecimal;

/**
 * @author heyunxia
 * @version 1.0
 * @since 2015-10-21 下午12:02
 */
public class PriceValueConverter implements ColumnValueConverter<String, Integer> {
    @Override
    public Integer deserialize(String value) {
        return null;
    }

    @Override
    public String serialize(Integer value) {

        return fmtPrice(value.longValue());
    }


    public static Long fmtPrice(String price) {

        Float fPrice = Float.parseFloat(price) * 100;
        return fPrice.longValue();
    }
    // 分 转换 元（String）
    public static String fmtPrice(Long price) {
        BigDecimal a = new BigDecimal(price);
        BigDecimal b = new BigDecimal(100);
        BigDecimal c = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);

        return c.toString();
    }
}
