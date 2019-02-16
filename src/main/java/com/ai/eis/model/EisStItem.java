package com.ai.eis.model;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;

public class EisStItem {
    private Integer itemId;

    private Integer stId;

    private String testName;

    private String clause;

    private String template;

    private String tableFile;

    private MultipartFile templateFile;
    
    private MultipartFile tabTemplateFile;

    @Transient
    private String text;

    public MultipartFile getTabTemplateFile() {
		return tabTemplateFile;
	}

	public void setTabTemplateFile(MultipartFile tabTemplateFile) {
		this.tabTemplateFile = tabTemplateFile;
	}

	public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getStId() {
        return stId;
    }

    public void setStId(Integer stId) {
        this.stId = stId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName == null ? null : testName.trim();
        setText(this.testName);
    }

    public String getClause() {
        return clause;
    }

    public void setClause(String clause) {
        this.clause = clause == null ? null : clause.trim();
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template == null ? null : template.trim();
    }

    public MultipartFile getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(MultipartFile templateFile) {
        this.templateFile = templateFile;
    }

    public String getTableFile() {
        return tableFile;
    }

    public void setTableFile(String tableFile) {
        this.tableFile = tableFile;
    }
}