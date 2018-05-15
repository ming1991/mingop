package com.huawei.mops.api;

/**
 * Created by tWX366549 on 2016/12/7.
 */
public class NoPermissionsException extends RuntimeException {
    private String message;

    public NoPermissionsException(String message) {
        this.message = message;
    }

    public NoPermissionsException() {
        this.message = "no permissions";
    }


    @Override
    public String getMessage() {
        return message;
    }
}
