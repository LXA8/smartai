package com.tcy.smartai.service.service;

import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.vo.DetectionVo;

public interface DetectionServcie {
    void addDetection(DetectionVo detectionVo) throws BusinessException;;
}
