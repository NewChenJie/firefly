package zool.firefly.kits;


import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA
 */
public class SessionKit {
    private SessionKit() {
    }

    public static <T> T getSessionAttr(String key) {
        return (T) getSession().getAttribute(key);
    }


    public static void setSessionAttr(String key, Object value) {
        getSession().setAttribute(key, value);
    }


    public static void clearSessionAttr(String key) {
        setSessionAttr(key, null);
    }

    public static HttpSession getSession() { return WebKit.getRequest().getSession();
    }
}
