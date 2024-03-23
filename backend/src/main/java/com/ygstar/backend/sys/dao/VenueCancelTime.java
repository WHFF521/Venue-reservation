package com.ygstar.backend.sys.dao;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class VenueCancelTime {
    private LocalDateTime venueDate;
    private Integer venCancelTime;
    private LocalTime startTime;
    private LocalTime endTime;
}
