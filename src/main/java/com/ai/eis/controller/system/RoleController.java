package com.ai.eis.controller.system;

 

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.DataGrid;
import com.ai.eis.model.EisMenuResource;
import com.ai.eis.model.EisRank;
import com.ai.eis.model.EisRole;
import com.ai.eis.model.EisRoleMenuResource;
import com.ai.eis.model.EisRoleWithMenuReource;
import com.ai.eis.service.EisMenuResourceService;
import com.ai.eis.service.EisRoleMenuResourceService;
import com.ai.eis.service.EisRoleService;
import com.github.pagehelper.PageHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理控制器
 *
 * @author gson
 */
@Controller
@RequestMapping("/system/role")
@Transactional(readOnly = true)
public class RoleController {

    Logger logger = Logger.getLogger(RoleController.class);
 

    @Autowired
    EisMenuResourceService resourceService;

    @Autowired
    EisRoleService eisRoleService;
    
    @Autowired
    EisRoleMenuResourceService eisRoleMenuResourceService;

    @RequestMapping
    public void index() {
   
    }

    @RequestMapping("/list")
    @ResponseBody
    public DataGrid<EisRoleWithMenuReource> list(Integer page,Integer rows) {
    	com.github.pagehelper.Page<Object> pg = null;
    	if(page != null && rows != null) {
    		  pg = PageHelper.startPage(page, rows);
    	}
     
    	List<EisRoleWithMenuReource> rsResources = listAll();
        DataGrid<EisRoleWithMenuReource> dg = new DataGrid<EisRoleWithMenuReource>(rsResources);
        if ( pg != null) {
    		dg.setTotal(pg.getTotal());
        }else {
        	if (rsResources.size()!=0) {
               	dg.setTotal(1L);
        	}else {
        		dg.setTotal(0L);
        	}
 
        }

        return  dg;
    }
    
    
    
    @RequestMapping("/listRoles")
    @ResponseBody
    public List<EisRoleWithMenuReource> listAll() {
    	List<EisRoleWithMenuReource> rsResources = new ArrayList<>();
     	List<EisRole> lst = eisRoleService.findAll();
    	for (EisRole role:lst) {
    		EisRoleWithMenuReource one = new EisRoleWithMenuReource();
    		one.setName(role.getName());
    		one.setRemarks(role.getRemarks());
    		one.setRoleId(role.getRoleId());
    		one.setStatus(role.getStatus());
    		List<EisRoleMenuResource>   lstMenus = eisRoleMenuResourceService.selectByRoleId(role.getRoleId());
    		List<Long> menuIds = new ArrayList<>();
    		for (EisRoleMenuResource menu: lstMenus) {
    			menuIds.add(menu.getMenuId());
    		}
    		one.setMenuIds(menuIds); 
    		rsResources.add(one);
    	}
    	
       
        return  rsResources;
    }
    
    
    

    @RequestMapping({"/save" })
    @Transactional
    @ResponseBody
    public Object save(@Valid EisRole role, BindingResult br) {
        if (br.hasErrors()) {
            logger.error("对象校验失败：" + br.getAllErrors());
            return new AjaxResult(false).setData(br.getAllErrors());
        } else {
            return eisRoleService.addRole(role);
        }
    }
    
    @RequestMapping("/update")
    @Transactional
    @ResponseBody
    public Object update(@Valid EisRole role,BindingResult br) {
    	if (br.hasErrors()) {
    		  logger.error("对象校验失败：" + br.getAllErrors());
              return new AjaxResult(false).setData(br.getAllErrors());
    	} else {
    		  return eisRoleService.updateRole(role);
    	}
    }
    
    
    
    

    @RequestMapping("/delete")
    @Transactional
    @ResponseBody
    public AjaxResult delete(Integer id) {
        try {
        	eisRoleService.deleteRole(id);
        } catch (Exception e) {
            return new AjaxResult().setMessage(e.getMessage());
        }
        return new AjaxResult();
    }

    @RequestMapping("/resource/tree")
    @ResponseBody
    public Iterable<EisMenuResource> resourceTree() {
       return resourceService.getResourceTree(true);
    }

    /**
     * 角色关联资源
     *
     * @param roleId
     * @param resourceId
     * @return
     */
    @RequestMapping("/resource/save")
    @Transactional
    @ResponseBody
    public AjaxResult resourceSave(Integer roleId, Long[] resourceId) {
    	
    	EisRole role = eisRoleService.selectByPrimaryKey(roleId);
    	List<Long> resourceids = new ArrayList<>();
        if (role != null && resourceId != null && resourceId.length > 0) {
            try {
                for (Long rid : resourceId) {
                    if (rid != null) {
                    	resourceids.add(rid);
                    }
                }
                eisRoleMenuResourceService.deleteRoleMenu(roleId);
                eisRoleMenuResourceService.addRoleMenu(role.getRoleId(),resourceids);
                return new AjaxResult();
            } catch (Exception e) {
                logger.error("角色资源关联错误", e);
            }
        }
        return new AjaxResult(false, "关联失败！");
    }
}
