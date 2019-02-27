package com.ai.eis.model;

import com.ai.eis.common.AttachmentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ai.eis.model.EisLogin;

import javax.persistence.*;
import java.io.File;
import java.util.Date;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2017 soho team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : 系统附件信息</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2017年07月12日</li>
 * <li>Author      : 郭华</li>
 * </ul>
 * <p>****************************************************************************</p>
 */

public class Attachment {

    private Long id;


    private AttachmentType type;

    /**
     * 上传人
     */

    private EisLogin member;

    /**
     * 文件原始名称
     */
    private String originalName;

    /**
     * 文件存储的相对路径
     */

    private String filePath;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型(后缀)
     */

    private String suffix;


    private String contentType;

    /**
     * 上传时间
     */

    private Date uploadTime = new Date();

    /**
     * 非持久化字段，不需要存储到数据库,也不做json的序列化
     */
    @Transient
    @JsonIgnore
    private File file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttachmentType getType() {
        return type;
    }

    public void setType(AttachmentType type) {
        this.type = type;
    }

    public EisLogin getMember() {
        return member;
    }

    public void setMember(EisLogin member) {
        this.member = member;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", type=" + type +
                ", member=" + member.getAccount() +
                ", originalName='" + originalName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", suffix='" + suffix + '\'' +
                ", contentType='" + contentType + '\'' +
                ", uploadTime=" + uploadTime +
                '}';
    }
}
