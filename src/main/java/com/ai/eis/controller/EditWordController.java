package com.ai.eis.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.configuration.ApplicationConfigData;
import com.ai.eis.model.Member;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;

@Controller
public class EditWordController {
	private Logger logger = LoggerFactory.getLogger(EditWordController.class);

	@Autowired
	private ApplicationConfigData applicationData;
	private String posyspath;

	@RequestMapping(value = "/testtask/editword", method = RequestMethod.GET)
	public ModelAndView editWord(HttpServletRequest request, Map<String, Object> map,
			@RequestParam("filePath") String filePath, HttpSession session) {

		PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
		try {
			filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("文件名转换编码失败:{}", e1.getMessage());
			logger.error("文件名为:{}", filePath);
		}
		poCtrl.setServerPage("/poserver.zz");// 设置服务页面
		poCtrl.addCustomToolButton("保存", "Save", 1);// 添加自定义保存按钮
		poCtrl.addCustomToolButton("盖章", "AddSeal", 2);// 添加自定义盖章按钮
		poCtrl.addCustomToolButton("全屏显示", "fullScreen", 3);
		poCtrl.addCustomToolButton("关闭全屏", "cancelFullScreen", 4);
		poCtrl.addCustomToolButton("修改签章密码", "modifyPassword", 5);
		poCtrl.addCustomToolButton("打印报告", "Print", 6);
		poCtrl.setSaveFilePage("/testtask/savefile");// 设置处理文件保存的请求方法
		// 这句可以不要，设置了应该不用单独在word里面设置服务器了
		poCtrl.setZoomSealServer("http://127.0.0.1:8081/poserver.zz");

		// logger.info("GetPath:{}",this.getClass().getResource("/application.yml").getPath());

		Member userMember = (Member) session.getAttribute(Constants.SESSION_MEMBER_KEY);
		logger.info("用户:{} 打开文档:{}", userMember.getRealName(), filePath);

		if (filePath != null && !filePath.equals("null")) {
			poCtrl.webOpen(filePath, OpenModeType.docAdmin, userMember.getRealName());
		} else {
			poCtrl.webOpen("d:\\test.doc", OpenModeType.docAdmin, userMember.getRealName());
			filePath = "d:\\test.doc";
		}
		map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
		ModelAndView mv = new ModelAndView("/word/editword");
		mv.addObject("fileName", filePath);
		return mv;
	}

	@RequestMapping(value = "/testtask/readword", method = RequestMethod.GET)
	public ModelAndView readWord(HttpServletRequest request, Map<String, Object> map,
			@RequestParam("filePath") String filePath, HttpSession session) {

		PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
		try {
			filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("文件名转换编码失败:{}", e1.getMessage());
			logger.error("文件名为:{}", filePath);
		}
		poCtrl.setServerPage("/poserver.zz");// 设置服务页面
		poCtrl.addCustomToolButton("全屏显示", "fullScreen", 1);
		poCtrl.addCustomToolButton("关闭全屏", "cancelFullScreen", 2);
		poCtrl.addCustomToolButton("打印报告", "Print", 6);
		poCtrl.setSaveFilePage("/testtask/savefile");// 设置处理文件保存的请求方法

		Member userMember = (Member) session.getAttribute(Constants.SESSION_MEMBER_KEY);
		logger.info("用户:{} 以只读模式打开文档:{}", userMember.getRealName(), filePath);

		if (filePath != null && !filePath.equals("null")) {
			poCtrl.webOpen(filePath, OpenModeType.docReadOnly, userMember.getRealName());
		} else {
			poCtrl.webOpen("d:\\test.doc", OpenModeType.docReadOnly, userMember.getRealName());
			filePath = "d:\\test.doc";
		}
		map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
		ModelAndView mv = new ModelAndView("/word/readword");
		mv.addObject("fileName", filePath);
		return mv;
	}

	// 文件保存
	@RequestMapping("/testtask/savefile")
	public void savefile(HttpServletRequest request, HttpServletResponse response) {
		FileSaver fs = new FileSaver(request, response);
		String filename = fs.getFormField("fileName");
		logger.info("Get save fileName:{}", filename);
		fs.saveToFile(filename);
		fs.close();
	}

	// 文件保存
	@RequestMapping(value = "/mail/downloadfile", method = RequestMethod.GET)
	public AjaxResult downloadFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "filePath", defaultValue = "") String filePath) {

		try {
			if (filePath == null || filePath.equals("") || filePath.equals("null")) {
				return new AjaxResult(false).setMessage("传入的文件为空!");
			}
			File downloadFile = new File(filePath);
			if (!downloadFile.exists() || downloadFile.isDirectory()) {
				return new AjaxResult(false).setMessage("文件不存在或为目录");
			}
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filePath, "UTF-8"));
			FileInputStream in = new FileInputStream(filePath);
			OutputStream out = response.getOutputStream();
			byte buffer[] = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			in.close();
			out.close();

		} catch (Exception e) {
			logger.error("下载失败:{}", e.getMessage());
			e.printStackTrace();
		}

		return new AjaxResult(true);

	}

	/**
	 * 添加PageOffice的服务器端授权程序Servlet（必须）
	 * 
	 * @return
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		com.zhuozhengsoft.pageoffice.poserver.Server poserver = new com.zhuozhengsoft.pageoffice.poserver.Server();
		posyspath = applicationData.getPosyspath();
		logger.info("path:====={}", posyspath);
		poserver.setSysPath(posyspath);// 设置PageOffice注册成功后,license.lic文件存放的目录
		ServletRegistrationBean srb = new ServletRegistrationBean(poserver);
		srb.addUrlMappings("/poserver.zz");
		srb.addUrlMappings("/posetup.exe");
		srb.addUrlMappings("/pageoffice.js");
		srb.addUrlMappings("/sealsetup.exe");
		srb.addUrlMappings("/pobstyle.css");
		return srb;//
	}

	/**
	 * 添加印章管理程序Servlet（可选）
	 * 
	 * @return
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean2() {
		com.zhuozhengsoft.pageoffice.poserver.AdminSeal adminSeal = new com.zhuozhengsoft.pageoffice.poserver.AdminSeal();
		posyspath = applicationData.getPosyspath();
		adminSeal.setAdminPassword(applicationData.getPopassword());// 设置印章管理员admin的登录密码
		adminSeal.setSysPath(posyspath);// 设置印章数据库文件poseal.db存放的目录
		ServletRegistrationBean srb = new ServletRegistrationBean(adminSeal);
		srb.addUrlMappings("/adminseal.zz");
		srb.addUrlMappings("/sealimage.zz");
		srb.addUrlMappings("/loginseal.zz");
		return srb;//
	}

}
