package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.model.EisExperiment;
import com.ai.eis.service.EisExperimentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/resource/contract/experiment")
public class ExperimentController {

    private Logger logger = LoggerFactory.getLogger(ExperimentController.class);

    @Autowired
    private EisExperimentService experimentService;

    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Long id) {
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public AjaxResult add(EisExperiment experiment) {
        try {
            experimentService.insert(experiment);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(Boolean.TRUE);
    }

    /**
     * @param pId 项目ID，列出所有此项目关联的子测试项
     * @param id  实验项目ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list")
    public List <EisExperiment> list(@RequestParam(value = "projectId", defaultValue = "") String pId,
                                     @RequestParam(value = "id", defaultValue = "") String id) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("id", id);
        return experimentService.queryByCondition(map);
    }

    /**
     * @param pId 项目ID，传入项目ID，则删除此项目关联的所有的子测试项目
     * @param id  实验项目ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/remove")
    public AjaxResult remove(@RequestParam(value = "projectId", defaultValue = "") String pId,
                             @RequestParam(value = "id", defaultValue = "") String id) {

        if (StringUtils.isEmpty(pId) && StringUtils.isEmpty(id)) {
            return new AjaxResult(false).setData("删除实验项目必须传入一个参数");
        }
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("id", id);
        try {
            experimentService.deleteByCondition(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/update")
    public AjaxResult update() {
        return new AjaxResult(true);
    }


}
