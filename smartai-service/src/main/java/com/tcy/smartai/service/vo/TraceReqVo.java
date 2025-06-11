package com.tcy.smartai.service.vo;

import lombok.Data;

@Data
public class TraceReqVo {
    Long start;

    Long end;

    String imageUrl;

    int pageSize;

    String requestUuid;
}
