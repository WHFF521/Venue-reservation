package com.ygstar.backend.sys.mapper;

import com.ygstar.backend.sys.entity.VenueVisitSchedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface VenueVisitScheduleMapper extends BaseMapper<VenueVisitSchedule> {

    @Select("select count(*) from t_venue_visit_schedule WHERE venue_id=#{venueId}")
    Long countAll(Integer venueId);

    Long countByDateWeek( @Param("venueId") Integer venueId, @Param("dateWeek") Integer dateWeek);

    List<VenueVisitSchedule> page (@Param("start") Integer start, @Param("pageSize") Integer pageSize ,@Param("venueId") Integer venueId);
    List<VenueVisitSchedule> selectSchedulesByDateWeek( @Param("start") Integer start, @Param("pageSize") Integer pageSize , @Param("venueId") Integer venueId , @Param("dateWeek") Integer dateWeek);

    List<VenueVisitSchedule> selectAllSchedulesByDateWeek(@Param("venueId") Integer venueId , @Param("dateWeek") Integer dateWeek);
    void insertVenueVisitSchedule(@Param("venueVisitSchedule") VenueVisitSchedule venueVisitSchedule);

    void deleteVenueVisitSchedule(@Param("venueVisitScheduleId") Integer venueVisitScheduleId ,@Param("venueId") Integer venueId);

    List<VenueVisitSchedule> getVenueVisitScheduleByVenueId(@Param("venueId")Integer venueId,@Param("week")Integer week);
    //根据venueVisitScheduleId查询maximumCapacity
    Integer getMCByVVSId(@Param("venueVisitScheduleId")Integer venueVisitScheduleId);
}
