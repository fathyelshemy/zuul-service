package com.fathy.gatewayservice.dtos;

public class DeletedUserDto {
    private String email;
    private boolean deleted;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "DeletedUserDto{" +
                "email='" + email + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
