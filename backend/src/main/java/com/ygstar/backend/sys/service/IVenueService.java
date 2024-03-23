package com.ygstar.backend.sys.service;

import com.ygstar.backend.sys.dao.VenueAdminDAO;
import com.ygstar.backend.sys.entity.Venue;
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
public interface IVenueService extends IService<Venue> {
    public Venue getVenueById(Integer id) ;
    void updateVenue(Venue updatedVenue);

    void newVenue(Venue newVenue);
    List<Venue> VenueList();

    Venue VenueInfo(Integer venueId);

    boolean updatePicture(int venueId, String newPicture);



    List<Venue> getTopEightVenue();

    List<Venue> searchVenueByName(@Param("venueName") String venueName);
    PageBean getBasicVenues(Integer page, Integer pageSize);

    void deleteVenue(Integer venueId);

    //获取openDays
    String getOpenDays(Integer venueId);
    void regainVenue(Integer venueId);
}
