package com.tcy.smartai.service.service;

import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.vo.TraceReqVo;
import com.tcy.smartai.service.vo.TraceRespBodyVo;

import java.util.List;

public interface TrackService {
    TraceRespBodyVo getFeature(TraceReqVo traceReqVo)throws BusinessException;

    void getTrace(TraceRespBodyVo traceRespBodyVo)throws BusinessException;
}
