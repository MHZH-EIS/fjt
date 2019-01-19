package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.model.EisContract;
import com.ai.eis.model.EisExperiment;
import com.ai.eis.model.EisUser;
import com.ai.eis.service.EisContractService;
import com.ai.eis.service.EisExperimentService;


import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/resource/contract")
public class ContractController {

    private Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EisContractService contractService;

    @Autowired
    private EisExperimentService experimentService;

    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Long id) {
    }

    @RequestMapping("/deploy")
    public void deployProcess() {
        repositoryService.createDeployment().addClasspathResource("./processes/eisprocess.bpmn").deploy();
    }

    @RequestMapping("/save")
    @Transactional
    @ResponseBody
    public AjaxResult addContract(@Valid EisContract contract, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }

        HttpSession session = request.getSession();
        EisUser user = (EisUser) session.getAttribute(Constants.SESSION_EIS_KEY);
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance pi = processInstanceBuilder.businessKey(String.valueOf(contract.getProjectId()))
                                                   .processDefinitionKey("eisprocess")
                                                   .variable("manager", user.getUserid())
                                                   .start();
        logger.info("项目流程创建成功，当前流程实例{}", pi.getId());
        contractService.insertContract(contract);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        taskService.complete(task.getId());
        logger.info("合同{}录入成功", contract.getProjectName());
        return new AjaxResult(true);
    }


    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List <EisContract> postquery(String pId, String pName) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("pName", pName);
        return contractService.queryByCondition(map);
    }

    @RequestMapping(value = "/delete")
    public AjaxResult delete(Integer projectId) {
        contractService.deleteByPrimaryKey(projectId);
        return new AjaxResult(true);
    }

    @RequestMapping(value = "/update")
    public AjaxResult update(@Valid EisContract contract, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
        contractService.update(contract);
        return new AjaxResult(true);
    }


    @ResponseBody
    @RequestMapping(value = "/needToHandle", produces = {"application/json;charset=UTF-8"})
    public List <EisContract> queryNeedToHandle() {
        HttpSession session = request.getSession();
        EisUser user = (EisUser) session.getAttribute("s_member");
        List <EisContract> results = taskService.createTaskQuery()
                                                .taskAssignee(String.valueOf(user.getUserid()))
                                                .list()
                                                .stream()
                                                .map(task -> runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult().getBusinessKey())
                                                .map(projectId -> contractService.selectByPrimaryKey(Integer.valueOf(projectId)))
                                                .collect(Collectors.toList());
        logger.info("代办人{}一共有{}个项目需要处理", user.getName(), results.size());
        return results;
    }


    @RequestMapping("/taskAllocat")
    public AjaxResult taskAllocat(String projectId, String charge) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(projectId).singleResult();
        Map <String, Object> variables = new HashMap <>();
        variables.put("charge", charge);
        taskService.complete(task.getId(), variables);
        return new AjaxResult(true);
    }

    @RequestMapping("/addExperiment")
    public AjaxResult addExperiment(List <EisExperiment> list) {
        for (EisExperiment eisExperiment : list) {
            experimentService.insert(eisExperiment);
        }
        return new AjaxResult(true);
    }


}
