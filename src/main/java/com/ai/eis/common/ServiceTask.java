package com.ai.eis.common;

import com.ai.eis.model.EisExperiment;
import com.ai.eis.service.EisExperimentService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("ServiceTask")
public class ServiceTask implements JavaDelegate {

    private Logger logger = LoggerFactory.getLogger(ServiceTask.class);

    @Autowired
    private EisExperimentService experimentService;

    @Override
    public void execute(DelegateExecution execution) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", execution.getProcessInstanceId());
        List <EisExperiment> list = experimentService.queryByCondition(map);
        List <File> files = list.stream().map(EisExperiment::getFile).map(File::new).collect(Collectors.toList());
        try {
            File target = FileModel.generateMergeExperiment(execution.getProcessInstanceId(), "merge.docx");
            FileUtil.mergeWord(files, target);
            if (target.exists()) {
                logger.info("子实验项目报告合并成功");
            }
        } catch (IOException | Docx4JException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
