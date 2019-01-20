package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.common.Tools;
import com.ai.eis.model.EisStItem;
import com.ai.eis.model.EisStandard;
import com.ai.eis.service.SItemService;
import com.ai.eis.service.StandardService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.Tool;
import javax.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/resource/standard")
public class StandardController {

    private Logger logger = LoggerFactory.getLogger(StandardController.class);

    @Autowired
    private StandardService standardService;

    @Autowired
    private SItemService sItemService;

    @RequestMapping
    public void index() {
    }


    @RequestMapping("/form")
    public void form(Long id) {
    }

    @ResponseBody
    @RequestMapping("/list")
    public List <EisStandard> queryByCondition(String name) {
        Map <String, String> map = new HashMap <>();
        map.put("name", Tools.liker(name));
        return standardService.list(map);
    }

    @RequestMapping("/save")
    @Transactional
    @ResponseBody
    public AjaxResult add(@Valid EisStandard standard, BindingResult br) {
    	 if (br.hasErrors()) {
             logger.error("对象校验失败：" + br.getAllErrors());
             return new AjaxResult(false).setData(br.getAllErrors());
         }

        MultipartFile multipartFile = standard.getEnclosureFile();
        try {
            if (!multipartFile.isEmpty()) {
                String path = "./standard/" + standard.getName() + "/" + multipartFile.getOriginalFilename();
                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                multipartFile.transferTo(file);
                logger.info("标准附件上传成功，地址为{}", path);
                standard.setEnclosure(path);
            }
            standard.setResourceId(Constants.STANDARD_RESOURCE_ID);
            standardService.add(standard);
            logger.info("标准{}添加成功", standard.getName());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/delete")
    public AjaxResult delete(List <EisStandard> standards) {
        try {
            for (EisStandard standard : standards) {
                sItemService.deleteByStandardId(standard.getStId());
                logger.info("删除标准测试项目成功");
                standardService.delete(standard.getStId());
                logger.info("删除标准成功");
                File file = new File(standard.getEnclosure());
                if (file.getParentFile().exists()) {
                    FileUtils.deleteDirectory(file.getParentFile());
                }
                logger.info("删除所有附件文档成功");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/item/list")
    public List <EisStItem> listItem(String sId, String name) {
        Map <String, String> map = new HashMap <>();
        map.put("sId", sId);
        map.put("name", Tools.liker(name));
        return sItemService.queryByCondition(map);
    }

    @ResponseBody
    @RequestMapping("/item/add")
    public AjaxResult addItem(EisStItem item) {
        MultipartFile multipartFile = item.getTemplateFile();
        HashMap <String, String> map = new HashMap <>();
        map.put("sId", item.getStId().toString());
        List <EisStandard> list = standardService.list(map);
        try {
            if (list.size() > 0) {
                EisStandard standard = list.get(0);
                logger.info("当前测试项对应的标准为{}", standard.getName());
                String path = "./standard/" + standard.getName() + "/item/" + multipartFile.getOriginalFilename();
                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                multipartFile.transferTo(file);
                logger.info("附件上传成功,地址为{}", path);
                item.setTemplate(path);
                sItemService.add(item);
                logger.info("测试项目{}添加成功", item.getTestName());
            } else {
                return new AjaxResult(false).setData("找不到对应的标准资源");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/item/delete")
    public AjaxResult deleteItem(EisStItem item) {
        File file = new File(item.getTemplate());
        if (file.exists()) {
            file.delete();
            logger.info("附件删除成功");
        }
        sItemService.deleteByItemId(item.getItemId());
        logger.info("标准测试项{}删除成功", item.getTestName());
        return new AjaxResult(true);
    }


}
