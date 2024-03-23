package com.ygstar.backend.sys.service;

import com.ygstar.backend.sys.dao.BanlogUserDAO;
import com.ygstar.backend.sys.entity.Banlog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygstar.backend.sys.pojo.PageBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
public interface IBanlogService extends IService<Banlog> {
    PageBean getAllBanlog(Integer page, Integer pageSize);
}
