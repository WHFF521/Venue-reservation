package com.ygstar.backend.sys.service;

import com.ygstar.backend.sys.dao.ActivityReservationDAO;
import com.ygstar.backend.sys.dto.StatusAndScore;
import com.ygstar.backend.sys.dao.ActivityDetailDAO;
import com.ygstar.backend.sys.dto.VenueActivityDTO;
import com.ygstar.backend.sys.entity.ActivityReservation;
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
public interface IActivityReservationService extends IService<ActivityReservation> {
    List<ActivityReservationDAO> getVenueReservations(int status, String userMail);
    //查询活动报名的人数
    int getReservationCount(int activityId);
    void insertReservation(int activityId, String userMail, String idCardNumber);

    int hasSameRecord(String idCardNumber);
    //首页图表获取数据
    List<VenueActivityDTO> getActivityInfoByVenueId(Integer venueId);

    PageBean getActivityDetails(Integer venueId, Integer page, Integer pageSize);
    void scoreActivity(int orderNumber, int activityID, double score);

    StatusAndScore getStatusAndScore(int orderNumber);
    Boolean isDefaultCancel(int activityId);
    Boolean cancelNotDefault(int orderNumber);

    int cancelDefault(int orderNumber, String userMail);

    Boolean visitActivity(int orderNumber);
}

