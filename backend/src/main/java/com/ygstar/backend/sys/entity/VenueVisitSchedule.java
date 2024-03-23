package com.ygstar.backend.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalTime;
import io.swagger.annotations.ApiModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@TableName("t_venue_visit_schedule")
@ApiModel(value = "VenueVisitSchedule对象", description = "")
public class VenueVisitSchedule implements Serializable {


    private static final long serialVersionUID = 1L;

    @TableId(value = "venue_visit_schedule_id" , type = IdType.AUTO)
    private Integer venueVisitScheduleId;

    private Integer venueId;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer maximumCapacity;

    private Integer dateWeek;

    public Integer getVenueVisitScheduleId() {
        return venueVisitScheduleId;
    }

    public void setVenueVisitScheduleId(Integer venueVisitScheduleId) {
        this.venueVisitScheduleId = venueVisitScheduleId;
    }
    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public Integer getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(Integer maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
    public Integer getDateWeek() {
        return dateWeek;
    }

    public void setDateWeek(Integer dateWeek) {
        this.dateWeek = dateWeek;
    }

    @Override
    public String toString() {
        return "VenueVisitSchedule{" +
            "venueVisitScheduleId=" + venueVisitScheduleId +
            ", venveId=" + venueId +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", maximumCapacity=" + maximumCapacity +
            ", dateWeek=" + dateWeek +
        "}";
    }
}
