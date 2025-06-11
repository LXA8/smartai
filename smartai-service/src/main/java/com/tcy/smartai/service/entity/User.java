package com.tcy.smartai.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "user")
@Data
public class User implements Serializable {

    @Id// 必须指定id列
    private String id;

    private String name;

    private String pwd;
}
