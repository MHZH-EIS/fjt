package com.ai.eis.controller;

import com.ai.eis.model.EisSampleSend;
import com.ai.eis.model.EisSampleSign;
import com.ai.eis.service.EisContractService;
import com.ai.eis.service.EisSampleService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/sample")
public class SampleController {

    private Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private EisSampleService sampleService;

    @Autowired
    private EisContractService contractService;

    @Autowired
    private RuntimeService runtimeService;

    @RequestMapping("/send")
    public void sendSample(@RequestBody EisSampleSend record) {
        sampleService.send(record);
        logger.info("send {} smaple success", record.getSendNum());
    }

    @RequestMapping("/sign")
    public void signSample(@RequestBody EisSampleSign record) {
        sampleService.sign(record);
        logger.info("项目{}此次成功签收{}个样品", record.getProjectId(), record.getSingNum());
        int signNum = sampleService.getSampleNum(record.getProjectId());
        int totalNum = contractService.getTotalSampleNum(record.getProjectId());
        // 比较合同中的样品数量，签收完毕一致则走下个流程
        if (signNum == totalNum) {
            ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                                               .processInstanceBusinessKey(String.valueOf(record.getProjectId()))
                                               .singleResult();
            Execution execution = runtimeService.createExecutionQuery()
                                                .messageEventSubscriptionName("signSample")
                                                .list()
                                                .stream()
                                                .filter(x -> x.getParentId().equals(pi.getId()))
                                                .findFirst()
                                                .get();
            runtimeService.messageEventReceived("signSample", execution.getId());
            logger.info("项目{}样品接受完毕,等待任务分配", record.getProjectId());
        }

    }

}
