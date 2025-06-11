package com.tcy.smartai.service.dao;

import com.tcy.smartai.service.entity.EventEntity;

import java.util.List;

public interface EventDao {

    void addEvent(EventEntity eventEntity);

    void updateEvent(EventEntity eventEntity);

    void updateGraspPictures(String cameraSn, String graspCode, List graspPictures);

    EventEntity findEventByGraspCode(String cameraSn, String graspCode);
}
