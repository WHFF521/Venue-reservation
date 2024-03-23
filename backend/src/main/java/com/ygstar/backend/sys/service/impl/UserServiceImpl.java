package com.ygstar.backend.sys.service.impl;

import com.ygstar.backend.sys.dao.BanlogUserDAO;
import com.ygstar.backend.sys.dto.UserDTO;
import com.ygstar.backend.sys.entity.User;
import com.ygstar.backend.sys.mapper.UserMapper;
import com.ygstar.backend.sys.pojo.PageBean;
import com.ygstar.backend.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User login(User user) {
        User login = userMapper.login(user);
        if(login != null){
            return login;
        }throw new RuntimeException("用户名或密码错误");

    }

    @Override
    public boolean updateAvatar(String newPicture, String userMail) {
        int result = userMapper.updateProfilePicture(newPicture,userMail);
        //如果更新的数据数等于1条说明更新成功
        if(result == 1) return true;
        else return false;
    }
    @Override
    public boolean register(UserDTO userDTO){
        User user = new User();
        user.setUserMail(userDTO.getUserMail());
        user.setPassword(userDTO.getPassword());
        user.setNickName("微信用户");
        user.setStatus(true);
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setProfilePicture("");
        user.setBanDays(0);
        user.setCancellation(0);
        if(userMapper.insertUser(user)==1)return true;
        else return false;
    }

    @Override
    public void updateUserName(String newUserName, String userMail) {
        userMapper.updateUserName(newUserName,userMail);
    }

    @Override
    public User getUserInfo(String userMail) {
        return userMapper.getUserInfo(userMail);
    }

    @Override
    public void updateUserPhoneNumber(String newPhoneNumber, String userMail) {
        userMapper.updateUserPhoneNumber(newPhoneNumber,userMail);
    }

    @Override
    public PageBean getBanedUser(Integer page, Integer pageSize){
        Long count = userMapper.countGetBanedUser();
        Integer startIndex = (page - 1) * pageSize;
        List<User>banedUserList = userMapper.getBanedUser(startIndex,pageSize);
        PageBean pagebean = new PageBean(count,banedUserList);
        return pagebean;
    }
    @Override
    public void updateUserStatus(String userMail){
        userMapper.updateUserStatus(userMail);
    }
}
