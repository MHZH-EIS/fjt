package com.ai.eis.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;





/**
 * 用户职务管理控制器
 *
 * @author gson
 */
@Controller
@RequestMapping("/system/post")
@Transactional(readOnly = true)
public class PostController {
    private Logger logger = LoggerFactory.getLogger(MemberController.class);
    
 
    
    
}
