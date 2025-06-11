package com.tcy.smartai.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcy.smartai.service.common.CommonResponse;
import com.tcy.smartai.service.common.OkHttpCli;
import com.tcy.smartai.service.entity.ConfigEntity;
import com.tcy.smartai.service.entity.TraceResultEntity;
import com.tcy.smartai.service.entity.TraceResultListEntity;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.ConfigService;
import com.tcy.smartai.service.service.TrackService;
import com.tcy.smartai.service.vo.TraceReqVo;
import com.tcy.smartai.service.vo.TraceRespBodyVo;
import com.tcy.smartai.service.vo.TraceRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    public static Logger logger = LoggerFactory.getLogger(TrackServiceImpl.class);

    @Autowired
    private OkHttpCli okHttpCli;

    @Autowired
    ConfigService configService;

    @Value("${path.url.trace}")
    private String traceUrl;

    @Value("${path.url.image}")
    private String imageUrl;

    @Override
    public TraceRespBodyVo getFeature(TraceReqVo traceReqVo) throws BusinessException {
        if(traceReqVo ==  null){
            logger.error("追踪人员,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            String imageUrl = traceReqVo.getImageUrl();
            Long start = traceReqVo.getStart();
            Long end = traceReqVo.getEnd();
            String requestUuid = traceReqVo.getRequestUuid();
            int pageSize = traceReqVo.getPageSize();
            if(StringUtils.isEmpty(imageUrl) || StringUtils.isEmpty(requestUuid) || start ==0L
                || end == 0L || pageSize ==0){
                logger.error("追踪人员,必填参数为空");
                throw  new BusinessException(ErrorCodeEnum.TRACE_AVALID_NULL);
            }
            String request = JSON.toJSONString(traceReqVo);
            logger.info("向追踪平台请求图像数据:{}",request);
            String response = okHttpCli.doPostJson(traceUrl,request);
            if(StringUtils.isEmpty(response)){
                logger.error("追踪人员,返回数据为空");
                throw  new BusinessException(ErrorCodeEnum.TRACE_RESPONSE_NULL);
            }
            logger.info("追踪平台返回图像数据，返回数据:{}",response);
            TraceRespBodyVo traceRespBodyVo = JSON.parseObject(response,TraceRespBodyVo.class);
            traceRespBodyVo  = parseResponse(traceRespBodyVo);
            return traceRespBodyVo;

        }catch (BusinessException e1){
            throw e1;
        } catch(Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }


    @Override
    @Async("threadPoolTaskExecutor")
    public void getTrace(TraceRespBodyVo traceRespBodyVo) {
         parseResponse(traceRespBodyVo);
         //转发到UI平台
        logger.debug("异步返回追踪信息到UI平台:{}",JSON.toJSONString(traceRespBodyVo));
        ConfigEntity configEntity = configService.findConfig();
        if(configEntity ==  null ||StringUtils.isEmpty(configEntity.getAsyncRespUrl())){
            logger.error("异步返回追踪url为空");
            return;
        }
        try{
            String url = configEntity.getAsyncRespUrl();
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(traceRespBodyVo.getCode());
            commonResponse.setMsg(traceRespBodyVo.getMsg());
            TraceRespVo traceRespVo = new TraceRespVo();
//            traceRespVo.setResponseType(traceRespBodyVo.getResponseType());
            traceRespVo.setData(traceRespBodyVo.getData());
            traceRespVo.setRequestUuid(traceRespBodyVo.getRequestUuid());
            commonResponse.setContent(traceRespVo);
            logger.info("开始异步返回UI平台追踪信息");
            String responseStr = okHttpCli.doPostJson(url,JSON.toJSONString(commonResponse));
            logger.info("结束异步返回UI平台追踪信息:{}",responseStr);
        }catch (Exception e) {
            logger.error("异步返回追踪信息到UI平台失败", e);
        }

    }

    private TraceRespBodyVo parseResponse(TraceRespBodyVo traceRespBodyVo){
        if(traceRespBodyVo ==  null){
            logger.info("追踪人员解析，返回数据为空");
            return traceRespBodyVo;
        }
        int code = traceRespBodyVo.getCode();
        String msg = traceRespBodyVo.getMsg();
        if(code != 200){
            logger.info("追踪人员解析，返回数据错误，code:{},msg:{}",code,msg);
            return traceRespBodyVo;
        }

        //替换图片的地址
        TraceResultListEntity traceResultListEntity = traceRespBodyVo.getData();
        if(traceResultListEntity ==  null){
            logger.info("追踪人员解析，返回data为null");
            return traceRespBodyVo;
        }
        List<TraceResultEntity> list = traceResultListEntity.getList();
        if(list ==  null || list.size() == 0){
            logger.info("追踪人员解析，返回图片列表为空");
            return traceRespBodyVo;
        }

        for( TraceResultEntity entity : list){
            String image = entity.getBackgroundImage();
            image = imageUrl+image;
            entity.setBackgroundImage(image);
        }
        return traceRespBodyVo;
    }
}
