package com.ygstar.backend.sys.mapper;

import com.ygstar.backend.sys.dao.ActivityCancelTime;
import com.ygstar.backend.sys.entity.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import org.apache.ibatis.annotations.Param;


import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    Long countOngoingActivities (Integer venueId);

    Long countExpiredActivities(Integer venueId);

    Long countOngoingActivitiesByActivityName (@Param("venueId") Integer venueId , @Param("actName") String actName);

    Long countExpiredActivitiesByActivityName(@Param("venueId") Integer venueId , @Param("actName") String actName);

    List<Activity> getOngoingActivities(@Param("start") Integer start, @Param("pageSize") Integer pageSize ,@Param("venueId") Integer venueId);

    List<Activity> getExpiredActivities(@Param("start") Integer start, @Param("pageSize") Integer pageSize ,@Param("venueId") Integer venueId);

    List<Activity> selectOngoingActivitiesByActName(@Param("start") Integer start, @Param("pageSize") Integer pageSize ,@Param("venueId") Integer venueId, @Param("actName") String actName);

    List<Activity> selectExpiredActivitiesByActName(@Param("start") Integer start, @Param("pageSize") Integer pageSize ,@Param("venueId") Integer venueId, @Param("actName") String actName);
    int updateById(Activity activity);

    void deleteAct(@Param("actId") Integer actId);

    void insertActivity(@Param("insertActivity") Activity activity);

    Activity getActByActId(@Param("actId") Integer actId);



    List<Activity> ActivityList();

    void updateActRating(@Param("activityId") int activityId, @Param("newRating") BigDecimal newRating);

    ActivityCancelTime getCancelSection(@Param("activityId") int activityId);
    Activity ActivityInfo(Integer activityId);
    //获取活动最多预约人数
    int getMaximumCapacity(@Param("actId") Integer activityId);

    //获取评分最高的八个活动
    List<Activity> getTopEightActivity();

    //根据名称模糊搜索活动
    List<Activity> searchActivityByName(@Param("actName") String activityName);

    //查找某个场馆的所有活动
    List<Activity> getActivityByVenueId(@Param("venueId") Integer venueId);
}
