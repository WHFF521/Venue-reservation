package com.ygstar.backend.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@TableName("t_banlog")
@ApiModel(value = "Banlog对象", description = "")
public class Banlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "banlog_id", type = IdType.AUTO)
    private Integer banlogId;

    private String userMail;

    private LocalDateTime banTime;

    public Integer getBanlogId() {
        return banlogId;
    }

    public void setBanlogId(Integer banlogId) {
        this.banlogId = banlogId;
    }
    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
    public LocalDateTime getBanTime() {
        return banTime;
    }

    public void setBanTime(LocalDateTime banTime) {
        this.banTime = banTime;
    }

    @Override
    public String toString() {
        return "Banlog{" +
            "banlogId=" + banlogId +
            ", userMail=" + userMail +
            ", banTime=" + banTime +
        "}";
    }
}
