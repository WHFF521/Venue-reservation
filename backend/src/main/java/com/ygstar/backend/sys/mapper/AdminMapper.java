package com.ygstar.backend.sys.mapper;

import com.ygstar.backend.sys.dao.AdminVenueDAO;
import com.ygstar.backend.sys.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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
public interface AdminMapper extends BaseMapper<Admin> {

    Admin login(@Param("admin") Admin admin);

    //展示管理员表
    List<AdminVenueDAO> getAllAdmin(@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
    Long countGetAllAdmin();

    //新建管理员
    @Insert("INSERT INTO t_admin (username, user_password, venue_id, permission) " +
            "VALUES (#{username}, #{userPassword}, #{venueId}, #{permission})")
    void insertAdmin(Admin admin);

    @Update("UPDATE t_admin SET permission = #{frozenPermission} WHERE username = #{username}")
    void updatePermission(@Param("username") String username, @Param("frozenPermission") Integer frozenPermission);


}
