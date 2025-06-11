package com.tcy.smartai.api;


import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.common.CommonResponse;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.TrackService;
import com.tcy.smartai.service.vo.EventVo;
import com.tcy.smartai.service.vo.TraceReqVo;
import com.tcy.smartai.service.vo.TraceRespBodyVo;
import com.tcy.smartai.service.vo.TraceRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/")
public class TrackController {

    public static Logger logger = LoggerFactory.getLogger(TrackController.class);

    @Autowired
    TrackService trackService;

    @GetMapping("features")
    @ResponseBody
    public CommonResponse getFeatures(Long start,Long end,String imageUrl,int pageSize,String requestUuid) {
        logger.info("接受UI平台请求人员追踪,start:{},end:{},imageUrl:{},pageSize:{},requestUuid:{}", start,end,imageUrl,pageSize,requestUuid);
        try{
            TraceReqVo traceReqVo = new TraceReqVo();
            traceReqVo.setStart(start);
            traceReqVo.setEnd(end);
            traceReqVo.setImageUrl(imageUrl);
            traceReqVo.setPageSize(pageSize);
            traceReqVo.setRequestUuid(requestUuid);
            TraceRespBodyVo traceRespBodyVo = trackService.getFeature(traceReqVo);
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(traceRespBodyVo.getCode());
            commonResponse.setMsg(traceRespBodyVo.getMsg());
            TraceRespVo traceRespVo = new TraceRespVo();
            traceRespVo.setResponseType("async");//默认异步
//            traceRespVo.setRequestUuid(traceRespBodyVo.getRequestUuid());
            traceRespVo.setData(traceRespBodyVo.getData());
            commonResponse.setContent(traceRespVo);
            return commonResponse;
        }catch (BusinessException e){
            e.printStackTrace();
            logger.error("追踪人员失败,start:{},end:{},imageUrl:{},pageSize:{},requestUuid:{},error_code:{},error_msg:{}",start,end,imageUrl,pageSize,requestUuid,e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
            logger.error("追踪人员失败,start:{},end:{},imageUrl:{},pageSize:{},requestUuid:{},error_msg:{}",start,end,imageUrl,pageSize,requestUuid,e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }

    @PostMapping("trace")
    @ResponseBody
    public CommonResponse getTrace(@RequestBody TraceRespBodyVo traceRespBodyVo) {
        logger.info("接收到异步返回追踪,traceRespBodyVo:{}", JSON.toJSONString(traceRespBodyVo));
        try{

            //异步调用返回信息
            trackService.getTrace(traceRespBodyVo);
        }catch (BusinessException e){
            e.printStackTrace();
            logger.error("接收到异步返回追踪失败,param:{},error_code:{},error_msg:{}",JSON.toJSONString(traceRespBodyVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
            logger.error("接收到异步返回追踪失败,param:{},error_msg:{}",JSON.toJSONString(traceRespBodyVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return CommonResponse.success(null);
    }

}
