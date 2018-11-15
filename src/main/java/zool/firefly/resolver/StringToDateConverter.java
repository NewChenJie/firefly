package zool.firefly.resolver;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class StringToDateConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairs = new HashSet<>();
        pairs.add(new ConvertiblePair(String.class, Date.class));
        return pairs;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType.getType() == String.class) {
            //设置接受的格式在DateTimeFormat注解上
            DateTimeFormat ann = targetType.getAnnotation(DateTimeFormat.class);
            // Format 最慢情况下 每秒也可以达到 20w 次 如果需要 可以用 ThreadLocal 缓存
            SimpleDateFormat sdf = new SimpleDateFormat(ann.pattern());
            try {
                return sdf.parse((String) source);
            } catch (ParseException e) {
                throw new IllegalArgumentException(String.format("日期 %s 和指定格式 %s 不匹配!", source, ann.pattern()), e);
            }
        }
        return source;
    }
}
