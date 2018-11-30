package zool.firefly.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import zool.firefly.interceptor.DebugInterceptor;
import zool.firefly.kits.BeanKit;
import zool.firefly.resolver.JSONArgumentResolver;

import java.nio.charset.Charset;
import java.util.List;


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
     * 信息转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastJsonHttpMessageConverter);
    }

    /**
     * 参数解析
     * @param resolvers 解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new JSONArgumentResolver());
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(BeanKit.getOfType(DebugInterceptor.class));
    }

}
