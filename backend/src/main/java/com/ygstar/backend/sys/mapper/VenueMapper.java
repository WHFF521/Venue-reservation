package com.ygstar.backend.sys.mapper;


import com.ygstar.backend.sys.dao.VenueAdminDAO;
import com.ygstar.backend.sys.entity.Venue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
public interface VenueMapper extends BaseMapper<Venue> {
    void insertVenue(Venue venue);
    Venue selectByVenueId(int Id);
    int updateById(@Param("venue") Venue venue);

    void newVenue(@Param("venue")Venue newVenue);

    //    场馆表格展示
    List<VenueAdminDAO> getBasicVenues(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize);
    Long countGetBasicVenues();
    //    场馆更新可复用参观管理的修改代码？

    //获取所有场馆数据
    List<Venue> VenueList();

    //根据id获取某个场馆的信息
    Venue VenueInfo(@Param("venueId") Integer venueId);

    int updateVenuePicture(@Param("venueId") int venueId, @Param("picture") String picture);

    //获取评分最高的八个场馆
    List<Venue> getTopEightVenue();

    //根据名称模糊搜索场馆
    List<Venue> searchVenueByName(@Param("venueName") String venueName);

    void updateDeleteFlag(@Param("venueId") int venueId, @Param("deleteFlag") Boolean deleteFlag);

    //获取openDays
    String getOpenDays(@Param("venueId")Integer venueId);
    void updateDeleteFlag(@Param("venueId") Integer venueId, @Param("deleteFlag") Boolean deleteFlag);

    void updateVenueRating(@Param("venueId") int venueId, @Param("newRating") BigDecimal newRating);
}
