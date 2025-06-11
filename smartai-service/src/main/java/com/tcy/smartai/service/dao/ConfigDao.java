package com.tcy.smartai.service.dao;

import com.tcy.smartai.service.entity.ConfigEntity;

public interface ConfigDao {

    void addConfig(ConfigEntity configEntity);

    void updateConfig(ConfigEntity configEntity);

    ConfigEntity findConfig();
}
