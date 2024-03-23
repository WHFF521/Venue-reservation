package com.ygstar.backend.sys.dao;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class VenueProfileDAO {
    private LocalDateTime bookTime;
    private String userMail;
    private String idCardNumber;
}
