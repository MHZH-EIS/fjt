package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.model.EisContract;
import com.ai.eis.model.EisExperiment;
import com.ai.eis.model.EisUser;
import com.ai.eis.model.EisUserTask;
import com.ai.eis.service.EisContractService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngineFactoryBean processEngine;

    @Autowired
    private FormService formService;

    @Autowired
    private EisContractService contractService;

    @RequestMapping("/assign")
    public void assign(@RequestParam(value = "id", defaultValue = "") String id) {

    }

    /**
     * 流程发布
     *
     * @return
     */
    @RequestMapping("/deploy")
    @ResponseBody
    public AjaxResult deployProcess() {
        repositoryService.createDeployment().addClasspathResource("./processes/eisprocess.bpmn").deploy();
        return new AjaxResult(true);
    }

    /**
     * 启动流程实例
     *
     * @param projectId 项目ID,必填参数
     * @param charge    指派的项目经理ID，必填参数
     * @return
     */
    @RequestMapping("/start")
    @ResponseBody
    public AjaxResult start(@RequestParam(value = "projectId", defaultValue = "") String projectId,
                            @RequestParam(value = "userId", defaultValue = "") String charge) {
        if (StringUtils.isEmpty(projectId) || StringUtils.isEmpty(charge)) {
            return new AjaxResult(false).setData("必须选择一个项目和一个项目经理");
        }
        ProcessInstance pi = runtimeService.createProcessInstanceBuilder()
                                           .businessKey(projectId)
                                           .processDefinitionKey("eisprocess")
                                           .variable("manager", charge)
                                           .start();
        EisContract contract = new EisContract();
        contract.setStatus(Constants.PROJECT_PROCESSING);
        contract.setProjectId(Integer.valueOf(projectId));
        contractService.update(contract);
        logger.info("项目流程创建成功，当前流程实例{},业务编码{},项目经理ID", pi.getId(), pi.getBusinessKey(), charge);
        return new AjaxResult(true);
    }


    /**
     * 下卡
     *
     * @param list 子实验任务集合
     * @return
     */
    @RequestMapping("/discard")
    @ResponseBody
    public AjaxResult discard(List <EisExperiment> list) {
        if (list.size() != 2) {
            return new AjaxResult(false).setData("目前仅能接受两个子实验任务");
        }
        IntStream stream = list.stream().mapToInt(EisExperiment::getProjectId).distinct();
        if (stream.count() != 1) {
            return new AjaxResult(false).setData("发现子实验项不属于同一个项目，不能完成下卡");
        }

        Task task = taskService.createTaskQuery()
                               .processInstanceBusinessKey(String.valueOf(stream.findFirst().getAsInt()))
                               .singleResult();
        if (task == null) {
            return new AjaxResult(false).setData("找不到此流程的相关任务");
        }
        Map <String, String> map = new HashMap <>();
        int index = 1;
        for (EisExperiment experiment : list) {
            map.put("item" + index, String.valueOf(experiment.getId()));
            map.put("user" + index, experiment.getUserId());
            map.put("experiment" + index, String.valueOf(experiment.getItemId()));
            index++;
        }
        formService.submitTaskFormData(task.getId(), map);
        return new AjaxResult(true);
    }


    @ResponseBody
    @RequestMapping("/completeTask")
    public AjaxResult completeTask(@RequestParam(value = "taskId", defaultValue = "") String taskId) {
        Task task = taskService.createTaskQuery()
                               .taskId(taskId)
                               .singleResult();

        if (task == null) {
            return new AjaxResult(false).setData("找不到此任务");
        }
        taskService.complete(taskId);
        return new AjaxResult(true);
    }


    /**
     * 获取当前用户的任务集合
     *
     * @return
     */
    @RequestMapping("/queryCurrentUserTask")
    @ResponseBody
    public List <EisUserTask> queryCurrentUserTask() {
        List <EisUserTask> tasks = new ArrayList <>();
        HttpSession session = request.getSession();
        EisUser user = (EisUser) session.getAttribute(Constants.SESSION_EIS_KEY);
        List <Task> list = taskService.createTaskQuery()
                                      .taskAssignee(String.valueOf(user.getUserid()))
                                      .list();
        for (Task task : list) {
            EisUserTask userTask = new EisUserTask();
            userTask.setTaskName(task.getName());
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                                                            .processInstanceId(task.getProcessInstanceId())
                                                            .singleResult();
            userTask.setProjectId(Integer.valueOf(processInstance.getBusinessKey()));
            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List <FormProperty> formProperties = taskFormData.getFormProperties();
            if (formProperties != null) {
                for (FormProperty formProperty : formProperties) {
                    if (formProperty.getId().startsWith("item")) {
                        userTask.setItemId(formProperty.getValue());
                    }
                }
            }
            tasks.add(userTask);
        }
        return tasks;
    }

    /**
     * 获取流程高亮图
     *
     * @param id       项目编号
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/image")
    @ResponseBody
    public AjaxResult getActivitiProccessImage(@RequestParam(value = "projectId", defaultValue = "") String id,
                                               HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(id)) {
            return new AjaxResult(false).setData("必须选择一个项目ID");
        }
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                                                                        .processInstanceBusinessKey(id)
                                                                        .singleResult();
        List <HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                                                                                  .processInstanceId(historicProcessInstance.getId())
                                                                                  .orderByHistoricActivityInstanceId()
                                                                                  .asc()
                                                                                  .list();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        List <String> executedActivityIdList = historicActivityInstances.stream()
                                                                        .map(HistoricActivityInstance::getActivityId)
                                                                        .collect(Collectors.toList());

        List <String> flowIdList = new ArrayList <>();
        List <FlowNode> historicFlowNodeList = new LinkedList <>();
        List <HistoricActivityInstance> finishedActivityInstanceList = new LinkedList <>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            FlowNode node = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            historicFlowNodeList.add(node);
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstanceList.add(historicActivityInstance);
            }
        }
        for (HistoricActivityInstance historicActivityInstance : finishedActivityInstanceList) {
            FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            List <SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();
            if (historicActivityInstance.getActivityType().equals("parallelGateway") || historicActivityInstance.getActivityType().equals("inclusiveGateway")) {
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    FlowNode targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicFlowNodeList.contains(targetFlowNode)) {
                        flowIdList.add(sequenceFlow.getId());
                    }
                }
            } else {
                List <Map <String, String>> tempMapList = new LinkedList <>();
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    for (HistoricActivityInstance activityInstance : historicActivityInstances) {
                        if (activityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            Map <String, String> map = new HashMap <>();
                            map.put("flowId", sequenceFlow.getId());
                            map.put("activityStartTime", String.valueOf(historicActivityInstance.getStartTime().getTime()));
                            tempMapList.add(map);
                        }
                    }
                }
                flowIdList.add(tempMapList.stream().min(Comparator.comparing(x -> Long.valueOf(x.get("activityStartTime")))).get().get("flowId"));
            }

        }

        ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIdList, "宋体", "微软雅黑", "黑体", null, 2.0);

        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
        return new AjaxResult(true);
    }


}
