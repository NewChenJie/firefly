package zool.firefly.util;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import lombok.SneakyThrows;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import zool.firefly.constant.Status;
import zool.firefly.exception.EntityValidException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 键值对处理类
 */
public class KeyValue extends HashMap<String, Object> {
    public static final String STATUS = "status";
    public static final String MSG = "msg";
    public static final String DATA = "data";
    public static final ThreadLocal<String> cacheResult = ThreadLocal.withInitial(() -> null);
    private static final long serialVersionUID = 1L;
    private static KeyValue EMPTY = new KeyValue();

    /**
     * 分页数据
     * @return {@link KeyValue}
     */
    public static KeyValue page(String pageNum, String pageSize) {
        return new KeyValue().add("pageNum", pageNum).add("pageSize", pageSize);
    }

    /**
     * 空数据
     *
     * @return {@link KeyValue}
     */
    public static KeyValue empty() {
        return (KeyValue) EMPTY.clone();
    }

    /**
     * 正确返回
     *
     * @return {@link KeyValue}
     */
    public static KeyValue ok() {
        return rd(Status.HTTP.OK);
    }

    /**
     * 正确返回
     *
     * @param msg 返回消息
     * @return {@link KeyValue}
     */
    public static KeyValue ok(String msg) {
        return rd(Status.HTTP.OK, msg);
    }

    public static KeyValue err(String msg) {
        return rd(Status.HTTP.ERROR, msg);
    }
    public static KeyValue serverError(String msg) {
        return rd(Status.HTTP.INTERNAL_SERVER_ERROR, msg);
    }

    /**
     * 正确返回
     *
     * @param data 数据
     * @return {@link KeyValue}
     */
    public static KeyValue ok(Object data) {
        return rd(Status.HTTP.OK, data);
    }

    /**
     * 正确返回
     *
     * @param msg  返回消息
     * @param data 数据
     * @return {@link KeyValue}
     */
    public static KeyValue ok(String msg, Object data) {
        return rd(Status.HTTP.OK, msg, data);
    }

    /**
     * 拒绝访问
     *
     * @param msg 提示消息
     * @return {@link KeyValue}
     */
    public static KeyValue bad_request(String msg) {
        return rd(Status.HTTP.BAD_REQUEST, msg);
    }

    /**
     * 拒绝访问
     *
     * @param msg  提示消息
     * @param data 错误数据
     * @return {@link KeyValue}
     */
    public static KeyValue bad_request(String msg, Object data) {
        return rd(Status.HTTP.BAD_REQUEST, msg, data);
    }

    /**
     * 拒绝访问
     *
     * @param msg 提示消息
     * @return {@link KeyValue}
     */
    public static KeyValue forbidden(String msg) {
        return rd(Status.HTTP.FORBIDDEN, msg);
    }

    /**
     * 拒绝访问
     *
     * @param msg  提示消息
     * @param data 错误数据
     * @return {@link KeyValue}
     */
    public static KeyValue forbidden(String msg, Object data) {
        return rd(Status.HTTP.FORBIDDEN, msg, data);
    }

    /**
     * 数据返回
     *
     * @param status 状态
     * @return {@link KeyValue}
     */
    public static KeyValue rd(int status) {
        return new KeyValue(STATUS, status);
    }

    /**
     * 结果返回
     *
     * @param result  判断结果
     * @param success 成功提示
     * @param error   失败提示
     * @return {@link KeyValue}
     */
    public static KeyValue result(boolean result, String success, String error) {
        return result ? ok(success) : forbidden(error);
    }

    /**
     * 数据返回
     *
     * @param msg 返回消息
     * @return {@link KeyValue}
     */
    public static KeyValue rd(int status, String msg) {
        return new KeyValue(STATUS, status).add(MSG, msg);
    }

    /**
     * 数据返回
     *
     * @param status 状态
     * @param data   数据
     * @return {@link KeyValue}
     */
    public static KeyValue rd(int status, Object data) {
        return new KeyValue(STATUS, status).add(DATA, data);
    }

    /**
     * 数据返回
     *
     * @param msg  返回消息
     * @param data 数据
     * @return {@link KeyValue}
     */
    public static KeyValue rd(int status, String msg, Object data) {
        return new KeyValue(STATUS, status).add(MSG, msg).add(DATA, data);
    }

    public static void valid(BindingResult... brs) {
        for (BindingResult br : brs) {
            check(br);
        }
    }

    public static KeyValue checkInGlobal(BindingResult br) {
        try {
            check(br);
        } catch (EntityValidException ex) {
            return ex.getKeyValue();
        }
        return null;
    }

    public static void check(BindingResult br) {
        if (br.hasErrors()) {
            ObjectError err = br.getAllErrors().get(0);
            String data;
            if (err instanceof FieldError) {
                FieldError f_err = (FieldError) err;
                data = String.format("字段 %s 的值 '%s' %s", f_err.getField(), f_err.getRejectedValue(), getErrType(err));
            } else {
                data = String.format("参数: %s 错误 原因: %s", err.getObjectName(), getErrType(err));
            }
            throw new EntityValidException(bad_request(err.getDefaultMessage(), data));
        }
    }

    private static String getErrType(ObjectError err) {
        switch (err.getCode()) {
            case "typeMismatch":
                return "类型不匹配!";
            default:
                return err.getDefaultMessage();
        }
    }

    public KeyValue() {
    }

    public KeyValue(String key, Object value) {
        put(key, value);
    }

    public KeyValue add(String key, Object value) {
        put(key, value);
        return this;
    }

    public KeyValue addExtraData(Map map) {
        getData().putAll(map);
        return this;
    }

    public KeyValue addExtraData(String key, Object value) {
        getData().put(key, value);
        return this;
    }

    public KeyValue del(String key) {
        remove(key);
        return this;
    }

    public <T> T getAttr(String key) {
        return (T) get(key);
    }

    public KeyValue page(Object list, Page page) {
        KeyValue kv = new KeyValue();
        kv.put("list", list);
        kv.setPage(page);
        put(DATA, kv);
        return this;
    }

    private KeyValue setPage(Page page) {
        put("num", page.getPageNum());
        put("size", page.getPageSize());
        put("count", page.getTotal());
        return this;
    }

    public String getMsg() {
        return getAttr(MSG);
    }

    public KeyValue getData() {
        return Optional.ofNullable((KeyValue) getAttr(DATA)).orElseGet(() -> add(DATA, new KeyValue()).getAttr(DATA));
    }

    public <T extends Map> T toMap() {
        return (T) this;
    }

    public MultiValueMap toMultiValueMap() {
        LinkedMultiValueMap<String, Object> lmvm = new LinkedMultiValueMap<>();
        forEach((s, o) -> {
            LinkedList<Object> list = new LinkedList<>();
            list.add(o);
            lmvm.put(s, list);
        });
        return lmvm;
    }

    public void write(HttpServletResponse response) throws IOException {
        // 设置编码 防止乱码
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        cacheResult.set(toJson());
        response.getOutputStream().write(cacheResult.get().getBytes("UTF-8"));
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    @SneakyThrows
    public String toJsonBase64() {
        return new String(Base64.getEncoder().encode(JSON.toJSONString(this).getBytes(Charset.forName("UTF-8"))), "UTF-8");
    }

    @Override
    public String toString() {
        return toJson();
    }
}
