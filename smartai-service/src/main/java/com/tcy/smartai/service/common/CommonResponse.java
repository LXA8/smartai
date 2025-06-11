package com.tcy.smartai.service.common;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> implements Serializable {

    public static Logger logger = LoggerFactory.getLogger(CommonResponse.class);

    private static final long serialVersionUID = -5809782578272943999L;
    private static final Integer SUCCESS_CODE = 200;
    private Integer code;
    private String msg;
    private T content;

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String message) {
        this.msg = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public CommonResponse() {
    }

    public CommonResponse(ErrorCodeEnum errorCodeEnum) {
        this.code = errorCodeEnum.getCode();
        this.msg = errorCodeEnum.getDesc();
    }

    public static <T> CommonResponse<T> success(T data) {
        CommonResponse<T> response = new CommonResponse();
        response.setCode(SUCCESS_CODE);
        if(data != null){
            response.setContent(data);
        }
        response.setMsg("success");
        logger.info("返回响应数据:{}", JSON.toJSONString(response));
        return response;
    }

    public static <T> CommonResponse<T> fail(int errorCode, String errorMessage) {
        CommonResponse<T> response = new CommonResponse();
        response.setCode(errorCode);
        response.setMsg(errorMessage);
        logger.error("返回响应数据:{}", JSON.toJSONString(response));
        return response;
    }

    public static <T> CommonResponse<T> fail(ErrorCodeEnum errorCodeEnum) {
        CommonResponse<T> response = new CommonResponse();
        response.setCode(errorCodeEnum.getCode());
        response.setMsg(errorCodeEnum.getDesc());
        logger.error("返回响应数据:{}", JSON.toJSONString(response));
        return response;
    }

    public static <T> CommonResponse<T> fail(BusinessException businessException) {
        CommonResponse<T> response = new CommonResponse();
        response.setCode(businessException.getCode());
        response.setMsg(businessException.getMessage());
        logger.error("返回响应数据:{}", JSON.toJSONString(response));
        return response;
    }

//    public boolean isSuccess() {
//        return SUCCESS_CODE.equals(this.code);
//    }
}
