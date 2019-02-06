package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.common.FileModel;
import com.ai.eis.model.EisExperiment;
import com.ai.eis.model.EisUser;
import com.ai.eis.model.EisUserTask;
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
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/test/workflow")
public class WorkFlowControllerTest {

    private Logger logger = LoggerFactory.getLogger(WorkFlowControllerTest.class);
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
    ProcessEngineConfiguration processEngineConfiguration;

    @Autowired
    ProcessEngineFactoryBean processEngine;

    @Autowired
    FormService formService;

    @ResponseBody
    @RequestMapping("/test")
    public String test() throws FileNotFoundException {
       return ResourceUtils.getFile("classpath:templates").getAbsolutePath();
    }

    @RequestMapping("/deploy")
    @ResponseBody
    public String deployProcess() {
        repositoryService.createDeployment().addClasspathResource("./processes/eisprocess.bpmn").deploy();
        return "流程发布成功";
    }
    
    @RequestMapping("/getPath")
    @ResponseBody
    public String getPath() {
    	return FileModel.getBasePath();
    }
    
 

	@RequestMapping("/clear")
	@ResponseBody
	public void clear() {
		
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        //连接数据库配置
        processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/test?characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("mhq19831030");
        
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        //工作流的核心对象，ProcessEngine对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
 
        
		/*ProcessEngine processEngine =  processEngineConfiguration.
			      setDatabaseSchema(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_DROP_CREATE  )
			      .buildProcessEngine();*/
	
	   processEngine.close();
	   logger.info("Clear tables...");
	   return;
				
	}
	
    @RequestMapping("/start")
    @ResponseBody
    public AjaxResult start(String projectId, String charge) {
        if (StringUtils.isEmpty(projectId) || StringUtils.isEmpty(charge)) {
            return new AjaxResult(false).setData("必须选择一个项目和一个项目经理");
        }
        ProcessInstance pi = runtimeService.createProcessInstanceBuilder()
                                           .businessKey(projectId)
                                           .processDefinitionKey("eisprocess")
                                           .variable("manager", charge)
                                           .start();
        logger.info("项目流程创建成功，当前流程实例{},业务编码{},项目经理ID", pi.getId(), pi.getBusinessKey(), charge);
        return new AjaxResult(true);
    }

    @RequestMapping("/discard")
    @ResponseBody
    public AjaxResult card(String projectId) {
        Task task = taskService.createTaskQuery()
                               .processInstanceBusinessKey(projectId)
                               .singleResult();

        Map <String, String> map = new HashMap <>();
        map.put("item1", "23");
        map.put("item2", "24");
        map.put("experiment1", "重量测试2");
        map.put("experiment2", "防水测试2");
        map.put("user1", "A");
        map.put("user2", "B");
        formService.submitTaskFormData(task.getId(), map);
        // taskService.complete(task.getId(), map);
        return new AjaxResult(true);
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

    @RequestMapping("/getTaskByProject")
    @ResponseBody
    public List <EisUserTask> getByProject(String projectId) {
        List <EisUserTask> tasks = new ArrayList <>();
        List <Task> list = taskService.createTaskQuery().processInstanceBusinessKey(projectId)
                                      .list();
        for (Task task : list) {
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


    @RequestMapping("/getProjectByUser")
    @ResponseBody
    public String getByAssgin(String name) {
        return taskService.createTaskQuery().taskAssignee(name)
                          .list()
                          .stream().map(Task::getName).collect(Collectors.joining(","));
        //   .forEach(task -> System.out.println(task.getName()));

    }

    @RequestMapping("/complete")
    @ResponseBody
    public String complete(String name) {
        Task task = taskService.createTaskQuery().taskAssignee(name)
                               .singleResult();
        taskService.complete(task.getId());
        return "success";
    }

    @RequestMapping("/completeByProject")
    @ResponseBody
    public void completeByProject(String projectId) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(projectId)
                               .singleResult();
        taskService.complete(task.getId());
    }

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
        }
        return tasks;
    }

    @RequestMapping("/image")
    public void getActivitiProccessImage(String projectId, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                                                                        .processInstanceBusinessKey(projectId)
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
                                                         .processInstanceBusinessKey(projectId)
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
        List <String> flowIdList = new ArrayList <String>();
        List <FlowNode> historicFlowNodeList = new LinkedList <FlowNode>();
        List <HistoricActivityInstance> finishedActivityInstanceList = new LinkedList <HistoricActivityInstance>();
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
                List <Map <String, String>> tempMapList = new LinkedList <Map <String, String>>();
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
        InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIdList, "宋体", "微软雅黑", "黑体", null, 2.0);

        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    public static void main(String[] args) {
        List <EisExperiment> list = new ArrayList <>();
        Supplier <IntStream> supplier = () -> list.stream().mapToInt(EisExperiment::getProjectId).distinct();
        System.out.println(supplier.get().count());
        supplier.get().findFirst();
    }


}
