package zool.firefly.resolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA
 */
public class JSONArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return JSONObject.class.isAssignableFrom(paramType) || JSONArray.class.isAssignableFrom(paramType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> paramType = parameter.getParameterType();
        String json = webRequest.getParameter(parameter.getParameterName());
        if (JSONObject.class.isAssignableFrom(paramType)) {
            return JSON.parseObject(json);
        } else if (JSONArray.class.isAssignableFrom(paramType)) {
            return JSON.parseArray(json);
        } else {
            Method method = parameter.getMethod();
            throw new UnsupportedOperationException("Unknown parameter type: " + paramType + " in method: " + method);
        }
    }
}
