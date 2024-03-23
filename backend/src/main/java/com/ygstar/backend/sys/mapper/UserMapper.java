package com.ygstar.backend.sys.mapper;

import com.ygstar.backend.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface UserMapper extends BaseMapper<User> {
    User login(User user);
    int updateProfilePicture(@Param("newPicture")String newPicture, @Param("userMail")String userMail);
    int insertUser(User user);

    //更新用户名
    void updateUserName(@Param("newUserName")String newUserName, @Param("userMail")String userMail);

    //获取用户信息
    User getUserInfo(@Param("userMail") String userMail);

    //更新用户手机号
    void updateUserPhoneNumber(@Param("newPhoneNumber")String newPhoneNumber, @Param("userMail")String userMail);
    List<User> getBanedUser(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize);
    Long countGetBanedUser();
    User selectByUserMail(@Param("userMail") String userMail);
    int updateBanDays(@Param("userMail") String userMail);
    int addCancellation(@Param("newCancellation") int newCancellation,@Param("userMail")String userMail);
    void updateUserStatus(@Param("userMail")String userMail);
}
