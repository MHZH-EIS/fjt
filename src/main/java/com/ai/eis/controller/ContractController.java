package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisContract;
import com.ai.eis.service.EisContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/resource/contract")
public class ContractController {

    private Logger logger = LoggerFactory.getLogger(ContractController.class);


    @Autowired
    private EisContractService contractService;

    @RequestMapping
    public void index() {

    }

    @RequestMapping("/form")
    public void form(Long id) {
    }


    @RequestMapping("/save")
    @Transactional
    @ResponseBody
    public AjaxResult addContract(@Valid EisContract contract, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
        contract.setResourceId(Constants.CONTRACT_RESOURCE_ID);
        contract.setStatus(Constants.PROJECT_TYPING);
        contractService.insertContract(contract);
        logger.info("合同{}录入成功", contract.getProjectName());
        return new AjaxResult(true);
    }


    @ResponseBody
    @RequestMapping(value = "/list")
    public List <EisContract> postquery(@RequestParam(value = "projectId", defaultValue = "") String pId,
                                        @RequestParam(value = "projectName", defaultValue = "") String pName,
                                        @RequestParam(value = "status", defaultValue = "") String status) {
        Map <String, String> map = new HashMap <>();
        map.put("pId", pId);
        map.put("pName", Tools.liker(pName));
        map.put("status", status);
        return contractService.queryByCondition(map);
    }

    @ResponseBody
    @RequestMapping(value = "/listwithtext")
    public List <EisContract> queryCombo() {
        Map <String, String> map = new HashMap <>();
        List <EisContract> orgs = contractService.queryByCondition(map);
        for (EisContract org : orgs) {
            org.setText(org.getProjectId() + ":" + org.getProjectName());
            org.setId(org.getProjectId());
        }
        return orgs;
    }


    @ResponseBody
    @Transactional
    @RequestMapping(value = "/delete")
    public AjaxResult delete(@RequestParam(value = "id") Integer projectId) {
        contractService.deleteByPrimaryKey(projectId);
        return new AjaxResult(true);
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/update")
    public AjaxResult update(@Valid EisContract contract, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }
        contractService.update(contract);
        return new AjaxResult(true);
    }


}
