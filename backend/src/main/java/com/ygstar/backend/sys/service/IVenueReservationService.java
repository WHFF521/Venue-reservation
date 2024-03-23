package com.ygstar.backend.sys.service;

import com.ygstar.backend.sys.dao.VenueProfileDAO;
import com.ygstar.backend.sys.dto.StatusAndScore;
import com.ygstar.backend.sys.dao.VenueReservationDAO;
import com.ygstar.backend.sys.dao.VenueVisitDetailDAO;
import com.ygstar.backend.sys.dto.VenueReservationDTO;
import com.ygstar.backend.sys.entity.VenueReservation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygstar.backend.sys.pojo.PageBean;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
public interface IVenueReservationService extends IService<VenueReservation> {
    int getVRCByVeIdABTAVeVSI(Integer venueId, LocalDateTime bookTime, Integer venueVisitScheduleId);
    List<VenueReservationDTO> getVenueReservations(int status, String userMail);
    void insertVenueReservation(VenueReservation venueReservation);
    int hasSameRecord(VenueReservation venueReservation);
    List<Integer> countTotalVisitorsByWeek(Integer venueId);
    PageBean getVenueVisitDetails(Integer venueId,Integer page, Integer pageSize);
    List<VenueProfileDAO> getVenueProfile(Integer venueId);

    void scoreVenue(int orderNumber, int venueID, double score);

    StatusAndScore getStatusAndScore(int orderNumber);
    Boolean isDefaultCancel(int orderNumber);
    int cancelDefault(int orderNumber,String userMail);
    Boolean cancelNotDefault(int orderNumber);

    Boolean visitVenue(int orderNumber);

}
