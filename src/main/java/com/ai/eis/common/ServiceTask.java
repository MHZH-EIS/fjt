package com.ai.eis.common;

import com.ai.eis.service.EisDeviceService;
import com.ai.eis.service.EisExperimentService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("ServiceTask")
public class ServiceTask implements JavaDelegate {

    private Logger logger = LoggerFactory.getLogger(ServiceTask.class);

    @Autowired
    private EisExperimentService experimentService;

    @Autowired
    private EisDeviceService deviceService;

    @Autowired
    private RuntimeService runtimeService;


    @Override
    public void execute(DelegateExecution execution) {
        try {
            String projectId = runtimeService.createProcessInstanceQuery()
                                             .processInstanceId(execution.getProcessInstanceId())
                                             .singleResult()
                                             .getBusinessKey();
            List <Map <String, Object>> experiments = experimentService.queryExperimentBrief(Integer.valueOf(projectId));
            List <Map <String, Object>> devs = deviceService.queryDevBrief(Integer.valueOf(projectId));

            createExperimentBrief(experiments, projectId);
            createDevBrief(devs, projectId);
            createDetailExperiment(experiments, projectId);

        } catch (IOException | Docx4JException | JAXBException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void createExperimentBrief(List <Map <String, Object>> experiments, String projectId) throws JAXBException, Docx4JException {
        List <Map <String, Object>> briefs = new ArrayList <>();
        for (int i = 0; i < experiments.size(); i++) {
            Map <String, Object> map = new HashMap <>();
            map.put("index", i + 1);
            map.put("experiment", experiments.get(i).get("experiment"));
            map.put("clause", experiments.get(i).get("clause"));
            map.put("result", experiments.get(i).get("result"));
            briefs.add(map);
        }
        File target = FileModel.generateDevBrief(projectId);
        WordCommon.createWordTable(FileModel.getDevBriefTemplate(), target, briefs);
        if (target.exists()) {
            logger.info("实验项目清单生成成功");
        }

    }

    private void createDevBrief(List <Map <String, Object>> devs, String projectId) throws JAXBException, Docx4JException {
        List <Map <String, Object>> briefs = new ArrayList <>();
        for (int i = 0; i < devs.size(); i++) {
            Map <String, Object> map = new HashMap <>();
            map.put("index", i + 1);
            map.put("name", devs.get(i).get("NAME"));
            map.put("version", devs.get(i).get("DEVICE_TYPE"));
            map.put("devNo", devs.get(i).get("DEV_NO"));
            map.put("validityDate", devs.get(i).get("VALIDITY_DATE"));
            briefs.add(map);
        }
        File target = FileModel.generateDevBrief(projectId);
        WordCommon.createWordTable(FileModel.getDevBriefTemplate(), target, briefs);
        if (target.exists()) {
            logger.info("设备清单生成成功");
        }
    }

    private void createDetailExperiment(List <Map <String, Object>> experiments, String projectId) throws IOException, Docx4JException {
        List <File> files = experiments.stream().map(m -> m.get("file").toString()).map(File::new).collect(Collectors.toList());
        File target = FileModel.generateMergeExperiment(projectId);
        FileUtil.mergeWord(files, target);
        if (target.exists()) {
            logger.info("子实验项目报告合并成功");
        }
    }


}
