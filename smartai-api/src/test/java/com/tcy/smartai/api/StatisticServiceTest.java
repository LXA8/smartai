package com.tcy.smartai.api;

import com.tcy.smartai.service.service.StatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticServiceTest {

    @Autowired
    private StatisticService statisticService;

    @Test
    void statistic(){
        statisticService.scheduleDetecteData();

    }
}
