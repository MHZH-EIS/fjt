package com.ai.eis.model;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class EisCreateReport {
    private Integer projectNo;

    private String projectName;

    private Date submitTime;

    private Date completeReportTime;

    private String reportNo;

    private String remark;

    private MultipartFile qzoneFile;
    
    private String qzoneFilePath;

    private MultipartFile qplusMaxFile;
    
    private String qplusMaxFilePath;

    private MultipartFile qminusMaxFile;

    private String qminusMaxFilePath;

    private Integer dataSpeed;

    private String trfNo;
    
    private Integer ratePower;

    public Integer getRatePower() {
		return ratePower;
	}

	public void setRatePower(Integer ratePower) {
		this.ratePower = ratePower;
	}

	public String getReportFilePath() {
        return reportFilePath;
    }

    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

    private String reportFilePath;
    
    public MultipartFile getQzoneFile() {
		return qzoneFile;
	}

	public void setQzoneFile(MultipartFile qzoneFile) {
		this.qzoneFile = qzoneFile;
	}

	public MultipartFile getQplusMaxFile() {
		return qplusMaxFile;
	}

	public void setQplusMaxFile(MultipartFile qplusMaxFile) {
		this.qplusMaxFile = qplusMaxFile;
	}

	public MultipartFile getQminusMaxFile() {
		return qminusMaxFile;
	}

	public void setQminusMaxFile(MultipartFile qminusMaxFile) {
		this.qminusMaxFile = qminusMaxFile;
	}

    public Integer getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(Integer projectNo) {
        this.projectNo = projectNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getCompleteReportTime() {
        return completeReportTime;
    }

    public void setCompleteReportTime(Date completeReportTime) {
        this.completeReportTime = completeReportTime;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo == null ? null : reportNo.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getQzoneFilePath() {
        return qzoneFilePath;
    }

    public void setQzoneFilePath(String qzoneFilePath) {
        this.qzoneFilePath = qzoneFilePath == null ? null : qzoneFilePath.trim();
    }

    public String getQplusMaxFilePath() {
        return qplusMaxFilePath;
    }

    public void setQplusMaxFilePath(String qplusMaxFilePath) {
        this.qplusMaxFilePath = qplusMaxFilePath == null ? null : qplusMaxFilePath.trim();
    }

    public String getQminusMaxFilePath() {
        return qminusMaxFilePath;
    }

    public void setQminusMaxFilePath(String qminusMaxFilePath) {
        this.qminusMaxFilePath = qminusMaxFilePath == null ? null : qminusMaxFilePath.trim();
    }

    public Integer getDataSpeed() {
        return dataSpeed;
    }

    public void setDataSpeed(Integer dataSpeed) {
        this.dataSpeed = dataSpeed;
    }

    public String getTrfNo() {
        return trfNo;
    }

    public void setTrfNo(String trfNo) {
        this.trfNo = trfNo == null ? null : trfNo.trim();
    }
}