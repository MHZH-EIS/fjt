package com.ai.eis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/activiti")
public class ActivitiController {
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private ManagementService managementService;

    @ResponseBody
	@RequestMapping(value = "/singleAssignee", produces = {"application/json;charset=UTF-8"})
	public Map<String, Object> setSingleAssignee() {
		// 根据bpmn文件部署流程
		repositoryService.createDeployment().addClasspathResource("processes\\test\\DymicUser.bpmn").deploy();
		// 设置User Task1受理人变量
		Map<String, Object> variables = new HashMap<>();
		variables.put("user1", "017");
		// 采用key来启动流程定义并设置流程变量，返回流程实例
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("singleAssignee", variables);
		String processId = pi.getId();
		System.out.println("流程创建成功，当前流程实例ID：" + processId);

		// 注意 这里需要拿007来查询，key-value需要拿value来获取任务
		List<Task> list = taskService.createTaskQuery().taskAssignee("017").list();
		if (list != null && list.size() > 0) {
			for (org.activiti.engine.task.Task task : list) {
				System.out.println("任务ID：" + task.getId());
				System.out.println("任务的办理人：" + task.getAssignee());
				System.out.println("任务名称：" + task.getName());
				System.out.println("任务的创建时间：" + task.getCreateTime());
				System.out.println("流程实例ID：" + task.getProcessInstanceId());
				System.out.println("##################################");
			}
		}
		// 设置User Task2的受理人变量
		Map<String, Object> variables1 = new HashMap<>();
		variables1.put("user2", "Kevin1");
		
		
		
		// 因为007只有一个代办的任务，直接完成任务，并赋值下一个节点的受理人user2为Kevin办理
		taskService.complete(list.get(0).getId(), variables1);
		System.out.println("User Task1被完成了，此时流程已流转到User Task2");
		
		
		List<Task> list2 = taskService.createTaskQuery().taskAssignee("Kevin1").list();
		if (list2 != null && list2.size() > 0) {
			for (org.activiti.engine.task.Task task : list2) {
				System.out.println("任务ID：" + task.getId());
				System.out.println("任务的办理人：" + task.getAssignee());
				System.out.println("任务名称：" + task.getName());
				System.out.println("任务的创建时间：" + task.getCreateTime());
				System.out.println("流程实例ID：" + task.getProcessInstanceId());
				System.out.println("##################################");
			}
		}
		taskService.complete(list2.get(0).getId());
		
		//流程结束
		Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
		System.out.println("name:"+task);
		return variables1;
 
	}

}
