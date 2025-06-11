package com.tcy.smartai.service.entity;

import lombok.Data;

import java.util.List;

@Data
public class TraceResultEntity {
    String cameraSn;

    String backgroundImage;

    Long timestamp;

    List position;
}
