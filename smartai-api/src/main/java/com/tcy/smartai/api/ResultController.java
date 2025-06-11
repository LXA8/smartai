package com.tcy.smartai.api;

import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.common.CommonResponse;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.DetectionServcie;
import com.tcy.smartai.service.service.EventService;
import com.tcy.smartai.service.vo.DetectionVo;
import com.tcy.smartai.service.vo.EventVo;
import com.tcy.smartai.service.vo.GraspPicVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/result/v1")
public class ResultController {
    public static Logger logger = LoggerFactory.getLogger(ResultController.class);

    @Autowired
    EventService eventService;

    @Autowired
    DetectionServcie detectionServcie;


    @PostMapping("event")
    @ResponseBody
    public CommonResponse addEvent(@RequestBody EventVo eventVo) {
        logger.info("事件上报,eventVo:{}", JSON.toJSONString(eventVo));
        try{
            eventService.addEvent(eventVo);
        }catch (BusinessException e){
            e.printStackTrace();
            logger.error("事件上报失败,param:{},error_code:{},error_msg:{}",JSON.toJSONString(eventVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
            logger.error("事件上报失败,param:{},error_msg:{}",JSON.toJSONString(eventVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(null);
    }

    @PostMapping("graspImg")
    @ResponseBody
    public CommonResponse addGraspImg(@RequestBody GraspPicVo graspPicVo) {
        logger.info("抓拍结果上报,graspPicVo:{}", JSON.toJSONString(graspPicVo));
        try{
            eventService.addGraspPic(graspPicVo);
        }catch (BusinessException e){
            logger.error("抓拍结果上报失败,param:{},error_code:{},error_msg:{}",JSON.toJSONString(graspPicVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
            logger.error("抓拍结果上报失败:{}",e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(null);
    }

    @PostMapping("detectionResult")
    @ResponseBody
    public CommonResponse addDetection(@RequestBody DetectionVo detectionVo) {
        logger.info("检测结果上报,detectionVo:{}", JSON.toJSONString(detectionVo));
        try{
            detectionServcie.addDetection(detectionVo);
        }catch (BusinessException e){
            logger.error("检测结果上报失败,param:{},error_code:{},error_msg:{}",JSON.toJSONString(detectionVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
            logger.error("检测结果上报失败,param:{},error_msg:{}",JSON.toJSONString(detectionVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(null);
    }


}
