package com.ai.eis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="eis")
public class ApplicationConfigData {
	
	/*超级用户ID不能删除*/
	@Value("${eis.super-user-id}")
	private Integer superUserId;
	
	/*pageoffice注册成功之后的license存放位置 license.lic*/
    @Value("${eis.posyspath}")
	private String posyspath;
	/*pageoffice 设置自带印章管理程序的登录密码*/
	@Value("${eis.popassword}")
	private String popassword;

	@Value("${eis.basepath}")
	private String basePath;
	
	
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

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
