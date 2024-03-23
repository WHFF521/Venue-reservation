package com.ygstar.backend.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@TableName("t_venue")
@ApiModel(value = "Venue对象", description = "")
public class Venue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "venue_id", type = IdType.AUTO)
    private Integer venueId;

    private String venueName;

    private String venueAddress;

    private String phoneNumber;

    private String venueIntroduction;

    private String guideLine;

    private BigDecimal rating;

    private Integer cancelTime;

    private Boolean deleteFlag;

    private String openTime;

    private String openDays;

    private Integer advanceBookday;

    private String picture;

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }
    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getVenueIntroduction() {
        return venueIntroduction;
    }

    public void setVenueIntroduction(String venueIntroduction) {
        this.venueIntroduction = venueIntroduction;
    }
    public String getGuideLine() {
        return guideLine;
    }

    public void setGuideLine(String guideLine) {
        this.guideLine = guideLine;
    }
    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
    public Integer getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Integer cancelTime) {
        this.cancelTime = cancelTime;
    }
    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
    public String getOpenDays() {
        return openDays;
    }

    public void setOpenDays(String openDays) {
        this.openDays = openDays;
    }
    public Integer getAdvanceBookday() {
        return advanceBookday;
    }

    public void setAdvanceBookday(Integer advanceBookday) {
        this.advanceBookday = advanceBookday;
    }
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Venue{" +
            "venueId=" + venueId +
            ", venueName=" + venueName +
            ", venueAddress=" + venueAddress +
            ", phoneNumber=" + phoneNumber +
            ", venueIntroduction=" + venueIntroduction +
            ", guideLine=" + guideLine +
            ", rating=" + rating +
            ", cancelTime=" + cancelTime +
            ", deleteFlag=" + deleteFlag +
            ", openTime=" + openTime +
            ", openDays=" + openDays +
            ", advanceBookday=" + advanceBookday +
            ", picture=" + picture +
        "}";
    }
}
