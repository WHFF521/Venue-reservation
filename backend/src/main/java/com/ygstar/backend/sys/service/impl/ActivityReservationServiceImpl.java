package com.ygstar.backend.sys.service.impl;

import com.ygstar.backend.sys.dao.ActivityCancelTime;
import com.ygstar.backend.sys.dao.ActivityReservationDAO;
import com.ygstar.backend.sys.dao.SumAndCount;
import com.ygstar.backend.sys.dto.StatusAndScore;
import com.ygstar.backend.sys.dao.ActivityDetailDAO;
import com.ygstar.backend.sys.dao.VenueVisitDetailDAO;
import com.ygstar.backend.sys.dto.VenueActivityDTO;
import com.ygstar.backend.sys.entity.ActivityReservation;
import com.ygstar.backend.sys.entity.Banlog;
import com.ygstar.backend.sys.entity.User;
import com.ygstar.backend.sys.mapper.ActivityMapper;
import com.ygstar.backend.sys.mapper.ActivityReservationMapper;
import com.ygstar.backend.sys.mapper.BanlogMapper;
import com.ygstar.backend.sys.mapper.UserMapper;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IActivityReservationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class ActivityReservationServiceImpl extends ServiceImpl<ActivityReservationMapper, ActivityReservation> implements IActivityReservationService {
    @Autowired
    private ActivityReservationMapper activityReservationMapper;

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BanlogMapper banlogMapper;
    @Override
    public List<ActivityReservationDAO> getVenueReservations(int status, String userMail) {
        List<ActivityReservationDAO> result = new ArrayList<>();
        if(status == 1) {
            result = activityReservationMapper.getVenueReservations(status, userMail);
            result.addAll(activityReservationMapper.getVenueReservations(4, userMail));
        }else {
            result = activityReservationMapper.getVenueReservations(status,userMail);
        }
        return result;
    }

    @Override
    public void scoreActivity(int orderNumber, int activityID, double score) {
        //更新用户评分
        activityReservationMapper.updateActScore(orderNumber, score);
        //获取所有已经评分人数和分数总和
        SumAndCount sumAndCount = activityReservationMapper.getActScoreSumAndCount(activityID);
        //计算均分
        double actScore = sumAndCount.getSum() / (double) sumAndCount.getCount();
        //更新对应活动的分数
        activityMapper.updateActRating(activityID, BigDecimal.valueOf(actScore));
    }

    @Override
    public StatusAndScore getStatusAndScore(int orderNumber) {
        return activityReservationMapper.getStatusAndScore(orderNumber);
    }

    @Override
    public Boolean isDefaultCancel(int activityId) {
        ActivityCancelTime activityCancelTime = activityMapper.getCancelSection(activityId);
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        // 计算活动的具体时间范围
        LocalDateTime activityStartTime = LocalDateTime.of(activityCancelTime.getActDate(), activityCancelTime.getStartTime());
        LocalDateTime activityEndTime = LocalDateTime.of(activityCancelTime.getActDate(), activityCancelTime.getEndTime());
        // 调整时间
        LocalDateTime adjustedStartTime = activityStartTime.minusHours(activityCancelTime.getActCancelTime());
//        System.out.println("开始："+ adjustedStartTime);
//        System.out.println("结束："+ activityEndTime);
        return currentTime.isAfter(adjustedStartTime) && currentTime.isBefore(activityEndTime);
    }

    @Override
    public Boolean cancelNotDefault(int orderNumber) {
        if(activityReservationMapper.updateToCancel(orderNumber)==1){
            return true;
        }
        else return false;
    }

    @Override
    public int cancelDefault(int orderNumber, String userMail) {
        if(activityReservationMapper.updateToCancel(orderNumber)==1){
            User user = userMapper.selectByUserMail(userMail);
            int cancellation = user.getCancellation();
            cancellation++;
            if(cancellation>=3){
                userMapper.updateBanDays(userMail);
                Banlog banlog = new Banlog();
                banlog.setUserMail(userMail);
                banlog.setBanTime(LocalDateTime.now());
                banlogMapper.insert(banlog);
                return 1;
            }
            else{
                userMapper.addCancellation(cancellation,userMail);
                return 2;
            }
        }
        return 3;
    }

    @Override
    public Boolean visitActivity(int orderNumber){
        if(activityReservationMapper.updateToVisit(orderNumber)==1){
            return true;
        }
        else return false;
    }

    @Override
    public int getReservationCount(int activityId) {
        return activityReservationMapper.getReservationCount(activityId);
    }

    @Override
    public void insertReservation(int activityId, String userMail, String idCardNumber) {
        activityReservationMapper.insertReservation(activityId, userMail, idCardNumber);
    }

    @Override
    public int hasSameRecord(String idCardNumber) {
        return activityReservationMapper.hasSameRecord(idCardNumber);
    }
    //首页图表获取数据
    public List<VenueActivityDTO> getActivityInfoByVenueId(Integer venueId) {
        return activityReservationMapper.getActivityInfoByVenueId(venueId);
    }

    //
    @Override
    public PageBean getActivityDetails(Integer venueId, Integer page, Integer pageSize) {
        Integer startIndex = (page - 1) * pageSize;
        List<ActivityDetailDAO> activityDetailsList = activityReservationMapper.getActivityDetails(startIndex, pageSize, venueId);
        Long count = activityReservationMapper.countGetActivityDetails(venueId);
        PageBean pageBean = new PageBean(count,activityDetailsList);
        return pageBean;
    }
}
