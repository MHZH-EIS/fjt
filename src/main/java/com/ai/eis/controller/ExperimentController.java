package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.FileModel;
import com.ai.eis.common.FileUtil;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisExperiment;
import com.ai.eis.model.EisExperimentDisplay;
import com.ai.eis.model.EisStItem;
import com.ai.eis.model.EisUser;
import com.ai.eis.service.EisExperimentService;
import com.ai.eis.service.EisUserService;
import com.ai.eis.service.SItemService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/resource/contract/experiment")
public class ExperimentController {

    private Logger logger = LoggerFactory.getLogger(ExperimentController.class);

    @Autowired
    private EisExperimentService experimentService;

    @Autowired
    private SItemService sItemService;

	@Autowired
	EisUserService userService;
	
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
            EisStItem eisStItem = sItemService.queryById(experiment.getItemId());
            if (eisStItem == null) {
                return new AjaxResult(false).setMessage("找不到此标准测试项");
            }
            File template = new File(eisStItem.getTemplate());
            if (!template.exists()) {
                return new AjaxResult(false).setMessage("找不到此标准测试项的模板文件");
            }
            File target = FileModel.generateExperiment(String.valueOf(experiment.getProjectId()), template.getName());
            FileUtil.nioTransferCopy(template, target);
            experiment.setFile(target.getAbsolutePath());
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
                                     @RequestParam(value = "id", defaultValue = "") String id,
                                     @RequestParam(value = "exName", defaultValue = "") String exName) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("id", id);
        map.put("exName", Tools.liker(exName));
        return experimentService.queryByCondition(map);
    }

    @ResponseBody
    @RequestMapping(value = "/setResult")
    public AjaxResult setResult( @RequestParam(value = "id", defaultValue = "") String id,
                                     @RequestParam(value = "result", defaultValue = "") String result) {
        Map <String, String> map = new HashMap <>();
        map.put("id", id);
        map.put("result", result);
        try {
            experimentService.updateResult(map);
        }catch (Exception e) {
        	logger.error(e.getMessage());
        	return new AjaxResult(false).setMessage(e.getMessage());
        }
    
        return new AjaxResult(true);
   }
    
    

    /**
     * @param pId 项目ID，列出所有此项目关联的子测试项
     * @param id  实验项目ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/display/list")
    public List <EisExperimentDisplay> displyList(@RequestParam(value = "projectId", defaultValue = "") String pId,
                                                  @RequestParam(value = "id", defaultValue = "") String id) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("id", id);

        List <EisExperimentDisplay> displays = new ArrayList <>();
        List <EisExperiment> lst = experimentService.queryByCondition(map);
        for (EisExperiment one : lst) {
        	EisUser user = null;
            EisExperimentDisplay display = new EisExperimentDisplay();
            EisStItem item = sItemService.queryById(one.getItemId());
            logger.info("userid:{}",one.getUserId());
            if (one.getUserId() != null) {
            	user = userService.selectByPrimaryKey(Integer.parseInt(one.getUserId()));
                //加上ID防止重名
                display.setAssign(user.getUserid()+":"+user.getName());
            }
            display.setItemId(one.getItemId());
            display.setProjectId(one.getProjectId());
            display.setClause(item.getClause());
            display.setTestId(one.getId());
            display.setRequirement(one.getRequirement());
            display.setTestName(item.getTestName());
            displays.add(display);
        }

        return displays;
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
            List <EisExperiment> list = list(pId, id, "");
            for (EisExperiment experiment : list) {
                File file = new File(experiment.getFile());
                if (file.exists()) {
                    file.delete();
                }
            }
            experimentService.deleteByCondition(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/update")
    public AjaxResult update(EisExperiment experiment) {
    	experimentService.update(experiment);
        return new AjaxResult(true);
    }


}
