package com.ygstar.backend.sys.dao;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

//获取计算违规取消时间区间的类
@Data
public class ActivityCancelTime {
    private LocalDate actDate;
    private Integer actCancelTime;
    private LocalTime startTime;
    private LocalTime endTime;
}
