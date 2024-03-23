package com.ygstar.backend.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@TableName("t_activity")
@ApiModel(value = "Activity对象", description = "")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "act_id", type = IdType.AUTO)
    private Integer actId;

    private String actAddress;

    private String actName;

    private Integer actParticipantNum;

    private String actIntroduction;

    private String actGuideline;

    private BigDecimal actRating;

    private Integer venueId;

    private Integer actCancelTime;

    private LocalDate actDate;

    private String actTime;

    private Boolean deleteFlag;

    private LocalDateTime actEarliestBookingTime;

    private LocalDateTime actLatestBookingtime;

    private Integer venueVisitScheduleId;

    private String actPicture;

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }
    public String getActAddress() {
        return actAddress;
    }

    public void setActAddress(String actAddress) {
        this.actAddress = actAddress;
    }
    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }
    public Integer getActParticipantNum() {
        return actParticipantNum;
    }

    public void setActParticipantNum(Integer actParticipantNum) {
        this.actParticipantNum = actParticipantNum;
    }
    public String getActIntroduction() {
        return actIntroduction;
    }

    public void setActIntroduction(String actIntroduction) {
        this.actIntroduction = actIntroduction;
    }
    public String getActGuideline() {
        return actGuideline;
    }

    public void setActGuideline(String actGuideline) {
        this.actGuideline = actGuideline;
    }
    public BigDecimal getActRating() {
        return actRating;
    }

    public void setActRating(BigDecimal actRating) {
        this.actRating = actRating;
    }
    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }
    public Integer getActCancelTime() {
        return actCancelTime;
    }

    public void setActCancelTime(Integer actCancelTime) {
        this.actCancelTime = actCancelTime;
    }
    public LocalDate getActDate() {
        return actDate;
    }

    public void setActDate(LocalDate actDate) {
        this.actDate = actDate;
    }
    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }
    public LocalDateTime getActEarliestBookingTime() {
        return actEarliestBookingTime;
    }

    public void setActEarliestBookingTime(LocalDateTime actEarliestBookingTime) {
        this.actEarliestBookingTime = actEarliestBookingTime;
    }
    public LocalDateTime getActLatestBookingtime() {
        return actLatestBookingtime;
    }

    public void setActLatestBookingtime(LocalDateTime actLatestBookingtime) {
        this.actLatestBookingtime = actLatestBookingtime;
    }
    public Integer getVenueVisitScheduleId() {
        return venueVisitScheduleId;
    }

    public void setVenueVisitScheduleId(Integer venueVisitScheduleId) {
        this.venueVisitScheduleId = venueVisitScheduleId;
    }
    public String getActPicture() {
        return actPicture;
    }

    public void setActPicture(String actPicture) {
        this.actPicture = actPicture;
    }
    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "Activity{" +
            "actId=" + actId +
            ", actAddress=" + actAddress +
            ", actName=" + actName +
            ", actParticipantNum=" + actParticipantNum +
            ", actIntroduction=" + actIntroduction +
            ", actGuideline=" + actGuideline +
            ", actRating=" + actRating +
            ", venueId=" + venueId +
            ", actCancelTime=" + actCancelTime +
            ", actDate=" + actDate +
            ", actTime=" + actTime +
            ", actEarliestBookingTime=" + actEarliestBookingTime +
            ", actLatestBookingtime=" + actLatestBookingtime +
            ", venueVisitScheduleId=" + venueVisitScheduleId +
            ", actPicture=" + actPicture +
        "}";
    }
}
