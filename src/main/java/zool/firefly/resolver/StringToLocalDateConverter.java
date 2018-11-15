package zool.firefly.resolver;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

@Component
public class StringToLocalDateConverter  implements GenericConverter {
    private static final int YEAR_MONTH_LENGTH=7;
    private static final int LOCAL_DATE_LENGTH=10;

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairs = new HashSet<>();
        pairs.add(new ConvertiblePair(String.class, LocalDate.class));
        pairs.add(new ConvertiblePair(String.class, YearMonth.class));
        return pairs;
    }
    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        //2018-11-11       2018-11
        if (sourceType.getType() == String.class) {
            String time = (String) source;
            try {
                if (time.length() == YEAR_MONTH_LENGTH) {
                    return YearMonth.parse(time);
                }
                if (time.length() == LOCAL_DATE_LENGTH) {
                    return LocalDate.parse(time);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("字符串类型不匹配",e);
            }
        }
        return source;
    }
}
