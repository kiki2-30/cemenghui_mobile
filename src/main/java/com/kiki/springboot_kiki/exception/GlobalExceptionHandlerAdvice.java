package com.kiki.springboot_kiki.exception;

import com.kiki.springboot_kiki.Pojo.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    Logger log = LoggerFactory.getLogger(GlobalExceptionHandlerAdvice.class);

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseMessage handleValidationException(Exception e) {
        List<FieldError> fieldErrors;
        if (e instanceof MethodArgumentNotValidException) {
            fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        } else {
            fieldErrors = ((BindException) e).getBindingResult().getFieldErrors();
        }
        
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.error("参数校验失败: {}", errorMessage);
        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "参数校验失败: " + errorMessage, null);
    }

    /**
     * 处理参数类型转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseMessage handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("参数类型转换失败: {}", e.getMessage());
        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), 
            "参数类型错误: " + e.getName() + " 应为 " + e.getRequiredType().getSimpleName(), null);
    }

    /**
     * 处理JSON解析异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("JSON解析失败: {}", e.getMessage());
        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "请求体格式错误", null);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseMessage handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("业务异常: {} - URL: {}", e.getMessage(), request.getRequestURI());
        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    /**
     * 处理权限异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseMessage handleUnauthorizedException(UnauthorizedException e) {
        log.error("权限异常: {}", e.getMessage());
        return new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null);
    }

    /**
     * 处理数据完整性异常（如外键约束、唯一约束等）
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseMessage handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("数据完整性异常: {}", e.getMessage());
        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "数据操作失败，请检查数据完整性", null);
    }

    /**
     * 处理数据不存在异常
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseMessage handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error("数据不存在: {}", e.getMessage());
        return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "请求的数据不存在", null);
    }

    /**
     * 处理限流异常
     */
    @ExceptionHandler(RateLimitException.class)
    public ResponseMessage handleRateLimitException(RateLimitException e) {
        log.error("限流异常: {}", e.getMessage());
        return new ResponseMessage(HttpStatus.TOO_MANY_REQUESTS.value(), e.getMessage(), null);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler({Exception.class})
    public ResponseMessage handlerException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {} - URL: {}", e.getMessage(), request.getRequestURI(), e);
        return new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统内部错误，请联系管理员", null);
    }
}
