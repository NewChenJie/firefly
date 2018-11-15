package zool.firefly.kits;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class BeanKit implements ApplicationContextAware{

    private static ApplicationContext context;

    private BeanKit() {}

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static <T> T getOfName(String name) {
        return (T) context.getBean(name);
    }

    public static <T> T getOfType(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> Collection<T> getListOfType(Class<T> clazz) {
        return context.getBeansOfType(clazz).values();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }
}
