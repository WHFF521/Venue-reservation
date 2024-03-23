package com.ygstar.backend.sys.controller;

import com.ygstar.backend.sys.entity.Venue;
import com.ygstar.backend.sys.dao.VenueAdminDAO;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IVenueService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/sys/venue")
@CrossOrigin
public class VenueController {
    @Autowired
    private IVenueService venueService;

    @GetMapping("/getBasicVenue" )
    @ApiOperation("获取场馆基本信息")
    public PageBean getBasicVenues(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize) {
        return venueService.getBasicVenues(page,pageSize);
    }

    @GetMapping("/VenueList")
    public List<Venue> VenueList(){
        return venueService.VenueList();
    }
    @GetMapping("/VenueInfo")
    public Venue VenueInfo(Integer venueId){
        return venueService.VenueInfo(venueId);
    }

    @GetMapping("/getTopEightVenue")
    @ApiOperation("获取热门8个场馆")
    public List<Venue> getTopEightVenue(){
        return venueService.getTopEightVenue();
    }

    @GetMapping("/searchVenueByName")
    @ApiOperation("根据名称模糊搜索场馆")
    public List<Venue> searchVenueByName(String venueName){
        return venueService.searchVenueByName("%"+venueName+"%");
    }

    @GetMapping("/getById")
    public Venue getVenueById(Integer venueId) {
        return venueService.getVenueById(venueId);
    }

    @PutMapping("/updateVenue")
    public void updateVenue(@RequestBody Venue venue) {
        venueService.updateVenue(venue);
    }

    @PutMapping("/newVenue")
    @ApiOperation("新建场馆")
    public void newVenue(@RequestBody Venue venue) {
        venueService.newVenue(venue);
    }

    //    删除场馆（标记为1）
    @PutMapping("/updateVenueStatus")
    @ApiOperation("更新场馆状态")
    public void deleteVenue(@RequestBody VenueAdminDAO venueTable){
        if (!venueTable.getDeleteFlag()){
            venueService.deleteVenue(venueTable.getVenueId());
        }else{
        venueService.regainVenue(venueTable.getVenueId());
    }
    }


}
