package com.ai.eis.controller;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.Constants;
import com.ai.eis.common.DataGrid;
import com.ai.eis.common.FileModel;
import com.ai.eis.common.Tools;
import com.ai.eis.common.WordCommon;
import com.ai.eis.model.EisDevice;
import com.ai.eis.model.EisStItem;
import com.ai.eis.model.EisStandard;
import com.ai.eis.service.SItemService;
import com.ai.eis.service.StandardService;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public void form(Integer id, Model model) {
    	  if (id != null) {
              ObjectMapper mapper = new ObjectMapper();
              EisStandard resource = standardService.queryById(id);
              try {
                  model.addAttribute("resource", mapper.writeValueAsString(resource));
              } catch (JsonProcessingException e) {
                  logger.error("json转换错误", e);
              }
          }
    }

    @RequestMapping("/items/form")
    public void itemsform(Integer id, Model model ) {
  	  if (id != null) {
          ObjectMapper mapper = new ObjectMapper();
          EisStItem resource = sItemService.queryById(id);
          try {
              model.addAttribute("resource", mapper.writeValueAsString(resource));
          } catch (JsonProcessingException e) {
              logger.error("json转换错误", e);
          }
      }
    
    }

    @ResponseBody
    @RequestMapping("/list")
    public DataGrid<EisStandard> queryByCondition(Integer page,Integer rows, @RequestParam(value = "stNo", defaultValue = "") String sNo,
                                               @RequestParam(value = "name", defaultValue = "") String name) {
       
    	com.github.pagehelper.Page<Object> pg = PageHelper.startPage(page, rows);
        List<EisStandard> lst = queryAllByCondition(sNo,name);
        DataGrid<EisStandard> dg = new DataGrid<EisStandard>(lst);
		dg.setTotal(pg.getTotal());
		return dg;
    }

    @ResponseBody
    @RequestMapping("/listall")
    public List<EisStandard> queryAllByCondition(  @RequestParam(value = "stNo", defaultValue = "") String sNo,
                                               @RequestParam(value = "name", defaultValue = "") String name) {
       
    	Map <String, String> map = new HashMap <>();
        map.put("name", Tools.liker(name));
        map.put("sNo", Tools.liker(sNo));
        List<EisStandard> lst = standardService.list(map);
		return lst;
    }

    
    @ResponseBody
    @RequestMapping("/listtree")
    public List<EisStandard> querytree() {
       
    	Map <String, String> map = new HashMap <>();
        List<EisStandard> lst = standardService.list(map);
        logger.info("lst size:{}",lst.size());
        if (!lst.isEmpty()) {
        	for (EisStandard stand :lst) {
        		 List <EisStItem>  items = listAllItem(String.valueOf(stand.getStId()),"");
        		 stand.setChildren(items);
        	}
        }
		return lst;
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
                File file = FileModel.generateStandard(standard.getStNo(), multipartFile.getOriginalFilename());
                logger.info("save file path:{}", file.getAbsolutePath());
                multipartFile.transferTo(file);
                logger.info("标准附件上传成功，地址为{}", file.getAbsolutePath());
                standard.setEnclosure(file.getAbsolutePath());
            }
            if (standard.getStId() != null) {
            	standardService.update(standard);
                logger.info("标准{}更改成功", standard.getName());
            }else {
                standard.setResourceId(Constants.STANDARD_RESOURCE_ID);
                standardService.add(standard);
                logger.info("标准{}添加成功", standard.getName());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setMessage(e.getMessage());
        }
        return new AjaxResult(true);
    }


    @ResponseBody
    @RequestMapping("/remove")
    public AjaxResult delete(@RequestParam(value = "id") Integer stId) {
        try {
            EisStandard standard = standardService.queryById(stId);
            sItemService.deleteByStandardId(standard.getStId());
            logger.info("删除标准测试项目成功");
            standardService.delete(standard.getStId());
            logger.info("删除标准成功");
            File file = new File(standard.getEnclosure());
            if (file.getParentFile().exists()) {
                FileUtils.deleteDirectory(file.getParentFile());
            }
            logger.info("删除所有附件文档成功");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/item/list")
    public DataGrid <EisStItem> listItem(Integer page,Integer rows,@RequestParam(value = "stId", defaultValue = "") String stId,
                                     @RequestParam(value = "testName", defaultValue = "") String name) {
    	com.github.pagehelper.Page<Object> pg = null;
    	if (page != null && rows!= null) {
        	  pg = PageHelper.startPage(page, rows);
    	}

    	 
        List<EisStItem> lst = listAllItem(stId,name);
        DataGrid<EisStItem> dg = new DataGrid<EisStItem>(lst);
        if (pg == null) {
        	dg.setTotal(1L);
        }else  {
      		dg.setTotal(pg.getTotal());
        }

        return dg;
    }
    
    
    

    @ResponseBody
    @RequestMapping("/item/listall")
    public List <EisStItem> listAllItem(@RequestParam(value = "stId", defaultValue = "") String stId,
                                     @RequestParam(value = "testName", defaultValue = "") String name) {
 
    	Map <String, String> map = new HashMap <>();
        map.put("stId", stId);
        map.put("name", Tools.liker(name));
        List<EisStItem> lst = sItemService.queryByCondition(map);
 
        return lst;
    }
    
    
    @ResponseBody
    @RequestMapping("/item/add")
    @Transactional
    public AjaxResult addItem(@Valid EisStItem item, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        }

        MultipartFile multipartFile = item.getTemplateFile();
        MultipartFile tabFile = item.getTabTemplateFile();
       
        
        EisStandard standard = standardService.queryById(item.getStId());
        try {
            if (standard != null) {
                logger.info("当前测试项对应的标准为{}", standard.getName());
                
                File file = FileModel.generateItem(standard.getStNo(), multipartFile.getOriginalFilename());
                

                
                
                /*更新情况*/
                if (file.exists()) {
                	file.delete();
                }

                multipartFile.transferTo(file);
          
                logger.info("附件上传成功,地址为{}", file.getAbsolutePath());
                item.setTemplate(file.getAbsolutePath());
                
                if ( StringUtils.isNotBlank (tabFile.getOriginalFilename())) {
                    String newFileName = item.getTestName()+"_"+item.getClause()+".docx";
                    File tableFile = FileModel.generateTabItem(standard.getStNo(), newFileName);
                    
                    if (tableFile.exists()) {
                    	tableFile.delete();
                    }
                    
                    tabFile.transferTo(tableFile);
                    item.setTableFile(tableFile.getAbsolutePath());
                    logger.info("表格模板上传成功,地址为:{}",tableFile.getAbsolutePath());
                    List <LinkedHashMap <String, String>> tables = null;
                    try {
    					 tables = WordCommon.getTable(tableFile);
    				} catch (Docx4JException e) {
    					logger.error("获取表格模板信息失败:{}",e.getMessage());
    					return new  AjaxResult(false).setMessage("获取表格模板信息失败:"+e.getMessage());
    				}
                    String jsonStr = JSON.toJSONString(tables);
                    logger.info("获取表格模板信息:{}",jsonStr);
                    item.setTableInfo(jsonStr);
                }

                
                if(item.getItemId() != null) {
                    sItemService.update(item);
                    logger.info("测试项目{}修改成功", item.getTestName());
                }else {
                    sItemService.add(item);
                    logger.info("测试项目{}添加成功", item.getTestName());
                }

            } else {
                return new AjaxResult(false).setMessage("标准资源不存在").setData("找不到对应的标准资源");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new AjaxResult(false).setData(e);
        }
        return new AjaxResult(true);
    }

    @ResponseBody
    @RequestMapping("/item/delete")
    public AjaxResult deleteItem(@RequestParam(value = "id", defaultValue = "") Integer itemId) {
      
        try {
        	 EisStItem item = sItemService.queryById(itemId);
        	 if (item == null) {
        		 return new AjaxResult(false).setMessage("测试项不存在");
        	 }
        	 //先删除数据库记录防止因为数据库记录造成的删除失败文件会被删除掉
            sItemService.deleteByItemId(item.getItemId());
            
            File file = new File(item.getTemplate());
            if (file.exists()) {
                file.delete();
                logger.info("附件删除成功");
            }
            
            if (!StringUtils.isEmpty(item.getTableFile())) {
                File tabFile = new File(item.getTableFile());
                if (file.exists()) {
                	file.delete();
                	logger.info("表格模板删除成功:{}",item.getTableFile());
                }
            }

            logger.info("标准测试项{}删除成功", item.getTestName());
        }catch(Exception e) {
        	return   new AjaxResult(false).setMessage(e.getMessage());
        }

        return new AjaxResult(true);
    }

}
