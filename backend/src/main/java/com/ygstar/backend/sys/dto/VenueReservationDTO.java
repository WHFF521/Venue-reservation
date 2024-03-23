package com.ygstar.backend.sys.dto;

import lombok.Data;

@Data
public class VenueReservationDTO {
    private int orderNumber;
    private int venueId;
    private String idCardNumber;
    private String visitorName;
    private String venueName;
    private String venueTime;
}
