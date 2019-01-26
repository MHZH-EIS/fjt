package com.ai.eis.controller.system;

//import cn.gson.crm.common.AjaxResult;
//import cn.gson.crm.common.DataGrid;
//import cn.gson.crm.common.MySpecification;
//import cn.gson.crm.common.MySpecification.Cnd;
//import cn.gson.crm.model.dao.MemberDao;
//import cn.gson.crm.model.dao.RoleDao;
//import cn.gson.crm.model.domain.Member;
//import cn.gson.crm.model.domain.Role;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.DataGrid;
import com.ai.eis.common.MySpecification;
import com.ai.eis.common.Tools;
import com.ai.eis.common.MySpecification.Cnd;

import com.ai.eis.model.EisUser;
import com.ai.eis.model.Member;
import com.ai.eis.service.EisLoginService;
import com.ai.eis.service.EisRoleService;
import com.ai.eis.service.EisUserService;
import com.ai.eis.model.EisLogin;
import com.ai.eis.model.EisRole;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 *
 * @author gson
 */
@Controller
@RequestMapping("/system/member")
@Transactional(readOnly = true)
public class MemberController {
	Logger logger = Logger.getLogger(RoleController.class);

	@Autowired
	EisRoleService roleService;
	@Autowired
	EisUserService userService;

	@Autowired
	EisLoginService loginService;

	/**
	 * 超级管理员id
	 */
   @Value("${eis.system.super-user-id}")
	Integer superUserId = 1;

	@RequestMapping
	public void index() {

	}

	@RequestMapping("/list")
	@ResponseBody
	public List<EisUser> list(  @RequestParam(value = "userName", defaultValue = "") String userName,
			@RequestParam(value = "account", defaultValue = "") String account,
			@RequestParam(value = "telephone", defaultValue = "") String telephone) {
 
		// 使用了自定义的复杂查询，这就比原生的Specification的语法使用流畅多了
		Map<String, String> conditions = new HashMap<>();
		conditions.put("userName", Tools.liker(userName));
		conditions.put("account", Tools.liker(account));
		conditions.put("telephone", telephone);

		return userService.queryByCondition(conditions);
	}

	@RequestMapping("/form")
	public void form(Long id, Model model) {

	}

	@RequestMapping("/check")
	@ResponseBody
	public boolean check(String account) {
		return loginService.selectByAccount(account) == null;
	}

	@RequestMapping("/roles")
	@ResponseBody
	public List<EisRole> roles() {
		return roleService.findAll();
	}

	@RequestMapping({ "/save", "/update" })
	@Transactional
	@ResponseBody
	public AjaxResult save(@Valid EisUser member, Integer roleId, BindingResult br) {
		if (br.hasErrors()) {
			logger.error("对象校验失败：" + br.getAllErrors());
			return new AjaxResult(false).setData(br.getAllErrors());
		} else {
			EisLogin loginInfo = null;
			// 更新代码
			if (member.getUserid() != null) {
				// 不在这里更新角色和密码
				EisUser orig = userService.selectByPrimaryKey(member.getUserid());

				// 理论上这里一定是要找得到对象的
				if (orig != null) {
					// 处理角色的关联
					if (roleId != null) {
						member.setRoleId(roleId);
					}
					userService.updateUser(member);
					loginInfo = orig.getLoginInfo();
					loginService.updateLogin(loginInfo);
				}
			} else {
				loginInfo = member.getLoginInfo();
				// 默认密码
				member.getLoginInfo().setPassword(DigestUtils.sha256Hex("0000"));
				userService.addUser(member);
				loginService.addLogin(member.getLoginInfo());
			}

			return new AjaxResult();
		}
	}

	/**
	 * 重置密码
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/password/reset")
	@Transactional
	@ResponseBody
	public AjaxResult resetPassword(Integer id) {
		EisLogin login = loginService.selectByPrimaryKey(id);
		login.setPassword(DigestUtils.sha256Hex("0000"));
		AjaxResult rs = new AjaxResult();
		rs.setSuccess(true);
		return rs;
	}

	@RequestMapping("/delete")
    @Transactional
    @ResponseBody
    public AjaxResult delete(Integer id) {
        try {
            if (superUserId != id) {
            	userService.deleteUser(id);
            } else {
                return new AjaxResult(false, "管理员不能删除！");
            }
        } catch (Exception e) {
            return new AjaxResult(false).setMessage(e.getMessage());
        }
        return new AjaxResult();
    }
}
