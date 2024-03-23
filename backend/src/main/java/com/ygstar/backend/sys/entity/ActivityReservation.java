package com.ygstar.backend.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@TableName("t_activity_reservation")
@ApiModel(value = "ActivityReservation对象", description = "")
public class ActivityReservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_number", type = IdType.AUTO)
    private Integer orderNumber;

    private LocalDateTime bookingTime;

    private String userMail;

    private String idCardNumber;
    // 0:已预约 1:已参观未评分 2:已取消 3:已过期 4:已评分
    private Integer status;

    private Integer activityId;
    private BigDecimal score;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "ActivityReservation{" +
            "orderNumber=" + orderNumber +
            ", bookingTime=" + bookingTime +
            ", userMail=" + userMail +
            ", idCardNumber=" + idCardNumber +
            ", status=" + status +
            ", activityId=" + activityId +
                ", score="   + score +
        "}";
    }
}
