package com.ai.eis.common;

 

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
 
import org.springframework.web.servlet.ModelAndView;
 

@ControllerAdvice
public class CommonExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";
    private Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);
	
 
 
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        logger.error("..............");
        logger.error("url:{},exception:{}",mav.getModelMap().get("url"),mav.getModel().get("url"));
        e.printStackTrace();
        return mav;
    }
    
    
    
}
