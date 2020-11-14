package com.zuul.limiter.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class FieldErrorDTO {

  private String code;
  private String message;
  private String description;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String infoURL;

  public FieldErrorDTO(String code, String message, String description) {
    this.code = code;
    this.message = message;
    this.description = description;
  }

  public FieldErrorDTO(String code, String message, String description, String infoURL) {
    this.code = code;
    this.message = message;
    this.description = description;
    this.infoURL = infoURL;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
