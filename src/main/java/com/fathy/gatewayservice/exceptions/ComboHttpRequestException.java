package com.fathy.gatewayservice.exceptions;

import com.fathy.gatewayservice.common.enums.OrangeErrorInfo;

import javax.servlet.http.HttpServletRequest;

public class ComboHttpRequestException extends RuntimeException {
    private HttpServletRequest request;
    private OrangeErrorInfo orangeErrorInfo = OrangeErrorInfo.INTERNAL_ERROR;

    public ComboHttpRequestException(String message, HttpServletRequest request) {
        super(message);
        this.request = request;
    }

    public ComboHttpRequestException(String message, HttpServletRequest request, OrangeErrorInfo orangeErrorInfo) {
        super(message);
        this.request = request;
        this.orangeErrorInfo = orangeErrorInfo;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public OrangeErrorInfo getOrangeErrorInfo() {
        return orangeErrorInfo;
    }

    public void setOrangeErrorInfo(OrangeErrorInfo orangeErrorInfo) {
        this.orangeErrorInfo = orangeErrorInfo;
    }
}
