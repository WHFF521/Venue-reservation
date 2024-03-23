package com.ygstar.backend.sys.service.impl;

import com.ygstar.backend.sys.entity.Activity;
import com.ygstar.backend.sys.entity.VenueVisitSchedule;
import com.ygstar.backend.sys.mapper.ActivityMapper;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
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
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public PageBean getOngoingActivities(Integer page, Integer pageSize, Integer venueId) {
        Long count=activityMapper.countOngoingActivities(venueId);
        Integer start = (page - 1) * pageSize;
        List<Activity> activityList = activityMapper.getOngoingActivities(start, pageSize,venueId);
        PageBean pageBean = new PageBean(count,activityList);
        return pageBean;
    }

    @Override
    public PageBean getExpiredActivities(Integer page, Integer pageSize, Integer venueId) {
        Long count=activityMapper.countExpiredActivities(venueId);
        Integer start = (page - 1) * pageSize;
        List<Activity> activityList = activityMapper.getExpiredActivities(start, pageSize,venueId);
        PageBean pageBean = new PageBean(count,activityList);
        return pageBean;
    }

    @Override
    public PageBean selectOngoingActivitiesByActName(Integer page, Integer pageSize, Integer venueId, String actName){
        Long count=activityMapper.countOngoingActivitiesByActivityName(venueId, actName);
        Integer start = (page - 1) * pageSize;
        List<Activity> activityList = activityMapper.selectOngoingActivitiesByActName(start, pageSize , venueId, actName);
        PageBean pageBean = new PageBean(count,activityList);
        return pageBean;
    }

    @Override
    public PageBean selectExpiredActivitiesByActName(Integer page, Integer pageSize, Integer venueId, String actName){
        Long count=activityMapper.countExpiredActivitiesByActivityName(venueId, actName);
        Integer start = (page - 1) * pageSize;
        List<Activity> activityList = activityMapper.selectExpiredActivitiesByActName(start, pageSize , venueId, actName);
        PageBean pageBean = new PageBean(count,activityList);
        return pageBean;
    }

    @Override
    public void updateActivity(Activity activity) {
        activityMapper.updateById(activity);
    }

    @Override
    public void deleteAct(Integer actId){
        activityMapper.deleteAct(actId);
    }

    @Override
    public void createActivity(Activity activity) {
        activityMapper.insertActivity(activity);
    }

    @Override
    public Activity getActByActId(Integer actId){
        return activityMapper.getActByActId(actId);
    }

    @Override
    public List<Activity> ActivityList() {
        return activityMapper.ActivityList();
    }

    @Override
    public Activity ActivityInfo(Integer activityId) {
        return activityMapper.ActivityInfo(activityId);
    }

    @Override
    public int getMaximumCapacity(Integer activityId) {
        return activityMapper.getMaximumCapacity(activityId);
    }

    @Override
    public List<Activity> getTopEightActivity() {
        return activityMapper.getTopEightActivity();
    }

    @Override
    public List<Activity> searchActivityByName(String activityName) {
        return activityMapper.searchActivityByName(activityName);
    }

    @Override
    public List<Activity> getActivityByVenueId(Integer venueId) {
        return activityMapper.getActivityByVenueId(venueId);
    }

}
