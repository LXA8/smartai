package com.tcy.smartai.api;

import com.tcy.smartai.service.common.OkHttpCli;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CameraControllerTest {

    @Autowired
    private OkHttpCli okHttpCli;




    @Test
    void addCamera(){
      String url = "http://127.0.0.1:8080/api/v1/camera/add";
      String body ="{\n" +
              "\t\"cameraSn\": \"ashdbha\",\n" +
              "\t\"lngLat\": \"121.406726,31.22055\",\n" +
              "\t\"address\": \"开江路 xx 号\",\n" +
              "\t\"streamUrl\": \"rtsp://192.168.2.1/a/b/c\",\n" +
              "\t\"algorithms\": [{\n" +
              "\t\t\t\"code\": \"S001\",\n" +
              "\t\t\t\"define\": {\n" +
              "\t\t\t\t\"threshold\": 10,\n" +
              "\t\t\t\t\"photo\": 2,\n" +
              "\t\t\t\t\"photoInterval\": 500,\n" +
              "\t\t\t\t\"reformTime\": 10\n" +
              "\t\t\t}\n" +
              "\t\t},\n" +
              "\t\t{\n" +
              "\t\t\t\"code\": \"S002\"\n" +
              "\t\t},\n" +
              "\t\t{\n" +
              "\t\t\t\"code\": \"S003\",\n" +
              "\t\t\t\"define\": {\n" +
              "\t\t\t\t\"photo\": 5,\n" +
              "\t\t\t\t\"photoInterval\": 1000,\n" +
              "\t\t\t\t\"reformTime\": 40\n" +
              "\t\t\t}\n" +
              "\t\t},\n" +
              "\t\t{\n" +
              "\t\t\t\"code\": \"S004\",\n" +
              "\t\t\t\"define\": {\n" +
              "\t\t\t\t\"photo\": 5,\n" +
              "\t\t\t\t\"photoInterval\": 1000,\n" +
              "\t\t\t\t\"reformTime\": 100\n" +
              "\t\t\t}\n" +
              "\t\t},\n" +
              "\t\t{\n" +
              "\t\t\t\"code\": \"S005\",\n" +
              "\t\t\t\"define\": {\n" +
              "\t\t\t\t\"photo\": 3,\n" +
              "\t\t\t\t\"photoInterval\": 1000,\n" +
              "\t\t\t\t\"reformTime\": 60\n" +
              "\t\t\t}\n" +
              "\t\t},\n" +
              "\t\t{\n" +
              "\t\t\t\"code\": \"S006\"\n" +
              "\t\t}\n" +
              "\t]\n" +
              "}";
        String respone = okHttpCli.doPostJson(url,body);
        System.out.print("response:"+respone);
    }
}
