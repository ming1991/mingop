package com.huawei.mops.api;

/**
 * Created by tWX366549 on 2016/8/30.
 */
public class APIException extends RuntimeException {
    private int code;
    private String message;

    public APIException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public APIException(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
