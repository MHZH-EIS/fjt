package com.ai.eis.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="eis")
public class ApplicationConfigData {
	/*超级用户ID不能删除*/
	private Integer superUserId;
	
	/*pageoffice注册成功之后的license存放位置 license.lic*/
	private String posyspath;
	/*pageoffice 设置自带印章管理程序的登录密码*/
	private String popassword;

	public Integer getSuperUserId() {
		return superUserId;
	}

	public void setSuperUserId(Integer superUserId) {
		this.superUserId = superUserId;
	}

	public String getPosyspath() {
		return posyspath;
	}

	public void setPosyspath(String posyspath) {
		this.posyspath = posyspath;
	}

	public String getPopassword() {
		return popassword;
	}

	public void setPopassword(String popassword) {
		this.popassword = popassword;
	}
	
}
