package com.tcy.smartai.api;

import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.common.CommonResponse;
import com.tcy.smartai.service.service.StatisticService;
import com.tcy.smartai.service.vo.CameraVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    StatisticService statisticService;

    public static Logger logger = LoggerFactory.getLogger(TestController.class);

    @PostMapping("reviceevent")
    @ResponseBody
    public CommonResponse addCamera(@RequestBody Map map){
        logger.debug("测试事件,map:{}", JSON.toJSONString(map));
        return  CommonResponse.success(null);
    }

    @PostMapping("getFeature")
    @ResponseBody
    public String getFeature(@RequestBody Map map){
        logger.debug("获取追踪图像,map:{}", JSON.toJSONString(map));
        String response="{\n" +
                "\t\"code\": 200,\n" +
                "\t\"requestUuid\": \"snewefos8d9fasn\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"list\": [{\n" +
                "\t\t\t\t\"backgroundImage\": \"data/img/20230320/SEU_test/20230320090126109_c04.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"SEU_test\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1046,\n" +
                "\t\t\t\t\t\t384,\n" +
                "\t\t\t\t\t\t47,\n" +
                "\t\t\t\t\t\t108\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679274086109\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"backgroundImage\": \"data/img/20230320/SEU_test/20230320090256112_c04.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"SEU_test\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1046,\n" +
                "\t\t\t\t\t\t384,\n" +
                "\t\t\t\t\t\t46,\n" +
                "\t\t\t\t\t\t109\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679274176112\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"backgroundImage\": \"/home/seu228228/smartai/ftp/data/img/20230320/SEU_test/20230320090456122_c04.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"SEU_test\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1046,\n" +
                "\t\t\t\t\t\t384,\n" +
                "\t\t\t\t\t\t46,\n" +
                "\t\t\t\t\t\t108\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679274296122\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"backgroundImage\": \"data/img/20230320/SEU_test/20230320091146100_c04.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"SEU_test\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1047,\n" +
                "\t\t\t\t\t\t383,\n" +
                "\t\t\t\t\t\t45,\n" +
                "\t\t\t\t\t\t110\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679274706100\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"backgroundImage\": \"data/img/20230320/SEU_test/20230320091216100_c04.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"SEU_test\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1047,\n" +
                "\t\t\t\t\t\t384,\n" +
                "\t\t\t\t\t\t45,\n" +
                "\t\t\t\t\t\t108\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679274736100\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"backgroundImage\": \"data/img/20230320/test445566/20230320090136109_c05.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"test445566\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1046,\n" +
                "\t\t\t\t\t\t384,\n" +
                "\t\t\t\t\t\t46,\n" +
                "\t\t\t\t\t\t108\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679274096109\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"backgroundImage\": \"data/img/20230320/test445566/20230320090256112_c05.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"test445566\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1047,\n" +
                "\t\t\t\t\t\t383,\n" +
                "\t\t\t\t\t\t46,\n" +
                "\t\t\t\t\t\t110\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679274176112\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"backgroundImage\": \"data/img/20230320/test445566/20230320090356127_c05.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"test445566\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1047,\n" +
                "\t\t\t\t\t\t383,\n" +
                "\t\t\t\t\t\t45,\n" +
                "\t\t\t\t\t\t110\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679274236127\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"backgroundImage\": \"data/img/20230320/test445566/20230320091216100_c05.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"test445566\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1046,\n" +
                "\t\t\t\t\t\t384,\n" +
                "\t\t\t\t\t\t46,\n" +
                "\t\t\t\t\t\t109\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679274736100\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"backgroundImage\": \"data/img/20230320/test445566/20230320091842373_c05.jpg\",\n" +
                "\t\t\t\t\"cameraSn\": \"test445566\",\n" +
                "\t\t\t\t\"position\": [\n" +
                "\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t1046,\n" +
                "\t\t\t\t\t\t384,\n" +
                "\t\t\t\t\t\t47,\n" +
                "\t\t\t\t\t\t110\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"timestamp\": 1679275122373\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t\"msg\": \"success\"\n" +
                "}";
        return  response;
    }

    @PostMapping("statistic")
    @ResponseBody
    public CommonResponse statistic(@RequestBody Map map){
        statisticService.scheduleDetecteData();
        return  CommonResponse.success(null);
    }

    @PatchMapping("revicepatch")
    @ResponseBody
    public CommonResponse addPatch(@RequestBody Map map){
        logger.debug("测试事件,map:{}", JSON.toJSONString(map));
        return  CommonResponse.success(null);
    }


}
