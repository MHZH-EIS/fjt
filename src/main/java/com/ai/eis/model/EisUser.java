package com.ai.eis.model;

import java.util.Date;

import javax.persistence.Transient;

public class EisUser {
    private Integer userid;

    private String name;

    private String sex;

    private String signature;

    private String phone;

    private String email;

    private Integer dmId;

    private String salary;

    private Integer rankId;

    private Integer postId;

    private Date entryTime;

    private Date registeTime;

    private String photo;

    private Integer roleId;

    private String remarks;

    private String account;

    //目前只支持一个用户一个角色，后续一个用户可以有多个角色 
    //要改下表结构
    @Transient
    private EisRole role;

    @Transient
    private EisLogin loginInfo;

    private String password;

    private String roleName;

    private String status;


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getDmId() {
        return dmId;
    }

    public void setDmId(Integer dmId) {
        this.dmId = dmId;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary == null ? null : salary.trim();
    }

    public Integer getRankId() {
        return rankId;
    }

    public void setRankId(Integer rankId) {
        this.rankId = rankId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getRegisteTime() {
        return registeTime;
    }

    public void setRegisteTime(Date registeTime) {
        this.registeTime = registeTime;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public EisRole getRole() {
        return role;
    }

    public void setRole(EisRole role) {
        this.role = role;
    }

    public EisLogin getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(EisLogin loginInfo) {
        this.loginInfo = loginInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}