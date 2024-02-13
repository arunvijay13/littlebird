package com.littlebird.userservice.contants;

public enum JWTErrorCode {

    JWT_GEN_FAILED("Failed to generate JWT token"),

    INVALID_JWT_TOKEN("Invalid JWT token");


    private String message;

    JWTErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
