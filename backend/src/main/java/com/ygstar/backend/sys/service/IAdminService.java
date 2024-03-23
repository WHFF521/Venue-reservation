package com.ygstar.backend.sys.service;

import com.ygstar.backend.sys.dao.AdminVenueDAO;
import com.ygstar.backend.sys.entity.Admin;
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
public interface IAdminService extends IService<Admin> {

    Admin login(Admin admin);

//展示管理员信息
    PageBean getAllAdmin(Integer page, Integer pageSize);

//    新增管理员
    void newAdmin(String username,String password,Integer venue_id);
//    删除管理员（权限标记为2）
    void deleteAdmin(String username);
    void regainAdmin(String username);

}
