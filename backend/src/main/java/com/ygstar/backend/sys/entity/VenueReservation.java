package com.ygstar.backend.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@TableName("t_venue_reservation")
@ApiModel(value = "VenueReservation对象", description = "")
public class VenueReservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_number", type = IdType.AUTO)
    private Integer orderNumber;

    private Integer venueId;

    private LocalDateTime bookTime;

    private String userMail;

    private String idCardNum;
    // 0:已预约 1:已参观未评分 2:已取消 3:已过期 4:已评分
    private Integer status;

    private BigDecimal score;

    private Integer venueVisitScheduleId;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }
    public LocalDateTime getBookTime() {
        return bookTime;
    }

    public void setBookTime(LocalDateTime bookTime) {
        this.bookTime = bookTime;
    }
    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getVenueVisitScheduleId() {
        return venueVisitScheduleId;
    }

    public void setVenueVisitScheduleId(Integer venueVisitScheduleId) {
        this.venueVisitScheduleId = venueVisitScheduleId;
    }

    public void setScore(BigDecimal score){this.score = score;}

    public BigDecimal getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "VenueReservation{" +
            "orderNumber=" + orderNumber +
            ", venueId=" + venueId +
            ", bookTime=" + bookTime +
            ", userMail=" + userMail +
            ", idCardNum=" + idCardNum +
            ", status=" + status +
            ", venueVisitScheduleId=" + venueVisitScheduleId +
             ", score="   + score +
        "}";
    }
}
