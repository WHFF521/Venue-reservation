package com.ygstar.backend.sys.service;

import com.ygstar.backend.sys.dto.UserDTO;
import com.ygstar.backend.sys.entity.User;
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
public interface IUserService extends IService<User> {
    User login(User user);

    boolean updateAvatar(String newPicture, String userMail);

    boolean register(UserDTO userDTO);

    //更新用户名
    void updateUserName(String newUserName, String userMail);

    //获取用户信息
    User getUserInfo(String userMail);

    //更新用户手机号
    void updateUserPhoneNumber(String newPhoneNumber, String userMail);

    PageBean getBanedUser(Integer page, Integer pageSize);
    void updateUserStatus(String userMail);
}
