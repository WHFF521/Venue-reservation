package com.ygstar.backend.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygstar.backend.sys.entity.VenueVisitSchedule;
import com.ygstar.backend.sys.mapper.VenueMapper;
import com.ygstar.backend.sys.mapper.VenueVisitScheduleMapper;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IVenueVisitScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@Service
public class VenueVisitScheduleServiceImpl extends ServiceImpl<VenueVisitScheduleMapper, VenueVisitSchedule> implements IVenueVisitScheduleService {
    @Autowired
    private VenueVisitScheduleMapper venueVisitScheduleMapper;

    @Override
    public PageBean page (Integer page,Integer pageSize , Integer venueId){

        Long count = venueVisitScheduleMapper.countAll(venueId);

        Integer start = (page - 1) * pageSize;
        List<VenueVisitSchedule> scheduleList = venueVisitScheduleMapper.page(start, pageSize,venueId);

        PageBean pageBean = new PageBean(count,scheduleList);
        return pageBean;
    }


    @Override
    public PageBean getSchedulesByDateWeek(Integer page,  Integer pageSize, Integer venueId , Integer dateWeek) {
        Long count =venueVisitScheduleMapper.countByDateWeek(venueId,dateWeek);
        Integer start = (page - 1) * pageSize;
        List<VenueVisitSchedule> scheduleList = venueVisitScheduleMapper.selectSchedulesByDateWeek(start,pageSize,venueId,dateWeek);
        PageBean pageBean = new PageBean(count,scheduleList);
        return pageBean;
    }

    @Override
    public List<VenueVisitSchedule> getAllSchedulesByDateWeek(Integer venueId , Integer dateWeek){
        List<VenueVisitSchedule> scheduleList = venueVisitScheduleMapper.selectAllSchedulesByDateWeek(venueId,dateWeek);
        return scheduleList;
    }

    @Override
    public void insertVenueVisitSchedule(VenueVisitSchedule venueVisitSchedule){
        venueVisitScheduleMapper.insertVenueVisitSchedule(venueVisitSchedule);
    }

    @Override
    public void deleteVenueVisitSchedule(Integer venueVisitScheduleId, Integer venueId){
        venueVisitScheduleMapper.deleteVenueVisitSchedule(venueVisitScheduleId , venueId);
    }

    @Override
    public List<VenueVisitSchedule> getVenueVisitScheduleByVenueId(Integer venueId,Integer week) {
        return venueVisitScheduleMapper.getVenueVisitScheduleByVenueId(venueId,week);
    }

    @Override
    public Integer getMCByVVSId(Integer venueVisitScheduleId) {
        return venueVisitScheduleMapper.getMCByVVSId(venueVisitScheduleId);
    }
}
