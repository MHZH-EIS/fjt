package com.ai.eis.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ai.eis.configuration.ApplicationConfigData;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;

@Controller
public class EditWordController {
	
	
	@Autowired
	private ApplicationConfigData applicationData; 
	private String posyspath;
	
	
	
	
	
    @RequestMapping(value="/testtask/editword", method=RequestMethod.GET)
    public ModelAndView editWord(HttpServletRequest request, Map<String,Object> map){
		
    	posyspath = applicationData.getPosyspath();
		PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
		poCtrl.setServerPage("/poserver.zz");//设置服务页面
		poCtrl.addCustomToolButton("保存","Save",1);//添加自定义保存按钮
		poCtrl.addCustomToolButton("盖章","AddSeal",2);//添加自定义盖章按钮
		poCtrl.setSaveFilePage("/save");//设置处理文件保存的请求方法
		//打开word
		poCtrl.webOpen("d:\\test.doc",OpenModeType.docAdmin,"张三");
		map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
		
		ModelAndView mv = new ModelAndView("./word/editword");
		return mv;
	}
}
