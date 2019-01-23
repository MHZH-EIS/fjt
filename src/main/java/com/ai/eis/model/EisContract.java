package com.ai.eis.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

public class EisContract implements Serializable  {
	private Integer projectId;

	private Integer resourceId;

	private String clientCompany;

	private String clientAddress;

	private String contact;

	private String contactWay;

	private String verifyMethod;

	private String projectName;

	private String projectVersion;

	private Integer sampleNum;

	private String cheackType;

	private String mfName;

	private String mfAddress;

	private String reportType;

	@NumberFormat(pattern="#,###.##") 
	private Long testCost;

	@NumberFormat(pattern="#,###.##") 
	private Long cfCost;

	@NumberFormat(pattern="#,###.##") 
	private Long totalCost;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	private Date registeDate;

	private String exCase;

	private String remarks;
	
    @Transient
    private String text;
    
    @Transient
    private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public String getClientCompany() {
		return clientCompany;
	}

	public void setClientCompany(String clientCompany) {
		this.clientCompany = clientCompany == null ? null : clientCompany.trim();
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress == null ? null : clientAddress.trim();
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact == null ? null : contact.trim();
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay == null ? null : contactWay.trim();
	}

	public String getVerifyMethod() {
		return verifyMethod;
	}

	public void setVerifyMethod(String verifyMethod) {
		this.verifyMethod = verifyMethod == null ? null : verifyMethod.trim();
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName == null ? null : projectName.trim();
	}

	public String getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion == null ? null : projectVersion.trim();
	}

	public Integer getSampleNum() {
		return sampleNum;
	}

	public void setSampleNum(Integer sampleNum) {
		this.sampleNum = sampleNum;
	}

	public String getCheackType() {
		return cheackType;
	}

	public void setCheackType(String cheackType) {
		this.cheackType = cheackType == null ? null : cheackType.trim();
	}

	public String getMfName() {
		return mfName;
	}

	public void setMfName(String mfName) {
		this.mfName = mfName == null ? null : mfName.trim();
	}

	public String getMfAddress() {
		return mfAddress;
	}

	public void setMfAddress(String mfAddress) {
		this.mfAddress = mfAddress == null ? null : mfAddress.trim();
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType == null ? null : reportType.trim();
	}

	public Long getTestCost() {
		return testCost;
	}

	public void setTestCost(Long testCost) {
		this.testCost = testCost;
	}

	public Long getCfCost() {
		return cfCost;
	}

	public void setCfCost(Long cfCost) {
		this.cfCost = cfCost;
	}

	public Long getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Long totalCost) {
		this.totalCost = totalCost;
	}

	public Date getRegisteDate() {
		return registeDate;
	}

	public void setRegisteDate(Date registeDate) {
		this.registeDate = registeDate;
	}

	public String getExCase() {
		return exCase;
	}

	public void setExCase(String exCase) {
		this.exCase = exCase == null ? null : exCase.trim();
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks == null ? null : remarks.trim();
	}
}