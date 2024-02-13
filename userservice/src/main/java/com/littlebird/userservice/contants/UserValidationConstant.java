package com.littlebird.userservice.contants;

public interface UserValidationConstant {

    String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d$#@!^&\\*\\.\\-]{8,15}";

    String EMAIL_PATTERN = "^[a-zA-Z0-9.\\-+%_]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

}
