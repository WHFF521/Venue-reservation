package com.ygstar.backend.sys.dao;

import java.time.LocalDateTime;

public class BanlogUserDAO {
    private Integer banlogId;

    private LocalDateTime banTime;

    private String nickName;

    public Integer getBanlogId() {
        return banlogId;
    }

    public void setBanlogId(Integer banlogId) {
        this.banlogId = banlogId;
    }

    public LocalDateTime getBanTime() {
        return banTime;
    }

    public void setBanTime(LocalDateTime banTime) {
        this.banTime = banTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
