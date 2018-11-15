package zool.firefly.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.*;
import zool.firefly.interceptor.DebugInterceptor;
import zool.firefly.kits.BeanKit;


@Configuration
public class MvcConfig implements WebMvcConfigurer {


    /**
     * 自动扫描相关组件并且注册
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        for (Converter<?, ?> converter : BeanKit.getListOfType(Converter.class)) {
            registry.addConverter(converter);
        }
        for (GenericConverter converter : BeanKit.getListOfType(GenericConverter.class)) {
            registry.addConverter(converter);
        }
        for (Formatter<?> formatter : BeanKit.getListOfType(Formatter.class)) {
            registry.addFormatter(formatter);
        }
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(BeanKit.getOfType(DebugInterceptor.class));
    }

}
