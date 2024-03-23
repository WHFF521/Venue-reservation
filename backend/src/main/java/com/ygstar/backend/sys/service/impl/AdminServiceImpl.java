package com.ygstar.backend.sys.service.impl;

import com.ygstar.backend.sys.dao.AdminVenueDAO;
import com.ygstar.backend.sys.entity.Admin;
import com.ygstar.backend.sys.mapper.AdminMapper;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IAdminService;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(Admin admin) {
        Admin login = adminMapper.login(admin);
        if(login != null){
            return login;
        }throw new RuntimeException("用户名或密码错误");
    }











//    新增管理员
//    void:小写
//    展示管理员信息
    @Override
    public PageBean getAllAdmin(Integer page, Integer pageSize) {
        int startIndex = (page - 1) * pageSize;
        List<AdminVenueDAO>adminList = adminMapper.getAllAdmin(startIndex, pageSize);
        Long count = adminMapper.countGetAllAdmin();
        PageBean pageBean = new PageBean(count,adminList);
        return pageBean;
    }
//    新增管理员
//    void:小写

    @Override
    public  void newAdmin(String username,String password,Integer venue_id){
        Integer permission = 0;

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setUserPassword(password);
        admin.setVenueId(venue_id);
        admin.setPermission(permission);

        adminMapper.insertAdmin(admin);
    }


//    删除管理员
    @Override
    public void deleteAdmin(String username){
        adminMapper.updatePermission(username,2);
    }
//    恢复权限
    @Override
    public void regainAdmin(String username){
        adminMapper.updatePermission(username,0);
    }

}
