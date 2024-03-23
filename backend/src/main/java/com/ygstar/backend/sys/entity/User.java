package com.ygstar.backend.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@TableName("t_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userMail;

    private String phoneNumber;

    private String nickName;

    private String password;

    private String profilePicture;
    // false 0 表示封禁 true 1 表示未封禁
    private Boolean status;
    //已经拉黑的天数
    private Integer banDays;

    private Integer cancellation;



    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public void setCancellation(Integer cancellation) {
        this.cancellation = cancellation;
    }

    public Integer getCancellation() {
        return cancellation;
    }



    @Override
    public String toString() {
        return "User{" +
            "userMail=" + userMail +
            ", phoneNumber=" + phoneNumber +
            ", nickName=" + nickName +
            ", password=" + password +
            ", profilePicture=" + profilePicture +
            ", status=" + status +
                ", cancellation=" + cancellation +
                ", banDays=" + banDays +
        "}";
    }

    public Integer getBanDays() {
        return banDays;
    }

    public void setBanDays(Integer banDays) {
        this.banDays = banDays;
    }
}
