package com.ygstar.backend.sys.controller;

import com.ygstar.backend.sys.entity.VenueVisitSchedule;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.pojo.ResponseData;
import com.ygstar.backend.sys.service.IVenueVisitScheduleService;
import io.swagger.annotations.ApiOperation;
import com.ygstar.backend.sys.service.impl.VenueVisitScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import com.ygstar.backend.sys.entity.VenueVisitSchedule;
import com.ygstar.backend.sys.service.IVenueVisitScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
@RequestMapping("/sys/venueVisitSchedule")
@CrossOrigin
public class VenueVisitScheduleController {
    @Autowired
    private IVenueVisitScheduleService venueVisitScheduleService;

    @GetMapping("/schedules")
    public PageBean page ( Integer page,  Integer pageSize, Integer venueId) {
        PageBean pageBean = venueVisitScheduleService.page(page,pageSize,venueId);
        return pageBean;
    }

    @GetMapping("/allSchedulesByDateWeek")
    public PageBean getSchedulesByDateWeek( Integer page,  Integer pageSize, Integer venueId , Integer dateWeek) {
        PageBean pageBean = venueVisitScheduleService.getSchedulesByDateWeek(page,pageSize,venueId,dateWeek);
        return pageBean;
    }

    @GetMapping("/allSchedulesByDateWeekForNewActivity")
    public List<VenueVisitSchedule> getAllSchedulesByDateWeek(Integer venueId , Integer dateWeek){
        List<VenueVisitSchedule> venueVisitSchedules=venueVisitScheduleService.getAllSchedulesByDateWeek(venueId,dateWeek);
        return venueVisitSchedules;
    }


    @GetMapping("/insertVenueVisitSchedule")
    public ResponseData saveVenueVisitSchedule(VenueVisitSchedule venueVisitSchedule,String startTimeString, String endTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString,formatter);
        venueVisitSchedule.setStartTime(startTime);
        venueVisitSchedule.setEndTime(endTime);
        venueVisitScheduleService.insertVenueVisitSchedule(venueVisitSchedule);
        return new ResponseData("添加成功");
    }

    @DeleteMapping("/deleteVenueVisitSchedule")
    public ResponseData deleteVenueVisitSchedule(Integer venueVisitScheduleId,Integer venueId){
        venueVisitScheduleService.deleteVenueVisitSchedule(venueVisitScheduleId , venueId);
        return new ResponseData("删除成功");
    }


    @GetMapping("/getVVScheduleByVenueIdAndWeek")
    @ApiOperation(value = "根据venueId和week获取场馆参观时间表")
    public List<VenueVisitSchedule> getVenueVisitScheduleByVenueId(Integer venueId,Integer week){
        return venueVisitScheduleService.getVenueVisitScheduleByVenueId(venueId,week);
    }

    @GetMapping("/getMCByVVSId")
    @ApiOperation(value = "根据venueVisitScheduleId查询maximumCapacity")
    public Integer getMCByVVSId(Integer venueVisitScheduleId){
        return venueVisitScheduleService.getMCByVVSId(venueVisitScheduleId);
    }

}
