package com.ygstar.backend.sys.mapper;
import com.ygstar.backend.sys.dao.VenueVisitDetailDAO;
import com.ygstar.backend.sys.dto.VenueActivityDTO;
import com.ygstar.backend.sys.dao.ActivityReservationDAO;
import com.ygstar.backend.sys.dao.SumAndCount;
import com.ygstar.backend.sys.dto.StatusAndScore;
import com.ygstar.backend.sys.entity.ActivityReservation;
import com.ygstar.backend.sys.dao.ActivityDetailDAO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface ActivityReservationMapper extends BaseMapper<ActivityReservation> {
    List<ActivityReservationDAO> getVenueReservations(@Param("status") int status, @Param("userMail") String userMail);
    int updateActScore(@Param("orderNumber") int orderNumber,
                       @Param("score") Double score);
    SumAndCount getActScoreSumAndCount(@Param("activityId") int activityId);

    StatusAndScore getStatusAndScore(@Param("orderNumber") int orderNumber);

    int updateToCancel(@Param("orderNumber") int orderNumber);
    int updateToVisit(@Param("orderNumber") int orderNumber);
    //查询活动报名的人数
    int getReservationCount(@Param("activityId") int activityId);

    //插入预约数据
    void insertReservation(@Param("activityId") int activityId, @Param("userMail") String userMail, @Param("idCardNumber") String idCardNumber);

    //根据身份证号查询是否有相同记录
    int hasSameRecord(@Param("idCardNumber") String idCardNumber);


//    首页活动表单数据获取
    List<VenueActivityDTO> getActivityInfoByVenueId(@Param("venueId") Integer venueId);

//  详情页获取活动表格数据
    List<ActivityDetailDAO> getActivityDetails(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize, @Param("venueId") Integer venueId);

    Long countGetActivityDetails(@Param("venueId")Integer venueId);

    List<String> activityRemind();
}
