package com.ai.eis.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.DataGrid;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisContract;
import com.ai.eis.model.EisDevice;
import com.ai.eis.model.EisExperimentDisplay;
import com.ai.eis.model.EisPost;
import com.ai.eis.model.EisRank;
import com.ai.eis.service.EisContractService;
import com.ai.eis.service.EisPostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;





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
    
    @Autowired
    private EisPostService eispostService;
    
    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Integer id,Model model) {
    	 if (id != null) {
             ObjectMapper mapper = new ObjectMapper();
             EisPost resource = eispostService.selectByPrimaryKey(id);
             try {
                 model.addAttribute("resource", mapper.writeValueAsString(resource));
             } catch (JsonProcessingException e) {
                 logger.error("json转换错误", e);
             }
         }
    }
    
    @RequestMapping("/save")
    @Transactional
    @ResponseBody
    public AjaxResult addContract(@Valid EisPost eispost, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
 
        eispostService.add(eispost);
        logger.info("岗位名称{}录入成功", eispost.getName());
        return new AjaxResult(true);
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/list")
    public DataGrid <EisPost> postquery(Integer page,Integer rows,@RequestParam(value = "postId", defaultValue = "") String pId,
                                        @RequestParam(value = "name", defaultValue = "") String pName
                                      ) {
    	com.github.pagehelper.Page<Object> pg = null;
    	if (page != null && rows != null) {
    		pg = PageHelper.startPage(page, rows);
    	}

     
        List <EisPost> lst =  postquery(pId,pName);
        DataGrid<EisPost> dg = new DataGrid<EisPost>(lst);

    	if (pg != null) {
        	dg.setTotal(pg.getTotal());
    	} else {
        	if (lst.size()!=0) {
               	dg.setTotal(1L);
        	}else {
        		dg.setTotal(0L);
        	}
    	}
    	
        return dg;
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/listPosts")
    public List <EisPost> postquery( @RequestParam(value = "postId", defaultValue = "") String pId,
                                        @RequestParam(value = "name", defaultValue = "") String pName
                                      ) {
    	 
        Map <String, String> map = new HashMap <>();
        map.put("postId", pId);
        map.put("name", Tools.liker(pName));
        List <EisPost> lst =  eispostService.queryByCondition(map);    
        return lst;
    }
    
    
    
    
    
    @ResponseBody
    @Transactional
    @RequestMapping(value = "/delete")
    public AjaxResult delete(@RequestParam(value = "id") Integer postId) {
    	eispostService.deletePost(postId);
        return new AjaxResult(true);
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/update")
    public AjaxResult update(@Valid EisPost post, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
        eispostService.update(post);
        return new AjaxResult(true);
    } 
    
}
