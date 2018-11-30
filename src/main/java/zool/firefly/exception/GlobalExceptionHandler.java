package zool.firefly.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tk.mybatis.mapper.entity.Example;
import zool.firefly.constant.Status;
import zool.firefly.util.KeyValue;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityValidException.class})
    @ResponseBody
    public KeyValue handleForbiddenException(Exception ex) {
        return KeyValue.err(ex.getMessage());
    }
    @ExceptionHandler({AuthorizationException.class})
    @ResponseBody
    public KeyValue handleAuthorizationException(Exception ex) {
        return KeyValue.err("没有权限访问");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, KeyValue.checkInGlobal(ex.getBindingResult()), headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, KeyValue.checkInGlobal(ex.getBindingResult()), headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, KeyValue.bad_request("不支持的请求数据类型!", ex.getMessage()),
                headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, KeyValue.bad_request("无法读取请求数据 请检查入参数据是否正确!", ex.getMessage()),
                headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, KeyValue.bad_request(
                String.format("参数类型不匹配 值 %s 无法转换为 %s",
                        ex.getValue(),
                        ex.getRequiredType().getSimpleName()), ex.getMessage()),
                headers, HttpStatus.OK, request);
    }
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpServletRequest req = ((ServletWebRequest) request).getNativeRequest(HttpServletRequest.class);
        return handleExceptionInternal(
                ex,
                KeyValue.rd(Status.HTTP.NOT_FOUND, "路径未找到!")
                        .add("path", req.getRequestURI())
                        .add("method", req.getMethod())
                        .add("time", System.currentTimeMillis()),
                headers, status, request);
    }

}
