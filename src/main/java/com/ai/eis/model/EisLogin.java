package com.ai.eis.model;

import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EisLogin {
    private Integer userid;

    private String account;

    private String password;

    private Date expiredTime;

    private Date lastLoginTime;

    private String loginIp;

    private Integer status;

    private EisUser user;


    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public EisUser getUser() {
        return user;
    }

    public void setUser(EisUser user) {
        this.user = user;
    }
}