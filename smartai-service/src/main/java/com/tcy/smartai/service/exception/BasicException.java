package com.tcy.smartai.service.exception;

public class BasicException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    BasicException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    BasicException(Throwable throwable, Integer code, String message) {
        super(message, throwable);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
