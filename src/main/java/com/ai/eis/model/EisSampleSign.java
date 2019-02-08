package com.ai.eis.model;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class EisSampleSign {

    private Integer id;

    private Integer projectId;

    private String sampleName;

    private Integer singNum;

    private String company;

    private String address;

    private String contact;

    private String phone;

    private Date signDate;

    private String type;

    private String status;

    private MultipartFile enclosureFile;
    
    private String projectNo;
 
    
 

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName == null ? null : sampleName.trim();
    }

    public Integer getSingNum() {
        return singNum;
    }

    public void setSingNum(Integer singNum) {
        this.singNum = singNum;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public MultipartFile getEnclosureFile() {
		return enclosureFile;
	}

	public void setEnclosureFile(MultipartFile enclosureFile) {
		this.enclosureFile = enclosureFile;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
}