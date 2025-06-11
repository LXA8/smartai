package com.tcy.smartai.service.service;

import com.tcy.smartai.service.entity.ConfigEntity;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.vo.ConfigVo;

public interface ConfigService {
    void addConfig(ConfigVo configVo) throws BusinessException;

    public ConfigEntity findConfig() throws BusinessException;
}
