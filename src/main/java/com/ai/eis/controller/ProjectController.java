package com.ai.eis.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

 /***
  * 作用分配工程师页面，下卡主页面跳转
  * @author Think
  *
  */
@Controller
public class ProjectController {

    Logger logger = Logger.getLogger(ProjectController.class);
    
    /**
     * 分配工程师页面
     * @param id
     * @param model
     */
    @RequestMapping("/resource/project/assignform")
    public void form(Long id, Model model){
    	
    }
}
