package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.FileModel;
import com.ai.eis.common.WordCommon;
import com.ai.eis.model.EisExperiment;
import com.ai.eis.model.EisStItem;
import com.ai.eis.service.EisExperimentService;
import com.ai.eis.service.SItemService;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/experiment/appendix")
public class AppendixController {

    private Logger logger = LoggerFactory.getLogger(AppendixController.class);

    @Autowired
    private EisExperimentService experimentService;

    @Autowired
    private SItemService sItemService;


    @ResponseBody
    @RequestMapping("/getTableDesc")
    public AjaxResult getTableDesc(int experimentId) {
        try {
            EisExperiment experiment = experimentService.queryById(experimentId);
            EisStItem eisStItem = sItemService.queryById(experiment.getItemId());
            File file = new File(eisStItem.getTableFile());
            List <String> tables = WordCommon.getTable(file);
            return new AjaxResult(true).setData(tables);
        } catch (Docx4JException e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setMessage("表格模板不符合规范要求,解析失败");
        }
    }

    @ResponseBody
    @RequestMapping("/fillTable")
    public AjaxResult fillTable(int experimentId, List <String> dataList) {
        EisExperiment experiment = experimentService.queryById(experimentId);
        EisStItem eisStItem = sItemService.queryById(experiment.getItemId());
        try {
            File template = new File(eisStItem.getTableFile());
            File dst = FileModel.getExperimentTable(String.valueOf(experiment.getProjectId()), template.getName());
            WordCommon.fillTable(new File(eisStItem.getTableFile()), dst, dataList);
            return new AjaxResult(true);
        } catch (Docx4JException e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setMessage(e.getMessage());
        }
    }
}
