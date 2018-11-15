package zool.firefly.kits;

import com.google.common.base.Joiner;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class WebKit {
    /**
     * @return 获得当前请求
     */
    public static HttpServletRequest getRequest() { return getRequestAttributes().getRequest(); }

    /**
     * @return 获得当前Body
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static String getRequestUrl() {
        val request = getRequest();
        val query = request.getQueryString();
        return request.getRequestURI() + (Objects.isNull(query) ? "" : "?" + query);
    }

    public static String getLastReferer() {
        return SessionKit.getSessionAttr("Referer");
    }

    public static void redirect(String url) {
        val response = getResponse();
        response.addHeader("Location", url);
        response.setStatus(302);
    }

    /**
     * 获得参数MAP
     *
     * @param args
     *         如果有 必传2个参数 第一个为原始编码 第二个为输出编码
     * @return 参数MAP
     */
    public static Map<String, String> getParamMap(String... args) {
        Map<String, String> map = new HashMap<>();
        getRequest().getParameterMap().forEach((s, arr) -> {
            String value = Joiner.on(",").join(arr);
            if (args.length > 1) {
                map.put(s, converEncode(value, args[0], args[1]));
            } else {
                map.put(s, value);
            }
        });
        return map;
    }

    public static String converEncode(String value, String input, String output) {
        try {
            return new String(value.getBytes(input), output);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SneakyThrows
    public static Map<String, String> getParamMapToUTF8() {
        return getParamMap("ISO-8859-1", "UTF-8");
    }

    /**
     * @return 获取客户端IP
     */
    public static String getClientIP() {
        HttpServletRequest req = getRequest();
        String ip = Optional
                .ofNullable(req.getHeader("X-Real-IP"))
                .orElse(req.getHeader("X-Forwarded-For"));
        return Optional
                .ofNullable(ip)
                .orElse(req.getRemoteAddr()).split(",")[0];
    }
}
