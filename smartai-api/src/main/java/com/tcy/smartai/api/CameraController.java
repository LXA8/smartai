package com.tcy.smartai.api;

import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.common.CommonResponse;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.CameraService;
import com.tcy.smartai.service.service.StatisticService;
import com.tcy.smartai.service.vo.CameraVo;
import com.tcy.smartai.service.vo.StatisticRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/v1/cameras")
public class CameraController {
    public static Logger logger = LoggerFactory.getLogger(CameraController.class);

    @Autowired
    CameraService cameraService;

    @Autowired
    StatisticService statisticService;


    @PostMapping("")
    @ResponseBody
    public CommonResponse addCamera(@RequestBody CameraVo cameraVo) {
        logger.info("添加摄像头配置,CameraVo:{}", JSON.toJSONString(cameraVo));
        if(StringUtils.isEmpty(cameraVo)){
            logger.error("配置地址失败，入参为空");
            return CommonResponse.fail(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            cameraService.addCameras(cameraVo);
        }catch (BusinessException e){
//            e.printStackTrace();
            logger.error("添加摄像头配置失败,cameraVo:{},code:{},msg:{}",JSON.toJSONString(cameraVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
//            e1.printStackTrace();
            logger.error("添加摄像头配置失败,cameraVo:{},msg:{}",JSON.toJSONString(cameraVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(null);
    }

    @PutMapping("/{cameraSn}")
    @ResponseBody
    public CommonResponse updateCamera(@PathVariable("cameraSn") String cameraSn,@RequestBody CameraVo cameraVo) {
        logger.info("更新摄像头配置,CameraVo:{}", JSON.toJSONString(cameraVo));
        if(StringUtils.isEmpty(cameraVo) || StringUtils.isEmpty(cameraVo)){
            logger.error("更新摄像头失败，入参为空");
            return CommonResponse.fail(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        if(StringUtils.isEmpty(cameraVo.getCameraSn())){
            logger.error("更新摄像头失败，camerasn为空");
            return CommonResponse.fail(ErrorCodeEnum.CAMERAS_SN_NULL);
        }
        try{
            cameraService.updateCameras(cameraSn,cameraVo);

        }catch (BusinessException e){
//            e.printStackTrace();
            logger.error("更新摄像头失败,cameraSn:{},cameraVo:{},code:{},msg:{}",cameraSn,JSON.toJSONString(cameraVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
//            e1.printStackTrace();
            logger.error("更新摄像头失败,cameraSn:{},cameraVo:{},msg:{}",cameraSn,JSON.toJSONString(cameraVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(null);
    }

    @DeleteMapping("/{cameraSn}")
    @ResponseBody
    public CommonResponse deleteCamera(@PathVariable("cameraSn") String cameraSn) {
        logger.info("删除摄像头配置,cameraSn:{}", cameraSn);
        if(StringUtils.isEmpty(cameraSn) ){
            logger.error("删除摄像头失败，入参为空");
            return CommonResponse.fail(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            cameraService.deleteCameras(cameraSn);

        }catch (BusinessException e){
//            e.printStackTrace();
            logger.error("删除摄像头失败,cameraSn:{},code:{},msg:{}",cameraSn,e.getCode(),e.getMessage());
            return CommonResponse.fail(e);
        }catch (Exception e1){
//            e1.printStackTrace();
            logger.error("删除摄像头失败,cameraSn:{},code:{},msg:{}",cameraSn,e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(null);
    }

    @GetMapping("/{cameraSn}/analysis")
    @ResponseBody
    public CommonResponse analysisData(@PathVariable("cameraSn") String cameraSn,String date){
        logger.info("统计摄像头采集的数据,cameraSn:{},date:{}", cameraSn,date);
        StatisticRespVo statisticRespVo = null;
        try{
            statisticRespVo = statisticService.getStatisticData(cameraSn,date);
        }catch (BusinessException e){
            logger.error("统计摄像头采集的数据失败,cameraSn:{},date:{},error_code:{},error_msg:{}",cameraSn,date,e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
            logger.error("统计摄像头采集的数据失败,cameraSn:{},date:{},error_msg:{}",cameraSn,date,e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(statisticRespVo);
    }

    @GetMapping("/analysis")
    @ResponseBody
    public CommonResponse analysisAllData(String date)
    {
       return analysisData(null,date);
    }
}
