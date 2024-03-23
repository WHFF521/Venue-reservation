package com.ygstar.backend.sys.controller;

import com.ygstar.backend.sys.entity.Activity;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.pojo.ResponseData;
import com.ygstar.backend.sys.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import com.ygstar.backend.sys.entity.Activity;
import com.ygstar.backend.sys.service.IActivityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/sys/activity")
public class ActivityController {
    @Autowired
    public IActivityService activityService;
    @GetMapping("/getOngoingActivities")
    public PageBean getOngoingActivities(Integer page,  Integer pageSize, Integer venueId) {
        PageBean pageBean= activityService.getOngoingActivities(page, pageSize, venueId);
        return pageBean;
    }

    @GetMapping("/getExpiredActivities")
    public PageBean getExpiredActivities(Integer page,  Integer pageSize, Integer venueId) {
        PageBean pageBean= activityService.getExpiredActivities(page, pageSize, venueId);
        return pageBean;
    }

    @GetMapping("/searchOngoingActivities")
    public  PageBean selectOngoingActivitiesByActName(Integer page, Integer pageSize, Integer venueId, String actName) {
        PageBean pageBean=activityService.selectOngoingActivitiesByActName(page, pageSize, venueId, actName);
        return pageBean;
    }

    @GetMapping("/searchExpiredActivities")
    public  PageBean selectExpiredActivitiesByActName(Integer page, Integer pageSize, Integer venueId, String actName) {
        PageBean pageBean=activityService.selectExpiredActivitiesByActName(page, pageSize, venueId, actName);
        return pageBean;
    }

    @GetMapping("/updateActivity")
    public ResponseData updateActivity(Activity activity,String actDateString, String actEarliestBookingTimeString,String actLatestBookingtimeString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDate actDate = LocalDate.parse(actDateString.substring(0, 10),dateFormatter);
        LocalDateTime earliestTime = LocalDateTime.parse(actEarliestBookingTimeString, formatter);
        LocalDateTime latestTime = LocalDateTime.parse(actLatestBookingtimeString,formatter);
        activity.setActDate(actDate);
        activity.setActEarliestBookingTime(earliestTime);
        activity.setActLatestBookingtime(latestTime);
        activityService.updateActivity(activity);
        return new ResponseData("更新成功");
    }

    @GetMapping("/deleteActivity")
    public ResponseData deleteActivity(Integer actId){
        activityService.deleteAct(actId);
        return new ResponseData("删除成功");
    }

    @GetMapping("/createActivity")
    public void createActivity(Activity activity,String actDateString, String actEarliestBookingTimeString,String actLatestBookingtimeString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate actDate = LocalDate.parse(actDateString.substring(0, 10),dateFormatter);
        LocalDateTime earliestTime = LocalDateTime.parse(actEarliestBookingTimeString, formatter);
        LocalDateTime latestTime = LocalDateTime.parse(actLatestBookingtimeString,formatter);
        activity.setActDate(actDate);
        activity.setActEarliestBookingTime(earliestTime);
        activity.setActLatestBookingtime(latestTime);
        activityService.createActivity(activity);
    }

    @GetMapping("/selectActByActId")
    public Activity getActByActId (Integer actId){
        return activityService.getActByActId(actId);
    }


    @GetMapping("/actlist")
    @ApiOperation("活动列表")
    public List<Activity> ActivityList(){
        return activityService.ActivityList();
    }
    @GetMapping("/actinfo")
    @ApiOperation("活动详情")
    public Activity ActivityInfo(Integer activityId){
        return activityService.ActivityInfo(activityId);
    }

    @GetMapping("/getMaximumCapacity")
    @ApiOperation("获取活动最多预约人数")
    public int getMaximumCapacity(Integer activityId){
        return activityService.getMaximumCapacity(activityId);
    }

    @GetMapping("/getTopEightActivity")
    @ApiOperation("获取热门8个活动")
    List<Activity> getTopEightActivity(){
        return activityService.getTopEightActivity();
    }

    @GetMapping("/searchActivityByName")
    @ApiOperation("根据名称模糊搜索活动")
    List<Activity> searchActivityByName(String activityName){
        return activityService.searchActivityByName("%"+activityName+"%");
    }

    @GetMapping("/getActivityByVenueId")
    @ApiOperation("查找某个场馆的所有活动")
    List<Activity> getActivityByVenueId(Integer venueId){
        return activityService.getActivityByVenueId(venueId);
    }
}
