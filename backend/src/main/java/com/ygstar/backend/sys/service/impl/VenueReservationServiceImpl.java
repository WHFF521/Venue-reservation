package com.ygstar.backend.sys.service.impl;

import com.ygstar.backend.sys.dao.*;
import com.ygstar.backend.sys.dto.StatusAndScore;
import com.ygstar.backend.sys.dto.VenueReservationDTO;
import com.ygstar.backend.sys.entity.Banlog;
import com.ygstar.backend.sys.entity.User;
import com.ygstar.backend.sys.entity.VenueReservation;
import com.ygstar.backend.sys.mapper.BanlogMapper;
import com.ygstar.backend.sys.mapper.UserMapper;
import com.ygstar.backend.sys.mapper.VenueMapper;
import com.ygstar.backend.sys.mapper.VenueReservationMapper;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IVenueReservationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@Service
public class VenueReservationServiceImpl extends ServiceImpl<VenueReservationMapper, VenueReservation> implements IVenueReservationService {

    @Autowired
    private VenueReservationMapper venueReservationMapper;
    @Autowired
    private VenueMapper venueMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BanlogMapper banlogMapper;

    @Override
    public List<Integer> countTotalVisitorsByWeek(Integer venueId) {
        List<Integer> result = new ArrayList<>();
        for (int weekDay = 1; weekDay <= 7; weekDay++) {
            // 查询每天的总人数
            Integer totalVisitors = venueReservationMapper.countByWeek(venueId,weekDay);
            result.add(totalVisitors);
        }
        return result;
    }
    @Override
    public int getVRCByVeIdABTAVeVSI(Integer venueId, LocalDateTime bookTime, Integer venueVisitScheduleId) {
        return venueReservationMapper.getVRCByVeIdABTAVeVSI(venueId,bookTime,venueVisitScheduleId);
    }

    @Override
    public List<VenueReservationDTO> getVenueReservations(int status, String userMail) {
        List<VenueReservationDAO> venueReservationDAOs = venueReservationMapper.getVenueReservations(status, userMail);
        if(status == 1) {
            venueReservationDAOs.addAll(venueReservationMapper.getVenueReservations(4, userMail));
        }
        List<VenueReservationDTO> venueReservationDTOs = new ArrayList<>();
        for (VenueReservationDAO dao : venueReservationDAOs) {
            VenueReservationDTO dto = new VenueReservationDTO();
            dto.setOrderNumber(dao.getOrderNumber());
            dto.setIdCardNumber(dao.getIdCardNumber());
            dto.setVenueId(dao.getVenueId());
            dto.setVisitorName(dao.getVisitorName());
            dto.setVenueName(dao.getVenueName());
            dto.setVenueTime(formatVenueTime(dao.getBookTime(), dao.getStartTime(), dao.getEndTime()));
            venueReservationDTOs.add(dto);
        }
        return venueReservationDTOs;
    }


    private String formatVenueTime(LocalDateTime bookTime, LocalTime startTime, LocalTime endTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String datePart = bookTime.format(dateFormatter);
        String timePart = startTime.format(timeFormatter) + "-" + endTime.format(timeFormatter);

        return datePart + "(" + timePart + ")";
    }

//    @Resource
//    private RocketMQTemplate rocketMQTemplate;
    @Override
    public void insertVenueReservation(VenueReservation venueReservation) {

//        rocketMQTemplate.asyncSend("venue-reservation", MessageBuilder.withPayload(venueReservation).build(), new SendCallback() {
//            @Override
//            public void onSuccess(SendResult sendResult) {
//                System.out.println("发送成功");
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//                System.out.println("发送失败");
//            }
//        });
        venueReservationMapper.insertVenueReservation(venueReservation);
    }

    @Override
    public int hasSameRecord(VenueReservation venueReservation) {
        return venueReservationMapper.hasSameRecord(venueReservation);
    }
    @Override
    public PageBean getVenueVisitDetails(Integer venueId,Integer page, Integer pageSize){
        Integer startIndex = (page - 1) * pageSize;
        List<VenueVisitDetailDAO>venueVisitDetailsList=venueReservationMapper.getVenueVisitDetails(startIndex, pageSize,venueId);
        Long count = venueReservationMapper.countGetVenueVisitDetails(venueId);
        PageBean pageBean = new PageBean(count,venueVisitDetailsList);
        return pageBean;
    };

    @Override
    public List<VenueProfileDAO> getVenueProfile(Integer venueId){
        return  venueReservationMapper.getVenueProfile(venueId);
    }

    @Override
    public void scoreVenue(int orderNumber, int venueID, double score){
        //更新用户评分
        venueReservationMapper.updateVenueScore(orderNumber,score);
        //获取所有已经评分人数和分数总和
        SumAndCount sumAndCount = venueReservationMapper.getVenScoreSumAndCount(venueID);
        //计算均分
        double venScore = sumAndCount.getSum() / (double) sumAndCount.getCount();
        //更新对应场馆的分数
        venueMapper.updateVenueRating(venueID, BigDecimal.valueOf(venScore));
    }
    @Override
    public StatusAndScore getStatusAndScore(int orderNumber) {
        return venueReservationMapper.getStatusAndScore(orderNumber);
    }

    @Override
    public Boolean isDefaultCancel(int orderNumber){
        VenueCancelTime venueCancelTime = venueReservationMapper.getCancelSection(orderNumber);
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDate reserveDate = venueCancelTime.getVenueDate().toLocalDate();
        //计算具体时间范围
        LocalDateTime StartTime = LocalDateTime.of(reserveDate,venueCancelTime.getStartTime());
        LocalDateTime EndTime = LocalDateTime.of(reserveDate,venueCancelTime.getEndTime());
        // 调整时间
        LocalDateTime adjustedStartTime = StartTime.minusHours(venueCancelTime.getVenCancelTime());
        System.out.println("开始："+ adjustedStartTime);
        System.out.println("结束："+ EndTime);
        return currentTime.isAfter(adjustedStartTime) && currentTime.isBefore(EndTime);
    }

    @Override
    public int cancelDefault(int orderNumber, String userMail){
        if(venueReservationMapper.updateToCancel(orderNumber)==1){
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
            else {
                userMapper.addCancellation(cancellation,userMail);
                return 2;
            }
        }
        return 3;
    }
    @Override
    public Boolean cancelNotDefault(int orderNumber){
        if(venueReservationMapper.updateToCancel(orderNumber)==1){
            return true;
        }
        else return false;
    }

    @Override
    public Boolean visitVenue(int orderNumber){
        if(venueReservationMapper.updateToVisit(orderNumber)==1){
            return true;
        }
        else return false;
    }
}
