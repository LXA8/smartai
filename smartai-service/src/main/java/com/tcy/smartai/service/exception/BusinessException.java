package com.tcy.smartai.service.exception;

import com.tcy.smartai.service.enums.ErrorCodeEnum;

public class BusinessException  extends BasicException  {

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getCode(), errorCodeEnum.getDesc());
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, String message) {
        super(errorCodeEnum.getCode(), message);
    }

    public BusinessException(Throwable e, ErrorCodeEnum errorCodeEnum) {
        super(e, errorCodeEnum.getCode(), errorCodeEnum.getDesc());
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }
}
