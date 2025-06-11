package com.tcy.smartai.service.service;

import com.alibaba.fastjson.JSONObject;
import com.tcy.smartai.service.entity.EventEntity;

import java.util.ArrayList;

public interface NotifyService {

    public void sendEvent(EventEntity eventEntity);

    void sendGrasp(String graspCode, ArrayList<JSONObject> graspPictures);
}
