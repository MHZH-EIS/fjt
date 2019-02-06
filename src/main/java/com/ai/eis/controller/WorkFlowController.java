package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.common.FileModel;
import com.ai.eis.configuration.ApplicationConfigData;
import com.ai.eis.model.*;
import com.ai.eis.service.EisContractService;
import com.ai.eis.service.EisExperimentService;
import com.alibaba.fastjson.JSON;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
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
import java.io.*;
import java.util.*;
import java.util.function.Supplier;
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

    @Autowired
    private EisExperimentService experimentService;

    @Autowired
    private ApplicationConfigData applicationData;

    @RequestMapping("/assign")
    public void assign(@RequestParam(value = "id", defaultValue = "") String id) {

    }

    @RequestMapping("/item/form")
    public void itemForm(@RequestParam(value = "projectId", defaultValue = "") String projectId) {

    }

    @RequestMapping("/report/modify")
    public void modifyReportForm() {

    }

    @RequestMapping("/report/verify")
    public void verfrifyReportForm() {

    }

    @RequestMapping("/report/mail")
    public void mailReportForm() {

    }

    @RequestMapping("/query/tasks/his")
    public void queryHisTasks() {

    }

    @RequestMapping("/item/testform")
    public void displayform(Long id) {
    }

    @RequestMapping("item/taskform")
    public void taskForm(@RequestParam(value = "projectId", defaultValue = "") String projectId) {
    }


    @RequestMapping("/testdeal")
    public void testDeal(@RequestParam(value = "id", defaultValue = "") String id) {

    }
    
    @RequestMapping("/item/dealform")
    public void dealForm(@RequestParam(value = "id", defaultValue = "") String id) {

    }
 


    /**
     * 流程发布
     *
     * @return
     */
    @RequestMapping("/deploy")
    @ResponseBody
    public AjaxResult deployProcess() {
        String path = applicationData.getBasePath() + "/processes";
        // repositoryService.createDeployment().addClasspathResource(path + "/eisprocess.bpmn").deploy();
        try {
            repositoryService.createDeployment().addInputStream("eisprocess.bpmn",
                    new FileInputStream(new File(path + "/eisprocess.bpmn"))).deploy();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new AjaxResult(false).setMessage(e.getMessage());
        }
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
        HttpSession session = request.getSession();
        EisUser user = (EisUser) session.getAttribute(Constants.SESSION_EIS_KEY);
        ProcessInstance pi = runtimeService.createProcessInstanceBuilder()
                                           .businessKey(projectId)
                                           .processDefinitionKey("eisprocess")
                                           .variable("manager", charge)
                                           .variable("charger", user.getUserid())
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
     * @param listJSON 子实验任务集合
     * @return
     */
    @RequestMapping("/discard")
    @ResponseBody
    public AjaxResult discard(@RequestParam("jsonStr") String listJSON) {
        List <EisExperiment> list = JSON.parseArray(listJSON, EisExperiment.class);
        if (list.size() != 2) {
            return new AjaxResult(false).setData("目前仅能接受两个子实验任务");
        }
        Supplier <IntStream> supplier = () -> list.stream().mapToInt(EisExperiment::getProjectId).distinct();
        if (supplier.get().count() != 1) {
            return new AjaxResult(false).setData("发现子实验项不属于同一个项目，不能完成下卡");
        }
        Task task = taskService.createTaskQuery()
                               .processInstanceBusinessKey(String.valueOf(supplier.get().findFirst().getAsInt()))
                               .singleResult();
        if (task == null) {
            return new AjaxResult(false).setData("找不到此流程的相关任务");
        }
        Map <String, String> map = new HashMap <>();
        int index = 1;
        for (EisExperiment experiment : list) {
            map.put("item" + index, String.valueOf(experiment.getId()));
            map.put("user" + index, experiment.getUserId());
            map.put("experiment" + index, experiment.getExName());
            index++;
        }
        formService.submitTaskFormData(task.getId(), map);
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/completeTask")
    public AjaxResult completeTask(@RequestParam(value = "taskId", defaultValue = "") String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task == null) {
            return new AjaxResult(false).setData("找不到此任务");
        }
        taskService.complete(taskId);
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/endProcess")
    public AjaxResult endProcessInstance(@RequestParam(value = "taskId", defaultValue = "") String taskId) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                                                            .processInstanceId(task.getProcessInstanceId())
                                                            .singleResult();
            String projectId = processInstance.getBusinessKey();
            EisContract contract = new EisContract();
            contract.setStatus(Constants.PROJECT_FINISH);
            contract.setProjectId(Integer.valueOf(projectId));
            contractService.update(contract);
            taskService.complete(taskId);
        } catch (Exception e) {
            return new AjaxResult(false).setMessage(e.getMessage());
        }
        return new AjaxResult(true);
    }

    @RequestMapping("/task/display")
    @ResponseBody
    public List <EisAssignTaskDisplay> queryDisplayTasks(@RequestParam(value = "taskName", defaultValue = "") String taskName) {
        HttpSession session = request.getSession();
        List <EisAssignTaskDisplay> displayTasks = new ArrayList <>();
        List <EisUserTask> tasks = queryCurrentUserTask(taskName);

        for (EisUserTask task : tasks) {
            EisAssignTaskDisplay one = new EisAssignTaskDisplay();
            /*根据实验项目ID查询到具体的试验*/
            if (task.getItemId() != null) {
                logger.info("=========task itemId:{}", task.getItemId());
                EisExperiment experiment = experimentService.queryById(Integer.parseInt(task.getItemId()));
                if (experiment != null) {
                    one.setTestFilePath(experiment.getFile());
                }
            }

            /*下卡任务*/
            if (!taskName.equals(Constants.TEST_TASK)) {
                one.setTestFilePath(FileModel.getReportName(String.valueOf(task.getProjectId())));
            }
            one.setTaskName(task.getTaskName());
            one.setAssignTime(task.getDate());
            EisContract contract = contractService.selectByPrimaryKey(task.getProjectId());
            one.setProjectName(contract.getProjectName());
            one.setProjectNo(contract.getProjectNo());
            one.setProjectId(contract.getProjectId());
            EisUser user = (EisUser) session.getAttribute(Constants.SESSION_EIS_KEY);
            one.setAssignName(user.getName());
            one.setId(task.getItemId());
            one.setTaskId(task.getTaskId());
            displayTasks.add(one);
        }
        return displayTasks;
    }


    @RequestMapping("/query/task/his/display")
    @ResponseBody
    public List <EisAssignTaskDisplay> queryHisDisplayTasks() {
        HttpSession session = request.getSession();
        List <EisAssignTaskDisplay> displayTasks = new ArrayList <>();
        List <EisUserTask> tasks = queryCurrentUserHisTask();

        for (EisUserTask task : tasks) {
            EisAssignTaskDisplay one = new EisAssignTaskDisplay();
            /*根据实验项目ID查询到具体的试验*/
            if (task.getItemId() != null) {
                logger.info("=========task itemId:{}", task.getItemId());
                EisExperiment experiment = experimentService.queryById(Integer.parseInt(task.getItemId()));
                one.setTestFilePath(experiment.getFile());
            }


            one.setTaskName(task.getTaskName());
            one.setAssignTime(task.getDate());
            EisContract contract = contractService.selectByPrimaryKey(task.getProjectId());
            if (contract != null && contract.getProjectName() != null) {
                one.setProjectName(contract.getProjectName());
                one.setProjectNo(contract.getProjectNo());
                one.setProjectId(contract.getProjectId());
            }
            EisUser user = (EisUser) session.getAttribute(Constants.SESSION_EIS_KEY);
            one.setAssignName(user.getName());
            one.setId(task.getItemId());
            one.setTaskId(task.getTaskId());
            displayTasks.add(one);
        }
        return displayTasks;
    }


    /**
     * 获取当前用户的任务集合
     *
     * @return
     */
    @RequestMapping("/queryCurrentUserTask")
    @ResponseBody
    public List <EisUserTask> queryCurrentUserTask(@RequestParam(value = "taskName", defaultValue = "") String taskName) {
        List <EisUserTask> tasks = new ArrayList <>();
        HttpSession session = request.getSession();
        EisUser user = (EisUser) session.getAttribute(Constants.SESSION_EIS_KEY);
        logger.info("=======userId:{} taskName:{}====", user.getUserid(),taskName);
        List <Task> list = taskService.createTaskQuery().taskAssignee(String.valueOf(user.getUserid())).list();
        for (Task task : list) {
            logger.info("taskId:{} taskName:{}",task.getId(),task.getName());
            if (!task.getName().contains(taskName)) {
                continue;
            }
            EisUserTask userTask = new EisUserTask();
            userTask.setTaskName(task.getName());
            userTask.setDate(task.getCreateTime());
            userTask.setTaskId(task.getId());
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

    @RequestMapping("/queryAllCurrentTaskByName")
    @ResponseBody
    public List <EisUserTask> queryAllCurrentTaskByName(@RequestParam(value = "taskName", defaultValue = "") String taskName) {
        List <EisUserTask> tasks = new ArrayList <>();
        List <Task> list = taskService.createTaskQuery().taskName(taskName).list();
        for (Task task : list) {
            EisUserTask userTask = new EisUserTask();
            userTask.setTaskName(task.getName());
            userTask.setDate(task.getCreateTime());
            userTask.setTaskId(task.getId());
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                                                            .processInstanceId(task.getProcessInstanceId())
                                                            .singleResult();
            userTask.setProjectId(Integer.valueOf(processInstance.getBusinessKey()));
            tasks.add(userTask);
        }
        return tasks;
    }


    /**
     * 获取当前用户的历史任务集合
     *
     * @return
     */
    @RequestMapping("/queryCurrentUserHisTask")
    @ResponseBody
    public List <EisUserTask> queryCurrentUserHisTask() {
        List <EisUserTask> tasks = new ArrayList <>();
        HttpSession session = request.getSession();
        EisUser user = (EisUser) session.getAttribute(Constants.SESSION_EIS_KEY);
        List <HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                                                         .taskAssignee(String.valueOf(user.getUserid()))
                                                         .list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            EisUserTask userTask = new EisUserTask();
            userTask.setTaskId(historicTaskInstance.getId());
            userTask.setTaskName(historicTaskInstance.getName());
            userTask.setDate(historicTaskInstance.getCreateTime());
            userTask.setCompleteDate(historicTaskInstance.getEndTime());
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                                                                            .processInstanceId(historicTaskInstance.getProcessInstanceId())
                                                                            .singleResult();
            if (historicProcessInstance.getBusinessKey() != null &&
                    !historicProcessInstance.getBusinessKey().equals("null")) {
                userTask.setProjectId(Integer.valueOf(historicProcessInstance.getBusinessKey()));
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
        List <HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                                                         .processInstanceBusinessKey(id)
                                                         .list();
        if (list.size() > 0) {
            List <HistoricVariableInstance> hvlist = historyService.createHistoricVariableInstanceQuery()
                                                                   .processInstanceId(list.get(0).getProcessInstanceId())
                                                                   .list();
            for (HistoricVariableInstance historicVariableInstance : hvlist) {
                if (historicVariableInstance.getVariableName().equals("experiment1")) {
                    bpmnModel.getFlowElement("test1").setName(String.valueOf(historicVariableInstance.getValue()));
                } else if (historicVariableInstance.getVariableName().equals("experiment2")) {
                    bpmnModel.getFlowElement("test2").setName(String.valueOf(historicVariableInstance.getValue()));
                }
            }
        }
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
            if (historicActivityInstance.getActivityType().equals("parallelGateway")
                    || historicActivityInstance.getActivityType().equals("inclusiveGateway")) {
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
                Optional <Map <String, String>> activityStartTime = tempMapList.stream().min(Comparator.comparing(x -> Long.valueOf(x.get("activityStartTime"))));
                activityStartTime.ifPresent(map -> flowIdList.add(map.get("flowId")));
            }

        }

        ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,
                flowIdList, "宋体", "微软雅黑", "黑体", null, 2.0);

        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
        return new AjaxResult(true);
    }

}
