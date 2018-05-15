package com.huawei.mops.api;

/**
 * Created by tWX366549 on 2016/11/9.
 */
public class LoginException extends RuntimeException {
    private String message;

    public LoginException(String message) {
        this.message = message;
    }

    public LoginException() {
        this.message = "please login first";
    }



    @Override
    public String getMessage() {
        return message;
    }
}
