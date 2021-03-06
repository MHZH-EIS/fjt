package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.common.FileModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

 
import com.ai.eis.common.Constants;

import com.ai.eis.common.SocketMessage;
 
import com.ai.eis.service.EisLoginService;
import com.ai.eis.service.EisMenuResourceService;
import com.ai.eis.service.EisRoleMenuResourceService;
import com.ai.eis.service.EisRoleService;
import com.ai.eis.service.EisUserService;
import com.ai.eis.model.EisLogin;
import com.ai.eis.model.EisMenuResource;
import com.ai.eis.model.EisRole;
import com.ai.eis.model.EisRoleMenuResource;
import com.ai.eis.model.EisUser;
import com.ai.eis.model.Member;
 
import com.ai.eis.model.enums.ResourceType;

import com.ai.eis.common.VerifyCodeUtils;
import com.ai.eis.configuration.ApplicationConfigData;
import com.ai.eis.controller.system.MemberController;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

 
import static com.ai.eis.common.Constants.SESSION_VERIFY_CODE_KEY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.ai.eis.handler.*;
 


@Controller
public class AppController {
    private Logger logger = LoggerFactory.getLogger(MemberController.class);

	
	@Autowired
	EisLoginService eisLoginService;
	
	@Autowired
	EisUserService eisUserService;
	
	@Autowired
	ApplicationConfigData applicationData;
	
	@Autowired
	EisRoleService  eisRoleService;
	
	@Autowired
	EisMenuResourceService eisMenuResourceService;
	
	@Autowired
	EisRoleMenuResourceService eisRoleMenuResourceService;
	
    static Integer superUserId = 1;

    @Autowired
    WebSocketHandler webSocketHandler;
    
    private Integer userId;
    
    private  List<EisMenuResource>  allMenuResources;
    
    @RequestMapping("/")
    public String index() {
        return "index";
    }
    
    @RequestMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute(Constants.SESSION_MEMBER_KEY) != null) {
            return "redirect:/";
        }
        return "login";
    }
    
    @RequestMapping("/getUserId")
	@ResponseBody
    public String getUserId() {
    	return String.valueOf(userId);
    }
    
    /*根据ID获取菜单资源*/
    private EisMenuResource getMenuResourceById(Long id) {
    	    for (EisMenuResource one: allMenuResources) {
    	    	if (one.getId() == id) {
    	    		return one;
    	    	}
    	    }
    	    return null;
    }
    
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(String userName, String password, RedirectAttributes rAttributes,
    		HttpServletRequest request, HttpSession session) {
    	//登錄設置下基礎路徑
    	FileModel.setBasePath(applicationData.getBasePath());
    	logger.info("===========Path:{}",FileModel.getBasePath());
        // 参数校验
        if (isEmpty(userName) || isEmpty(password)) {
            rAttributes.addFlashAttribute("error", "参数错误！");
            return "redirect:/login";
        }

        EisLogin member = eisLoginService.selectByAccount(userName);
        // 校验密码
        if (member == null || member.getStatus() ==  0) {
            rAttributes.addFlashAttribute("error", "用户不存在或已被禁用！");
            return "redirect:/login";
        } else if (!member.getPassword().equals(DigestUtils.sha256Hex(password))) {
            rAttributes.addFlashAttribute("error", "用户名或密码错误！");
            return "redirect:/login";
        }

        if (webSocketHandler.isOnline( member.getUserid())) {
            //通知下线
            webSocketHandler.sendMessageToUser(member.getUserid(), new SocketMessage("logout", "").toTextMessage());
            webSocketHandler.offLine(member.getUserid());
        }
        
        final List<EisMenuResource> allResources;
        

        // 获取用户可用菜单,所有有权限的请求，所有资源key
        Integer userid = member.getUserid();
        
        EisUser user = eisUserService.selectByPrimaryKey(userid);
        Member userMember = new Member();
        
        EisRole role = eisRoleService.selectByPrimaryKey(user.getRoleId());

        List<EisRoleMenuResource>  lstMenuResources = eisRoleMenuResourceService.selectByRoleId(role.getRoleId());
        List<Long> menuIds = new ArrayList<Long>();		
        lstMenuResources.forEach(x-> menuIds.add(x.getMenuId()));
        
        userMember.setRealName(user.getName());
        userMember.setUserName(member.getAccount());
        userMember.setUserid(user.getUserid());
        userMember.setPassword(member.getPassword());
        user.setAccount(member.getAccount());
 
        
        List<EisMenuResource> menus = new ArrayList<EisMenuResource>();
        Set<String> urls = new HashSet<>();
        Set<String> resourceKey = new HashSet<>();
 
        if (superUserId == member.getUserid()) {
        	  // 超级管理员，直接获取所有权限资源
            allResources = eisMenuResourceService.selectAllResources();
            allMenuResources = allResources;
        } else {
            allResources = new ArrayList<>();
            List<EisMenuResource>   resources =  eisMenuResourceService.selectByRoleId(role.getRoleId());
            allResources.addAll(resources);
        }

        for (EisMenuResource t : allResources) {
        	Long parentId = t.getParentId();
            if (parentId != null) {
            	if (allMenuResources != null) {
                	t.setParent(getMenuResourceById(parentId));
            	}else {
            		t.setParent(eisMenuResourceService.selectByMenuId(parentId));
            	}
    
            }
            // 可用菜单
            if (t.getResType().equals(ResourceType.MENU.name())) {
                menus.add(t);
            }

            //所有请求资源
            if (isNotEmpty(t.getMenuUrl())) {
                urls.add(t.getMenuUrl());
            }
            
            if (t.getFunUrls()!= null) {
                String[] funUrls = t.getFunUrls().split(",");
                for (String url : funUrls) {
                    if (isNotEmpty(url)) {
                        urls.add(url);
                    }
                }
            }	
            // 资源key
            resourceKey.add(t.getResKey());
        }

        // 将用户可用菜单和权限存入session
        session.setAttribute("menus", menus);
        session.setAttribute("urls", urls);
        session.setAttribute("resourceKey", resourceKey);
        session.setAttribute(Constants.SESSION_MEMBER_KEY,  userMember);
        session.setAttribute(Constants.SESSION_EIS_KEY, user);
        session.setAttribute("userId", userMember.getUserid());
        userId = userMember.getUserid();
        // 是否是管理员
        session.setAttribute("isSuper", superUserId == member.getUserid());
        logger.info("================userId:{}========", session.getAttribute("userId"));
        return "redirect:/";
    }

    /**
     * 注册
     *
     * @return
     */
    @RequestMapping(value = "/reg", method = RequestMethod.GET)
    public String toReg() {
        return "reg";
    }
    
    /**
     * 校验码
     *
     * @param response
     * @param session
     */
    @RequestMapping(value = "/verify/code", method = RequestMethod.GET)
    public void verifyCode(HttpServletResponse response, HttpSession session) {
        try {
            String code = VerifyCodeUtils.outputVerifyImage(150, 50, response.getOutputStream(), 4);
            session.setAttribute(SESSION_VERIFY_CODE_KEY, code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    /**
     * 修改密码
     */
    @RequestMapping("/change/password")
    public String changePassword() {
        return "password";
    }
    
    
    /**
     * 修改密码
     */
    @RequestMapping(value = "/change/password", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public AjaxResult doChangePassword(@SessionAttribute(name = "s_member") Member member, String oldPassword, String newPassword1, String newPassword2) {
        if (isEmpty(oldPassword) || isEmpty(newPassword1) || isEmpty(newPassword2)) {
            return new AjaxResult(false).setMessage("参数错误！");
        }

        if (member == null)  {
        	logger.warn("Member is null.");
        }
        if (!member.getPassword().equals(DigestUtils.sha256Hex(oldPassword))) {
            return new AjaxResult(false).setMessage("原密码错误！");
        }

        if (!DigestUtils.sha256Hex(newPassword1).equals(DigestUtils.sha256Hex(newPassword2))) {
            return new AjaxResult(false).setMessage("新密码，两次不匹配！");
        }
 
        EisLogin loginUser =  eisLoginService.selectByPrimaryKey(member.getUserid());
 
        loginUser.setPassword(DigestUtils.sha256Hex(newPassword1));
        eisLoginService.updateLogin(loginUser);
        return new AjaxResult();
    }

    
    
    /**
     * 权限resource的js资源
     *
     * @param session
     * @return
     */
    @RequestMapping("/resource")
    public String login(HttpSession session, Model model) {
        ObjectMapper mapper = new ObjectMapper();
        Object resourceKey = session.getAttribute("resourceKey");
        try {
            model.addAttribute("resourceKey", mapper.writeValueAsString(resourceKey));
        } catch (JsonProcessingException e) {
            logger.error("json转换错误", e);
        }
        return "resource";
    }
    

    /**
     * 修改用户资料
     */
    @RequestMapping("/change/info")
    public String changeInfo() {
        return "info";
    }
    

    /**
     * 修改用户资料
     */
    @RequestMapping(value = "/change/info", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public AjaxResult doChangeInfo(@SessionAttribute("s_eis") EisUser user,
                                   Member member, HttpSession session) {

        if (isEmpty(member.getRealName()) || isEmpty(member.getUserName() )) {
            return new AjaxResult(false).setMessage("参数错误！");
        }
 
        eisUserService.updateUser(user);
        return new AjaxResult(true);
    }

    
}
