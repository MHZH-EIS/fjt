package com.ai.eis.controller.system;


import com.ai.eis.service.EisMenuResourceService;
import com.ai.eis.model.EisMenuResource;
import com.ai.eis.common.DataGrid;
import com.ai.eis.common.AjaxResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * 资源管理控制器
 *
 * @author gson
 */
@Controller
@RequestMapping("/system/resource")
@Transactional(readOnly = true)
public class ResourceController {

    Logger logger = Logger.getLogger(RoleController.class);
 

    @Autowired
    EisMenuResourceService eisMenuResourceService;

    @RequestMapping
    public void index() {
    }

    @RequestMapping("/list")
    @ResponseBody
    public DataGrid<EisMenuResource> list() {
        return new DataGrid<>(eisMenuResourceService.selectAllResources());
    }

    @RequestMapping("/parent/tree")
    @ResponseBody
    public Iterable<EisMenuResource> parentTree() {
        return null;
    	//return eisMenuResourceService.getResourceTree();
    }

    @RequestMapping("form")
    public void form(Long id, Model model) {
        if (id != null) {
            ObjectMapper mapper = new ObjectMapper();
            EisMenuResource resource = eisMenuResourceService.selectByMenuId(id);
            try {
                model.addAttribute("resource", mapper.writeValueAsString(resource));
            } catch (JsonProcessingException e) {
                logger.error("json转换错误", e);
            }
            if (resource.getParent() != null) {
                model.addAttribute("parentId", resource.getParent().getId());
            }
        }
    }

    @RequestMapping({"/save", "/update"})
    @Transactional
    @ResponseBody
    public Object save(@Valid EisMenuResource resource, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        } else {
            return eisMenuResourceService.addMenuResource(resource);
        }
    }

    @RequestMapping("/delete")
    @Transactional
    @ResponseBody
    public AjaxResult delete(Long id) {
        try {
        	eisMenuResourceService.deleteMenuResource(id);
        } catch (Exception e) {
            return new AjaxResult().setMessage(e.getMessage());
        }
        return new AjaxResult();
    }
}
