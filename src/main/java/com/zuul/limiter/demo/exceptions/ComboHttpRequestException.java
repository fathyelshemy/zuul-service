package com.zuul.limiter.demo.exceptions;


import javax.servlet.http.HttpServletRequest;

public class ComboHttpRequestException extends RuntimeException {
    private HttpServletRequest request;
    private ErrorInfo errorInfo = ErrorInfo.INTERNAL_ERROR;

    public ComboHttpRequestException(String message, HttpServletRequest request) {
        super(message);
        this.request = request;
    }

    public ComboHttpRequestException(String message, HttpServletRequest request, ErrorInfo errorInfo) {
        super(message);
        this.request = request;
        this.errorInfo = errorInfo;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public ErrorInfo getOrangeErrorInfo() {
        return errorInfo;
    }

    public void setOrangeErrorInfo(ErrorInfo orangeErrorInfo) {
        this.errorInfo = errorInfo;
    }
}
