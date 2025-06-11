package com.tcy.smartai.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.common.CommonConfig;
import com.tcy.smartai.service.dao.ConfigDao;
import com.tcy.smartai.service.entity.ConfigEntity;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.ConfigService;
import com.tcy.smartai.service.vo.ConfigVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConfigServiceImpl implements ConfigService {

    public static Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);
    Map<String,String> configMap = new HashMap<String,String>();

    @Autowired
    ConfigDao configDao;
    @Override
    public void addConfig(ConfigVo configVo) throws BusinessException {
        logger.debug("添加配置,configVo:{}", JSON.toJSONString(configVo));
        if(StringUtils.isEmpty(configVo)){
            logger.error("添加配置失败,入参为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }

        String eventUrl = configVo.getEventUrl();
        String asyncRespUrl = configVo.getAsyncRespUrl();
        if(StringUtils.isEmpty(eventUrl) || StringUtils.isEmpty(asyncRespUrl)){
            logger.error("添加配置失败,eventUrl或asyncRespUrl为空");
            throw  new BusinessException(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            ConfigEntity oldConfigEntity = configDao.findConfig();
            ConfigEntity configEntity =  new ConfigEntity();
            if(oldConfigEntity ==null){//新增
                configEntity.setEventUrl(eventUrl);
                configEntity.setAsyncRespUrl(asyncRespUrl);
                configMap.put(CommonConfig.EVENT_URL,configEntity.getEventUrl());
                configMap.put(CommonConfig.ASYNC_RESP_URL,configEntity.getAsyncRespUrl());
                configDao.addConfig(configEntity);
                logger.debug("新增配置成功,configEntity:{}", JSON.toJSONString(configEntity));
            }else{//更新
                oldConfigEntity.setAsyncRespUrl(asyncRespUrl);
                oldConfigEntity.setEventUrl(eventUrl);
                configMap.put(CommonConfig.EVENT_URL,configEntity.getEventUrl());
                configMap.put(CommonConfig.ASYNC_RESP_URL,configEntity.getAsyncRespUrl());
                configDao.updateConfig(oldConfigEntity);
                logger.debug("更新配置成功,configEntity:{}", JSON.toJSONString(oldConfigEntity));
            }
        }catch (BusinessException e1){
            throw e1;
        } catch(Exception e){
            throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }

    public ConfigEntity findConfig() throws BusinessException {
        logger.debug("查找配置");
        try{
            String eventUrl = configMap.get(CommonConfig.EVENT_URL);
            String asyncRespUrl =  configMap.get(CommonConfig.ASYNC_RESP_URL);
            ConfigEntity configEntity = new ConfigEntity();
            if(!StringUtils.isEmpty(eventUrl) && !StringUtils.isEmpty(asyncRespUrl)){
                configEntity.setEventUrl(eventUrl);
                configEntity.setAsyncRespUrl(asyncRespUrl);
                logger.debug("从缓存中获取:{}",JSON.toJSONString(configEntity));
                return configEntity;
            }
            configEntity = configDao.findConfig();
            if(configEntity != null){
                configMap.put(CommonConfig.EVENT_URL,configEntity.getEventUrl());
                configMap.put(CommonConfig.ASYNC_RESP_URL,configEntity.getAsyncRespUrl());
                logger.debug("从数据库获取:{}",JSON.toJSONString(configEntity));
            }
            return configEntity;

        }catch (BusinessException e1){
          throw e1;
        } catch(Exception e){
           throw  new BusinessException(e,ErrorCodeEnum.SERVER_CODE_ERROR);
        }
    }
}
