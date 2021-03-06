package com.ai.eis.common;

import com.ai.eis.model.EisContract;
import com.ai.eis.model.EisSampleSign;
import com.ai.eis.service.*;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang.StringUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("ServiceTask")
public class ServiceTask implements JavaDelegate {

    private Logger logger = LoggerFactory.getLogger(ServiceTask.class);
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
    private SimpleDateFormat format3 = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat format4 = new SimpleDateFormat("yyyyMM");

    @Autowired
    private EisExperimentService experimentService;

    @Autowired
    private EisDeviceService deviceService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private EisContractService contractService;

    @Autowired
    private EisSampleService sampleService;

    @Autowired
    private StandardService standardService;

    @Autowired
    private EisUserService userService;


    @Override
    public void execute(DelegateExecution execution) {
        try {
            String projectId = runtimeService.createProcessInstanceQuery()
                                             .processInstanceId(execution.getProcessInstanceId())
                                             .singleResult()
                                             .getBusinessKey();
            List <File> allFiles = new ArrayList <>();
            List <Map <String, Object>> experiments = experimentService.queryExperimentBrief(Integer.valueOf(projectId));
            List <Map <String, Object>> devs = deviceService.queryDevBrief(Integer.valueOf(projectId));
            EisContract contract = contractService.selectByPrimaryKey(Integer.valueOf(projectId));

            allFiles.add(createCover(contract, projectId, execution.getVariables()));
            allFiles.addAll(Arrays.asList(FileModel.getSample(projectId).listFiles()));
            allFiles.add(createExperimentBrief(experiments, projectId));
            allFiles.add(createDetailExperiment(experiments, projectId));
            allFiles.add(createDevBrief(devs, projectId));
            File tableFile = createExperimentTable(experiments, projectId);
            if (tableFile.exists()) {
                allFiles.add(tableFile);
            }
            allFiles.add(FileModel.getEndPage());

            FileUtil.mergeWord(allFiles, FileModel.generateReport(projectId));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage() + " 生成报告失败");
        }
        logger.info("最终报告生成成功");
    }

    private File createCover(EisContract contract, String projectId, Map <String, Object> variables) throws Exception {
        Map <String, String> paraMap = new HashMap <>();
        paraMap.put("pId", projectId);
        List <EisSampleSign> eisSampleSigns = sampleService.listSignRecord(paraMap);
        eisSampleSigns.sort(Comparator.comparing(EisSampleSign::getSignDate).reversed());

        List <Map <String, Object>> list = standardService.queryByProject(Integer.valueOf(projectId));
        String standards = list.stream().map(map -> map.get("ST_NO") + "《" + map.get("NAME") + "》").collect(Collectors.joining(" "));

        Map <String, String> map = new HashMap <>();
        map.put("projectNo", contract.getProjectNo());
        map.put("projectName", contract.getProjectName());
        map.put("version", contract.getProjectVersion());
        map.put("sampleNum", String.valueOf(contract.getSampleNum()));
        map.put("signDate", format1.format(eisSampleSigns.get(0).getSignDate()));
        map.put("clientCompany", contract.getClientCompany());
        map.put("clientAddress", contract.getClientAddress());
        map.put("mfName", contract.getMfName());
        map.put("mfAddress", contract.getMfAddress());
        Date date = new Date();
        map.put("sampleNo", format4.format(date));
        map.put("completeDate", format1.format(date));
        map.put("sDate", format2.format(date));
        map.put("sealDate", format3.format(date));
        map.put("standard", standards);
        map.put("manager", userService.selectByPrimaryKey(Integer.parseInt(String.valueOf(variables.get("manager")))).getName());
        map.put("charger", userService.selectByPrimaryKey(Integer.parseInt(String.valueOf(variables.get("charger")))).getName());
        map.put("signer", userService.selectByPrimaryKey(Integer.parseInt(String.valueOf(variables.get("charger")))).getName());
        map.put("refNo", contract.getProjectNo());
        map.put("footDate", format1.format(date));
        File target = FileModel.generateCoverFile(projectId);
        WordCommon.replacePlaceholder(FileModel.getCoverTemplate(), target, map);
        if (target.exists()) {
            logger.info("封面生成成功");
        }
        return target;
    }

    private File createExperimentBrief(List <Map <String, Object>> experiments, String projectId) throws JAXBException, Docx4JException {
        List <Map <String, Object>> briefs = new ArrayList <>();
        for (int i = 0; i < experiments.size(); i++) {
            Map <String, Object> map = new HashMap <>();
            map.put("index", i + 1);
            map.put("experiment", experiments.get(i).get("experiment"));
            map.put("clause", experiments.get(i).get("clause"));
            map.put("result", experiments.get(i).get("result"));
            briefs.add(map);
        }
        File target = FileModel.generateExperimentBrief(projectId);
        WordCommon.createWordTable(FileModel.getExperimentBriefTemplate(), target, briefs);
        if (target.exists()) {
            logger.info("实验项目清单生成成功");
        }
        return target;
    }

    private File createDevBrief(List <Map <String, Object>> devs, String projectId) throws JAXBException, Docx4JException {
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
        return target;
    }

    private File createDetailExperiment(List <Map <String, Object>> experiments, String projectId) throws IOException, Docx4JException {
        List <File> files = experiments.stream().map(m -> m.get("file").toString()).map(File::new).collect(Collectors.toList());
        File target = FileModel.generateMergeExperiment(projectId);
        FileUtil.mergeWord(files, target, false);
        if (target.exists()) {
            logger.info("子实验项目报告合并成功");
        }
        return target;
    }

    private File createExperimentTable(List <Map <String, Object>> experiments, String projectId) throws IOException, Docx4JException {
        List <File> tableFile = experiments.stream()
                                           .map(m -> m.get("tableFile") == null ? "" : String.valueOf(m.get("tableFile")))
                                           .filter(StringUtils::isNotEmpty)
                                           .map(File::new)
                                           .collect(Collectors.toList());
        File target = FileModel.generateMergeExperimentTable(projectId);
        if (tableFile.size() > 0) {
            logger.info("有{}个实验表待合并", tableFile.size());
            FileUtil.mergeWord(tableFile, target, false);
            if (target.exists()) {
                logger.info("子实验项目报告表文件合并成功");
            }
        }
        return target;
    }


}
