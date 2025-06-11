package com.tcy.smartai.api;


import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.common.CommonResponse;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.service.ConfigService;
import com.tcy.smartai.service.vo.ConfigVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1")
public class ConfigController {
    public static Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    ConfigService configService;

    @PutMapping("/config")
    @ResponseBody
    public CommonResponse config(@RequestBody ConfigVo configVo) {
        logger.info("配置地址,configVo:{}", JSON.toJSONString(configVo));
        Map result = new HashMap();
        if(StringUtils.isEmpty(configVo)){
            logger.error("配置地址失败，入参为空");
            return CommonResponse.fail(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            configService.addConfig(configVo);
        }catch(BusinessException e){
            logger.error("配置地址失败:{},configVo:{},error_code:{},error_msg:{}",JSON.toJSONString(configVo),e.getCode(),e);
            return CommonResponse.fail(ErrorCodeEnum.CONFIG_ERROR);
        }catch (Exception e1){
            logger.error("配置地址失败,configVo:{},error_msg:{}",JSON.toJSONString(configVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
       return  CommonResponse.success(null);
    }
}
