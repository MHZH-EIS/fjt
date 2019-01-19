package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.model.EisStandard;
import com.ai.eis.service.StandardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/standard")
public class StandardController {

    private Logger logger = LoggerFactory.getLogger(StandardController.class);

    @Autowired
    private StandardService standardService;

    @RequestMapping("/form")
    public void form(Long id) {
    }

    @ResponseBody
    @RequestMapping("/list")
    public List <EisStandard> queryByCondition(String name) {
        Map <String, String> map = new HashMap <>();
        map.put("name", name);
        return standardService.list(map);
    }

    @RequestMapping("/save")
    @Transactional
    @ResponseBody
    public AjaxResult add(EisStandard standard) {

        MultipartFile multipartFile = standard.getEnclosureFile();
        try {
            if (!multipartFile.isEmpty()) {
                String path = "./standard/" + standard.getName() + "/" + multipartFile.getOriginalFilename();
                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                multipartFile.transferTo(file);
                standard.setEnclosure(path);
            }
            standard.setResourceId(Constants.STANDARD_RESOURCE_ID);
            standardService.add(standard);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }


}
