package com.fathy.gatewayservice.dtos;

import java.io.Serializable;
import java.util.Date;

public class UpdatedTokenDto implements Serializable {
  private String userEmail;
  private Date lastUpdated;

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
