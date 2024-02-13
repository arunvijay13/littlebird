package com.littlebird.userservice.contants;

public enum UserCode {

    USERNAME_EXIST("Username already exist"),
    PASSWORD_NOT_STRONG("Password not met the criteria"),
    EMAIL_FORMAT_INVALID("Invalid email format"),
    USERNAME_NOT_EXIST("Username not exist"),

    LOGIN_SUCCESS("User verified successfully"),

    LOGIN_FAILED("Check the username & password");

    private String message;

    UserCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
