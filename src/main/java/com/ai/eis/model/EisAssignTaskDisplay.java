package com.ai.eis.model;

import java.util.Date;

public class EisAssignTaskDisplay {

		private String projectNo;
		private String projectName;
		private String taskName;
		
		private String assignName;
		private Date assignTime;
		private Integer projectId;
		private String remarks;

		public String getProjectNo() {
			return projectNo;
		}

		public void setProjectNo(String projectNo) {
			this.projectNo = projectNo;
		}

		public String getProjectName() {
			return projectName;
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}

		public String getTaskName() {
			return taskName;
		}

		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}

		public String getAssignName() {
			return assignName;
		}

		public void setAssignName(String assignName) {
			this.assignName = assignName;
		}

		public Date getAssignTime() {
			return assignTime;
		}

		public void setAssignTime(Date assignTime) {
			this.assignTime = assignTime;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public Integer getProjectId() {
			return projectId;
		}

		public void setProjectId(Integer projectId) {
			this.projectId = projectId;
		}
}
