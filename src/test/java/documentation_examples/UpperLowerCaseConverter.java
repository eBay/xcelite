package documentation_examples;

import com.ebay.xcelite.converters.ColumnValueConverter;

public class UpperLowerCaseConverter implements ColumnValueConverter<String, String> {
    @Override
    public String serialize(String value) {
        return value.toUpperCase();
    }

    @Override
    public String deserialize(String value) {
        return value.toLowerCase();
    }
}
