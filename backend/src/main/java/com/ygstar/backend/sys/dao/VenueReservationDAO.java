package com.ygstar.backend.sys.dao;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class VenueReservationDAO {
    private int orderNumber;
    private int venueId;
    private String idCardNumber;
    private String visitorName;
    private String venueName;
    private LocalDateTime bookTime;
    private LocalTime startTime;
    private LocalTime endTime;
}
