package com.ygstar.backend.sys.service;

import com.ygstar.backend.sys.entity.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygstar.backend.sys.pojo.PageBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
public interface IActivityService extends IService<Activity> {
    PageBean getOngoingActivities(Integer page, Integer pageSize, Integer venueId);

    PageBean getExpiredActivities(Integer page, Integer pageSize, Integer venueId);

    PageBean selectOngoingActivitiesByActName(Integer page, Integer pageSize, Integer venueId ,String actName);

    PageBean selectExpiredActivitiesByActName(Integer page, Integer pageSize, Integer venueId ,String actName);

    void updateActivity(Activity activity);

    void deleteAct(Integer actId);

    Activity getActByActId(Integer actId);

    void createActivity(Activity activity);
    List<Activity> ActivityList();
    Activity ActivityInfo(Integer activityId);

    int getMaximumCapacity(Integer activityId);

    List<Activity> getTopEightActivity();
    List<Activity> searchActivityByName(@Param("activityName") String activityName);
    List<Activity> getActivityByVenueId(@Param("venueId") Integer venueId);
}

