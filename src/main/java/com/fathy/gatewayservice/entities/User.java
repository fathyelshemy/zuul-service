package com.fathy.gatewayservice.entities;


import javax.persistence.*;
import java.util.Date;

@Entity(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "token_last_updated")
  private Date tokenLastUpdated;

  @Column(name = "deleted")
  private boolean deleted;

  public User() {

  }

  public User(String email, Date tokenLastUpdated) {
    this.email = email;
    this.tokenLastUpdated = tokenLastUpdated;
  }

  public User(String email, Date tokenLastUpdated, boolean deleted) {
    this.email = email;
    this.tokenLastUpdated = tokenLastUpdated;
    this.deleted = deleted;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getTokenLastUpdated() {
    return tokenLastUpdated;
  }

  public void setTokenLastUpdated(Date tokenLastUpdated) {
    this.tokenLastUpdated = tokenLastUpdated;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", deleted=" + deleted +
            '}';
  }
}
