package com.tcy.smartai.service.service;

import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.vo.EventVo;
import com.tcy.smartai.service.vo.GraspPicVo;

public interface EventService {

    void addEvent(EventVo eventVo) throws BusinessException;

    void addGraspPic(GraspPicVo graspPicVo) throws BusinessException;
}
