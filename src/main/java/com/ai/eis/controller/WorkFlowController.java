package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.model.EisUser;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workflow")
public class WorkFlowController {

    private Logger logger = LoggerFactory.getLogger(WorkFlowController.class);
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @RequestMapping("/deploy")
    @ResponseBody
    public String deployProcess() {
        repositoryService.createDeployment().addClasspathResource("./processes/eisprocess.bpmn").deploy();
        return "流程发布成功";
    }

    @RequestMapping("/start")
    @ResponseBody
    public AjaxResult start(String projectId) {
        HttpSession session = request.getSession();
        EisUser user = (EisUser) session.getAttribute(Constants.SESSION_EIS_KEY);
        System.out.println(user.getUserid());
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance pi = processInstanceBuilder.businessKey(projectId)
                                                   .processDefinitionKey("eisprocess")
                                                   .variable("manager", user.getUserid())
                                                   .start();
        logger.info("项目流程创建成功，当前流程实例{},业务编码{}", pi.getId(), pi.getBusinessKey());
        return new AjaxResult(true);
    }

    @RequestMapping("/taskAllocat")
    @ResponseBody
    public AjaxResult taskAllocat(String projectId, String charge) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(projectId).singleResult();
        Map <String, Object> variables = new HashMap <>();
        variables.put("charge", charge);
        taskService.complete(task.getId(), variables);
        return new AjaxResult(true);
    }

    @RequestMapping("/query")
    @ResponseBody
    public List <Task> queryTask() {
        HttpSession session = request.getSession();
        EisUser user = (EisUser) session.getAttribute(Constants.SESSION_EIS_KEY);
        return taskService.createTaskQuery()
                          .taskAssignee(String.valueOf(user.getUserid()))
                          .list();
    }


}
