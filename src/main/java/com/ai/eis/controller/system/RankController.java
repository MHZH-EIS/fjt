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
import com.ai.eis.common.Constants;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisContract;
import com.ai.eis.model.EisPost;
import com.ai.eis.model.EisRank;
import com.ai.eis.service.EisRankService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@RequestMapping("/system/rank")
@Transactional(readOnly = true)
public class RankController {
    private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
    @Autowired
    EisRankService rankService;
    
    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Integer id,Model model) {
    	 if (id != null) {
             ObjectMapper mapper = new ObjectMapper();
             EisRank resource = rankService.selectByPrimaryKey(id);
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
    public AjaxResult addContract(@Valid EisRank eisrank, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
 
        rankService.add(eisrank);
        logger.info("职级{}录入成功", eisrank.getName());
        return new AjaxResult(true);
    }
    
    @ResponseBody
    @RequestMapping(value = "/list")
    public List <EisRank> postquery(@RequestParam(value = "rankId", defaultValue = "") String pId,
                                        @RequestParam(value = "name", defaultValue = "") String pName
                                      ) {
        Map <String, String> map = new HashMap <>();
        map.put("rankId", pId);
        map.put("name", Tools.liker(pName));
        return rankService.queryByCondition(map);
    }
    
    @ResponseBody
    @Transactional
    @RequestMapping(value = "/delete")
    public AjaxResult delete(@RequestParam(value = "id") Integer rankId) {
        rankService.delete(rankId);
        return new AjaxResult(true);
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/update")
    public AjaxResult update(@Valid EisRank post, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
        rankService.update(post);
        return new AjaxResult(true);
    }
    
    
}
