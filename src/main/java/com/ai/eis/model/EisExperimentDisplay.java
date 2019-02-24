package com.ai.eis.model;

public class EisExperimentDisplay {

    private String testName;
    private String testClause;
    private Integer testId;
    private String clause;
    private String requirement;
    private Integer itemId;

    //指定测试工程师名字
    private String assign;

    private Integer projectId;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestClause() {
        return testClause;
    }

    public void setTestClause(String testClause) {
        this.testClause = testClause;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getClause() {
        return clause;
    }

    public void setClause(String clause) {
        this.clause = clause;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemid) {
        this.itemId = itemid;
    }
}
