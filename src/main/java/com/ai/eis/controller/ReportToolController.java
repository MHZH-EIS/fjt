package com.ai.eis.controller;



import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.DataGrid;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisCreateReports;
import com.ai.eis.model.EisDevice;
import com.ai.eis.service.EisCreateReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/report-tool/project")
public class ReportToolController {
    private Logger logger = LoggerFactory.getLogger(ReportToolController.class);


    @Autowired
    private EisCreateReportService eisCreateReportService;


    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Integer id, Model model) {
        if(id != null) {
            ObjectMapper mapper = new ObjectMapper();
            EisCreateReports resource = eisCreateReportService.queryById(id);

            try {
                model.addAttribute("resource", mapper.writeValueAsString(resource));
            } catch (JsonProcessingException e) {
                logger.error("json转换错误", e);
            }
        }
    }


    @Transactional
    @RequestMapping(value = "/save")
    @ResponseBody
    public AjaxResult insertDevice(@Valid EisCreateReports report, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        } else {
            if (report.getProjectNo() != null) {
                eisCreateReportService.update(report);
                logger.info("更新了一个项目{}", report.getProjectName());
            } else {
                eisCreateReportService.insert(report);
                logger.info("添加了一个新项目{}", report.getProjectName());
            }
            return new AjaxResult(true);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/list")
    public DataGrid<EisCreateReports> list(Integer page, Integer rows, @RequestParam(value = "projectNo", defaultValue = "") String projectNo,
                                    @RequestParam(value = "projectName", defaultValue = "") String projectName,
                                    @RequestParam(value = "reportNo", defaultValue = "") String reportNo) {
        com.github.pagehelper.Page<Object> pg = PageHelper.startPage(page, rows);

        List<EisCreateReports> lst = listAll(projectNo,projectName,reportNo);
        DataGrid<EisCreateReports> dg = new DataGrid<EisCreateReports>(lst);
        dg.setTotal(pg.getTotal());

        return dg;
    }

    @ResponseBody
    @RequestMapping(value = "/listAll")
    private List<EisCreateReports> listAll( @RequestParam(value = "projectNo", defaultValue = "") String projectNo,
                                            @RequestParam(value = "projectName", defaultValue = "") String projectName,
                                            @RequestParam(value = "reportNo", defaultValue = "") String reportNo) {
        Map<String, String> map = new HashMap<>();
        map.put("projectNo", Tools.liker(projectNo));
        map.put("projectName", Tools.liker(projectName));
        map.put("reportNo", Tools.liker(reportNo));
        return  eisCreateReportService.queryByCondition(map);
    }


    @ResponseBody
    @RequestMapping(value = "/remove")
    public AjaxResult remove(@RequestParam(value = "projectNo") Integer projectNo) {
        try {
            eisCreateReportService.deleteByPrimaryKey(projectNo);
            return new AjaxResult(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
    }


}
