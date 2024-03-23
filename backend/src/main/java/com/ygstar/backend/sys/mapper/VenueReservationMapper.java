package com.ygstar.backend.sys.mapper;

import com.ygstar.backend.sys.dao.VenueProfileDAO;
import com.ygstar.backend.sys.dao.SumAndCount;
import com.ygstar.backend.sys.dao.VenueCancelTime;
import com.ygstar.backend.sys.dao.VenueReservationDAO;
import com.ygstar.backend.sys.dto.StatusAndScore;
import com.ygstar.backend.sys.dao.VenueVisitDetailDAO;
import com.ygstar.backend.sys.entity.VenueReservation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public interface VenueReservationMapper extends BaseMapper<VenueReservation> {

    //查询venueId，bookTime，venueVisitScheduleId相同的数据个数
//    int getVRCByVeIdABTAVeVSI(@Param("venueId") Integer venueId, @Param("bookTime") LocalDateTime bookTime, @Param("venueVisitScheduleId") Integer venueVisitScheduleId);
    List<VenueReservationDAO> getVenueReservations(@Param("status") int status, @Param("userMail") String userMail);
    int getVRCByVeIdABTAVeVSI(@Param("venueId") Integer venueId,
                              @Param("bookTime") LocalDateTime bookTime,
                              @Param("venueVisitScheduleId") Integer venueVisitScheduleId);
    //插入VenueResrvation类，并返回成功与否的标志
    void insertVenueReservation(@Param("ac") VenueReservation venueReservation);
    // 查询是否有相同的记录
    int hasSameRecord(@Param("rd")VenueReservation venueReservation);
    Integer countByWeek(@Param("venueId")Integer venueId,@Param("weekDay")Integer weekDay);

    List<VenueVisitDetailDAO> getVenueVisitDetails(@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize,@Param("venueId") Integer venueId);
    Long countGetVenueVisitDetails(@Param("venueId")Integer venueId);

    List<VenueProfileDAO> getVenueProfile(@Param("venueId")Integer venueId);
    int updateVenueScore(@Param("orderNumber") int ordNumber,
                            @Param("score") Double score);
    SumAndCount getVenScoreSumAndCount(@Param("venueId") int venueId);
    StatusAndScore getStatusAndScore(@Param("orderNumber")int orderNumber);
    VenueCancelTime getCancelSection(@Param("orderNumber")int orderNumber);
    int updateToCancel(@Param("orderNumber") int orderNumber);

    int updateToVisit(@Param("orderNumber") int orderNumber);

    List<VenueVisitDetailDAO> getVenueVisitDetails(@Param("venueId") Integer venueId);

    List<String> venueRemind();
}
