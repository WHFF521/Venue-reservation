package com.ygstar.backend.sys.service.impl;

import com.ygstar.backend.sys.dao.BanlogUserDAO;
import com.ygstar.backend.sys.entity.Banlog;
import com.ygstar.backend.sys.mapper.BanlogMapper;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IBanlogService;
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
public class BanlogServiceImpl extends ServiceImpl<BanlogMapper, Banlog> implements IBanlogService {
    @Autowired
    private BanlogMapper banlogMapper;
    @Override
    public PageBean getAllBanlog(Integer page, Integer pageSize){
        Long count = banlogMapper.countGetAllBanlog();
        Integer startIndex = (page - 1) * pageSize;
        List<BanlogUserDAO>banlogList = banlogMapper.getAllBanlog(startIndex,pageSize);
        PageBean pageBean = new PageBean(count,banlogList);
        return pageBean;
    }
}
