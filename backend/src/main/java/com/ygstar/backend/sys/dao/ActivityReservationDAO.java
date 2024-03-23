package com.ygstar.backend.sys.dao;

import lombok.Data;

@Data
public class ActivityReservationDAO {
    private int orderNumber;
    private int activityId;
    private String idCardNumber;
    private String activityName;
    private String visitorName;
    private String activityTime;
}
