package com.ai.eis.model;

import java.util.Date;

public class EisStandard {
    private Integer stId;

    private Integer resourceId;

    private String name;

    private Date releaseDate;

    private String status;

    private String cardFormat;

    private String recordFormat;

    private String template;

    private String remarks;

    private String enclosure;

    private String switcher;

    private String cnas;

    private Date cnasDate;

    private String cb;

    private Date cbDate;

    public Integer getStId() {
        return stId;
    }

    public void setStId(Integer stId) {
        this.stId = stId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCardFormat() {
        return cardFormat;
    }

    public void setCardFormat(String cardFormat) {
        this.cardFormat = cardFormat == null ? null : cardFormat.trim();
    }

    public String getRecordFormat() {
        return recordFormat;
    }

    public void setRecordFormat(String recordFormat) {
        this.recordFormat = recordFormat == null ? null : recordFormat.trim();
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template == null ? null : template.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure == null ? null : enclosure.trim();
    }

    public String getSwitcher() {
        return switcher;
    }

    public void setSwitcher(String switcher) {
        this.switcher = switcher == null ? null : switcher.trim();
    }

    public String getCnas() {
        return cnas;
    }

    public void setCnas(String cnas) {
        this.cnas = cnas == null ? null : cnas.trim();
    }

    public Date getCnasDate() {
        return cnasDate;
    }

    public void setCnasDate(Date cnasDate) {
        this.cnasDate = cnasDate;
    }

    public String getCb() {
        return cb;
    }

    public void setCb(String cb) {
        this.cb = cb == null ? null : cb.trim();
    }

    public Date getCbDate() {
        return cbDate;
    }

    public void setCbDate(Date cbDate) {
        this.cbDate = cbDate;
    }
}