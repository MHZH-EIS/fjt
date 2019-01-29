package com.ai.eis.common;

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

@Service("ServiceTask")
public class ServiceTask implements JavaDelegate {

    private Logger logger = LoggerFactory.getLogger(ServiceTask.class);

    @Autowired
    private EisExperimentService experimentService;

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


            List <File> files = new ArrayList <>();
            List <Map <String, Object>> briefs = new ArrayList <>();
            for (int i = 0; i < experiments.size(); i++) {
                files.add(new File(experiments.get(i).get("file").toString()));
                Map <String, Object> map = new HashMap <>();
                map.put("index", i);
                map.put("experiment", experiments.get(i).get("experiment"));
                map.put("clause", experiments.get(i).get("clause"));
                map.put("result", experiments.get(i).get("result"));
                briefs.add(map);
            }

            //生成实验项目清单
            File experimentBriefTemplate = FileModel.getExperimentBriefTemplate();
            if (!experimentBriefTemplate.exists()) {
                throw new IOException("找不到实验项目清单模板文件");
            }
            WordCommon.createWordTable(FileModel.getExperimentBriefTemplate(), FileModel.generateExperimentBrief(projectId), briefs);

            //生成使用设备清单



            //合并实验报告
            File target = FileModel.generateMergeExperiment(projectId, "merge.docx");
            FileUtil.mergeWord(files, target);
            if (target.exists()) {
                logger.info("子实验项目报告合并成功");
            }


        } catch (IOException | Docx4JException | JAXBException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
