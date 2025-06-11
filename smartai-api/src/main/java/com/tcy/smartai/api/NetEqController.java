package com.tcy.smartai.api;

import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.common.CommonResponse;
import com.tcy.smartai.service.entity.CamerasEntity;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.CameraService;
import com.tcy.smartai.service.service.NetEqService;
import com.tcy.smartai.service.vo.CameraRespVo;
import com.tcy.smartai.service.vo.NetEqVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/netEq/v1")
public class NetEqController {
    public static Logger logger = LoggerFactory.getLogger(NetEqController.class);

    @Autowired
    NetEqService netEqService;

    @Autowired
    CameraService cameraService;

    @PostMapping("registration")
    @ResponseBody
    public CommonResponse registerNetEq(@RequestBody NetEqVo netEqVo) {
        logger.info("注册网元,netEqVo:{}", JSON.toJSONString(netEqVo));
        try{
            netEqService.registerNetEq(netEqVo);
        }catch (BusinessException e){
            logger.error("注册网元失败,param:{},error_code:{},error_msg:{}",JSON.toJSONString(netEqVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
            logger.error("注册网元失败,param:{},error_msg:{}",JSON.toJSONString(netEqVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(null);
    }


    @PostMapping("heartbeat")
    @ResponseBody
    public CommonResponse heartBeat(@RequestBody NetEqVo netEqVo) {
        logger.info("网元心跳,netEqVo:{}", JSON.toJSONString(netEqVo));
        int recVersion = 0;
        try{
            recVersion = netEqService.heartbeatNetEq(netEqVo);
        }catch (BusinessException e){
            logger.error("网元心跳失败,param:{},error_code:{},error_msg:{}",JSON.toJSONString(netEqVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
            logger.error("网元心跳失败,param:{},error_msg:{}",JSON.toJSONString(netEqVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(recVersion);
    }

    @PostMapping("getCameraInfo")
    @ResponseBody
    public CommonResponse getCameraInfo(@RequestBody NetEqVo netEqVo) {
        logger.info("获取摄像头信息，netEqVo:{}", JSON.toJSONString(netEqVo));
        List<CameraRespVo> camerasVos =new ArrayList<CameraRespVo>();
        try{
            String netEqId = netEqVo.getNetEqID();
            camerasVos = cameraService.getCamerasByNetEq(netEqId);
        }catch (BusinessException e){
            logger.error("获取摄像头信息失败,param:{},error_code:{},error_msg:{}",JSON.toJSONString(netEqVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
            logger.error("获取摄像头信息失败,param:{},error_msg:{}",JSON.toJSONString(netEqVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(camerasVos);
    }
}
