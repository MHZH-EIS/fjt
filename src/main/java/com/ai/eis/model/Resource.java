package com.ai.eis.model;

import com.ai.eis.model.enums.*;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


public class Resource {


    private Long id;


    private Resource parent;


    private String resName;


    private String resKey;

    private ResourceType resType;


    private String menuUrl;


    private String funUrls;

    private Integer weight = 0;

    private Boolean status = false;

    /**
     * 非持久化字段，可以使用@Transient标识
     */
    @Transient
    private List<Resource> children = new ArrayList<Resource>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getParent() {
        return parent;
    }

    public void setParent(Resource parent) {
        this.parent = parent;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResKey() {
        return resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey;
    }

    public ResourceType getResType() {
        return resType;
    }

    public void setResType(ResourceType resType) {
        this.resType = resType;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getFunUrls() {
        return funUrls;
    }

    public void setFunUrls(String funUrls) {
        this.funUrls = funUrls;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Resource> getChildren() {
        return children;
    }

    public void setChildren(List<Resource> children) {
        this.children = children;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getWeight() {
        return weight;
    }

    /**
     * 给JSON序列化时，获取ID
     *
     * @return
     */
    @JsonGetter("_parentId")
    public Long getParentId() {
        if (this.parent != null) {
            return this.parent.getId();
        }
        return null;
    }

    @JsonGetter("parent")
    public Long getParentId2() {
        if (this.parent != null) {
            return this.parent.getId();
        }
        return null;
    }

    /**
     * tree只认识text
     *
     * @return
     */
    @JsonGetter("text")
    public String getText() {
        return this.resName;
    }

    @Override
    public String toString() {
        return "Resource [id=" + id + ", resName=" + resName + ", resKey=" + resKey + ", resType=" + resType
                + ", menuUrl=" + menuUrl + ", funUrls=" + funUrls + ", status=" + status + "]";
    }
}
