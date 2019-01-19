package com.ai.eis.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonGetter;

 
 
public class EisMenuResource {
    private Long id;

    private String funUrls;

    private String menuUrl;

    private String resKey;

    private String resName;

    private String resType;

    private Boolean status;



	private Long parentId;

    private Integer weight;
    
    @Transient
    private EisMenuResource parent;
    
    private String text;
    
    /**
     * 非持久化字段，可以使用@Transient标识
     */
    @Transient
    private List<EisMenuResource> children = new ArrayList<EisMenuResource>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    
    
    public List<EisMenuResource> getChildren() {
		return children;
	}

	public void setChildren(List<EisMenuResource> children) {
		this.children = children;
	}

    public String getFunUrls() {
        return funUrls;
    }

    public void setFunUrls(String funUrls) {
        this.funUrls = funUrls == null ? null : funUrls.trim();
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    public String getResKey() {
        return resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey == null ? null : resKey.trim();
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName == null ? null : resName.trim();
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType == null ? null : resType.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    
    public Long getParentId() {
         
        return this.parentId;
    }
    
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

	public EisMenuResource getParent() {
		return parent;
	}
	
	
    @JsonGetter("parent")
    public Long getParentId2() {
        if (this.parentId != null) {
            return this.parentId;
        }
        return null;
    }

	public void setParent(EisMenuResource parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}