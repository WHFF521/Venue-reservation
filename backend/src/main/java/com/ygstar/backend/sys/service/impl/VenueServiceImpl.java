package com.ygstar.backend.sys.service.impl;

import com.ygstar.backend.sys.dao.BanlogUserDAO;
import com.ygstar.backend.sys.dao.VenueAdminDAO;
import com.ygstar.backend.sys.entity.Venue;
import com.ygstar.backend.sys.mapper.VenueMapper;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IVenueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class VenueServiceImpl extends ServiceImpl<VenueMapper, Venue> implements IVenueService {
    @Autowired
    private VenueMapper venueMapper;

    public Venue getVenueById(Integer id) {
        return venueMapper.selectById(id);
    }

    public void updateVenue(Venue updatedVenue) {
//        Venue existingVenue = venueMapper.selectById(id);
//        if (existingVenue != null) {
//            updatedVenue.setVenueId(id);
//            venueMapper.updateById(updatedVenue);
//            return updatedVenue;
//        }
//        return null;
        venueMapper.updateById(updatedVenue);
    }

    @Override
    public void newVenue(Venue newVenue){
        venueMapper.newVenue(newVenue);
    }

    @Override
    public PageBean getBasicVenues(Integer page, Integer pageSize) {
        Long count = venueMapper.countGetBasicVenues();
        Integer startIndex = (page - 1) * pageSize;
        List<VenueAdminDAO> venueList = venueMapper.getBasicVenues(startIndex, pageSize);
        PageBean pageBean = new PageBean(count, venueList);
        return pageBean;
    }
    @Override
    public List<Venue> getTopEightVenue() {
        return venueMapper.getTopEightVenue();
    }

    @Override
    public List<Venue> searchVenueByName(String venueName) {
        return venueMapper.searchVenueByName(venueName);
    }

    @Override
    public List<Venue> VenueList() {
        return venueMapper.VenueList();
    }

    @Override
    public Venue VenueInfo(Integer venueId) {
        return venueMapper.VenueInfo(venueId);
    }

    @Override
    public boolean updatePicture(int venueId, String newPicture) {
        int result = venueMapper.updateVenuePicture(venueId,newPicture);
        //如果更新的数据数等于1条说明更新成功
        if(result == 1) return true;
        else return false;
    }

    @Override
    public void deleteVenue(Integer venueId){
        Boolean deleteFlag = true;
        venueMapper.updateDeleteFlag(venueId,deleteFlag);
    }

    @Override
    public String getOpenDays(Integer venueId) {
        return venueMapper.getOpenDays(venueId);
    }

    @Override
    public void regainVenue(Integer venueId){
        Boolean deleteFlag = false;
        venueMapper.updateDeleteFlag(venueId,deleteFlag);
    };


}
