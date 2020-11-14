package com.fathy.gatewayservice.common.dto;

public class ReasonDTO {

    private int code;
    private String text;


    public ReasonDTO(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ReasonDTO{" +
                "code=" + code +
                ", text='" + text + '\'' +
                '}';
    }
}


