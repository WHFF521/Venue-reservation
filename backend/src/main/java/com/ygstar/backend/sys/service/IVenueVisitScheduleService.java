package com.ygstar.backend.sys.service;

import com.ygstar.backend.sys.entity.VenueVisitSchedule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygstar.backend.sys.pojo.PageBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
public interface IVenueVisitScheduleService extends IService<VenueVisitSchedule> {
    PageBean page(Integer page , Integer pageSize ,Integer venueId);

    PageBean getSchedulesByDateWeek(Integer page,  Integer pageSize, Integer venueId , Integer dateWeek);

    List<VenueVisitSchedule> getAllSchedulesByDateWeek(Integer venueId , Integer dateWeek);

    void insertVenueVisitSchedule(VenueVisitSchedule venueVisitSchedule);

    void deleteVenueVisitSchedule(Integer venueVisitScheduleId, Integer venueId);

    List<VenueVisitSchedule> getVenueVisitScheduleByVenueId(Integer venueId,Integer week);
    Integer getMCByVVSId(Integer venueVisitScheduleId);
}
